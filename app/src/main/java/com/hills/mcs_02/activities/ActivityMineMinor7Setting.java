package com.hills.mcs_02.activities;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.BuildConfig;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_mine_minor7_update;
import com.hills.mcs_02.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ActivityMineMinor7Setting extends BaseActivity {
    private String TAG = "Activity_mine_minor7_setting";
    private ListView mListView;
    private String appName;
    private String apkLocalPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor7_setting);
        //初始化列表
        initList();
        initBackBT();
    }

    private void initBackBT() {
        ImageView backIv = findViewById(R.id.minepage_minor7_backarrow);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initList() {
        List<String> list = new ArrayList<>();

        //测试所用
        list.add(getResources().getString(R.string.setting_general));
        list.add(getResources().getString(R.string.setting_help_feedback));
        list.add(getResources().getString(R.string.setting_version));

        mListView = findViewById(R.id.minepage_minor7_lv);
        mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_item_minepage_minor7,list));
        //列表监听器暂时不管-添加点击 设置 进入设置选择界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting -------");
                System.out.println("position = " + position);
                if(position == 0) {
                    Intent intent = new Intent(ActivityMineMinor7Setting.this, ActivityMineSettingGeneral.class);
                    startActivity(intent);
                }
                //在这里填写版本更新相关逻辑代码
                else if(position == 2){
                    checkVersion();
                }
                else Toast.makeText(ActivityMineMinor7Setting.this,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkVersion() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        PostRequest_mine_minor7_update request = retrofit.create(PostRequest_mine_minor7_update.class);
        int versionCode = BuildConfig.VERSION_CODE;
        appName = null;
        Call<ResponseBody> call = request.query_published(versionCode);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    downAlertDialog();
                    try {
                        appName = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //TODO：这里没有返回的是404
                else{
                    Toast.makeText(ActivityMineMinor7Setting.this,"暂无新版本可下载",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private void downAlertDialog(){
        //弹出下载的提示框口
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMineMinor7Setting.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.App_Update);
        builder.setMessage(R.string.update_message);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送下载应用请求，并显示progrossbar
                downloadNewApp();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadNewApp() {
        File newApp = null;
        //创建带进度的dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.App_Update);
        builder.setMessage(" ");
        builder.setView(R.layout.progressbar_layout);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.show();

        //拿到布局中的进度条
        NumberProgressBar bar = dialog.findViewById(R.id.dialog_progressbar);

        //创建下载监听器
        DownloadListener listener = new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int currentLength) {
                bar.setProgress(currentLength);
            }

            @Override
            public void onFinish(String localPath) {
                dialog.dismiss();
                apkLocalPath = localPath;
                Looper.prepare();
                if(!getPackageManager().canRequestPackageInstalls()) {
                    getInstallPermission(localPath);
                }else{
                    System.out.println("JUMP TO APK");
                    openAPK(new File(localPath));
                }
                Looper.loop();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(ActivityMineMinor7Setting.this, "Download Failure.", Toast.LENGTH_SHORT).show();
            }
        };
        //加入后台对标的网址
        newApp = new DownloadFileUtils(getString(R.string.base_url)).downloadFile(System.currentTimeMillis() + appName,listener);
    }




    private void getInstallPermission(String localPath) {
        if(! getPackageManager().canRequestPackageInstalls()) {
            System.out.println("can not request installs");
            //弹出下载的提示框口
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMineMinor7Setting.this);
            builder.setCancelable(false);
            builder.setTitle("权限授予");             //后续更改到string文件中
            builder.setMessage("请给予安装权限");
            builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //前往设置页面检查开放权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10086);//注意此处要对返回的code进行识别并进行再判断
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void openAPK(File newApp) {
        if (newApp.isFile()) {
            //通过Intent跳转到下载的文件并打开
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri;
            //对不同版本特性做适配
            uri = FileProvider.getUriForFile(ActivityMineMinor7Setting.this,getPackageName() + ".fileprovider", newApp);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            try{
                if(getPackageManager().canRequestPackageInstalls()) {
                    System.out.println("JUMP TO APK");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }catch (ActivityNotFoundException exp){
                exp.printStackTrace();
            }
            System.out.println("openAPK over");
        } else {
            Toast.makeText(ActivityMineMinor7Setting.this, "APK Download Failure.", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"APK Download Failure.");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10086){
            if(!getPackageManager().canRequestPackageInstalls()) {
                getInstallPermission(apkLocalPath);
            }else{
                openAPK(new File(apkLocalPath));
            }
        }
    }
}