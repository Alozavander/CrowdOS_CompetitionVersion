package com.hills.mcs_02.activities;

import com.daimajia.numberprogressbar.NumberProgressBar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hills.mcs_02.BaseActivity;
import com.hills.mcs_02.BuildConfig;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestMineMinor7Update;
import com.hills.mcs_02.R;

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
        initBackBtn();
    }

    private void initBackBtn() {
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting -------");
                System.out.println("position = " + position);
                if(position == 0) {
                    Intent intent = new Intent(ActivityMineMinor7Setting.this, ActivityMineSettingGeneral.class);
                    startActivity(intent);
                }
                else if(position == 2){
                    checkVersion();
                }
                else Toast.makeText(ActivityMineMinor7Setting.this,getResources().getString(R.string.notYetOpen),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkVersion() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        PostRequestMineMinor7Update request = retrofit.create(PostRequestMineMinor7Update.class);
        int versionCode = BuildConfig.VERSION_CODE;
        appName = null;
        Call<ResponseBody> call = request.queryPublished(versionCode);
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
        /** The prompt box for download pops up */
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMineMinor7Setting.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.App_Update);
        builder.setMessage(R.string.update_message);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Send a request to download the application */
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
        /**  Create a dialog with progress */
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
        /** Get the progress bar in the layout */
        NumberProgressBar bar = dialog.findViewById(R.id.dialog_progressbar);
      /** Create a download listener*/
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
                    openApk(new File(localPath));
                }
                Looper.loop();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(ActivityMineMinor7Setting.this, "Download Failure.", Toast.LENGTH_SHORT).show();
            }
        };
        newApp = new DownloadFileUtils(getString(R.string.base_url)).downloadFile(System.currentTimeMillis() + appName,listener);
    }

    private void getInstallPermission(String localPath) {
        if(! getPackageManager().canRequestPackageInstalls()) {
            System.out.println("can not request installs");
            /**  The prompt box for download pops up */
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMineMinor7Setting.this);
            builder.setCancelable(false);
            builder.setTitle("权限授予");
            builder.setMessage("请给予安装权限");
            builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /**  Go to the Settings page to check for open permissions */
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

    private void openApk(File newApp) {
        if (newApp.isFile()) {
            /** Use Intent to jump to the downloaded file and open it */
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri;
            /** Adapt the features of different versions */
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
                openApk(new File(apkLocalPath));
            }
        }
    }
}