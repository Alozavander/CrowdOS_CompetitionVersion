
package com.hills.mcs_02;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.Manifest;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hills.mcs_02.activities.ActivityEditInfo;
import com.hills.mcs_02.activities.ActivityFuncFoodShare;
import com.hills.mcs_02.activities.ActivityFuncSportShare;
import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.activities.ActivitySecondPage;
import com.hills.mcs_02.activities.ActivityTaskDetail;
import com.hills.mcs_02.activities.SearchActivity;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.fragmentsPack.FragmentHome;
import com.hills.mcs_02.fragmentsPack.FragmentMine;
import com.hills.mcs_02.fragmentsPack.FragmentPublish;
import com.hills.mcs_02.fragmentsPack.FragmentRemind;
import com.hills.mcs_02.main.MainAlertDilalogGenerator;
import com.hills.mcs_02.main.MainRetrofitCallGenerator;
import com.hills.mcs_02.main.OpenAPK;
import com.hills.mcs_02.main.UserLivenessFunction;
import com.hills.mcs_02.sensorFunction.SenseDataUploadService;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorService;
import com.hills.mcs_02.sensorFunction.SensorServiceInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends BaseActivity implements ForTest {
    public static final String TAG = "MainActivity";
    public static final String dbPath = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";
    public int PERMISSION_REQUEST_CODE = 10001;
    //自己封装的传感器获取信息类
    private String appName;
    //GPS
    private LocationManager locationManager;
    private int GpsRequestCode = 1;
    static final String[] LOCATION_GPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE};
    private static final int READ_PHONE_STATE = 100;//定位权限请求
    private FragmentManager fragmentManager;
    //为解决多重点击底部导航栏导致回退栈错误设立的tag
    //自定义底部导航栏的点击相应监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private String apkLocalPath;
    public List<Fragment> mFragmentList;
    public int lastFragmentIndex = 0;

    //开启sensorService服务
    private SensorServiceInterface sensorServiceInterface;
    private boolean isBind;
    private ServiceConnection conn;
    public SenseHelper sh;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        //涉及到Fragment返回重建问题,initFragment时初始化函数，只有在没有前置保留地InstanceState情况下才执行
        initBottomNavigationView();
        if (savedInstanceState == null) {
            initFragment();
        }
        initUserInfo();
        initService();
        initPermission();
        checkVersion();
    }

    private void livenessInit() {
        int loginUserId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        //检查是否登录
        if (loginUserId != -1) {
            Call<ResponseBody> call = MainRetrofitCallGenerator.getLivenessCall(MainActivity.this, loginUserId, getResources().getString(R.string.base_url));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "LivenessLogin success.");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }
    }

    private void initPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.INSTALL_PACKAGES};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission),
                PERMISSION_REQUEST_CODE, perms);
        }
    }

    private void initBottomNavigationView() {
        //获取底部导航栏并添加监听器
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //创建BNV底部点击监听器
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        bottomNavigationViewIconFresh();
                        setFragmentPosition(0);
                        mBottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
                        break;
                    case R.id.navigation_mine:
                        bottomNavigationViewIconFresh();
                        setFragmentPosition(3);
                        mBottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.navi_mine_press);
                        break;
                    case R.id.navigation_publish:
                        bottomNavigationViewIconFresh();
                        setFragmentPosition(1);
                        mBottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.navi_publish_press);
                        break;
                    case R.id.navigation_remind:
                        int loginUserId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                        //检查是否登录
                        if (loginUserId == -1) {
                            bottomNavigationViewIconFresh();
                            //跳转到登录页面
                            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                            startActivity(intent);
                            mBottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.navi_remind_press);
                        } else {
                            bottomNavigationViewIconFresh();
                            setFragmentPosition(2);
                            mBottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.navi_remind_press);
                            break;
                        }
                        break;
                }
                return true;
            }
        };
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //导航栏颜色，文本等设置
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.bottom_navigation_color_list, null);
        mBottomNavigationView.setItemTextColor(csl);
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
        mBottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
    }

    private void initUserInfo() {
        //为用户登录信息初始化sharedPreference
        SharedPreferences userSp = getSharedPreferences("user", MODE_PRIVATE);
        userSp.edit().putString("userID", "-1");   //userID设置为-1初始化
        userSp.edit().commit();
    }

    private void initService() {
        Log.i(TAG, "=======Now Init the sensor Service...===========");
        //初始化传感器感知服务Service
        sh = new SenseHelper(this);
        Intent intent = new Intent(MainActivity.this, SensorService.class);
        if (conn == null) {
            Log.i(TAG, "===========connection creating...============");
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    sensorServiceInterface = (SensorServiceInterface) service;
                    Log.i(TAG, "sensorService_interface connection is done.");
                    //测试使用调用接口，启动所有传感器
                    sensorServiceInterface.binderSensorOn(sh.getSensorListTypeIntIntegers());
                    Log.i(TAG, "SensorService's sensorOn has been remote.");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "sensorService disconnected.");
                }
            };
        } else {
            Log.i(TAG, "===============sensorService connection exits.================");
        }
        isBind = bindService(intent, conn, BIND_AUTO_CREATE);
        Log.i(TAG, "=============SensorService has been bound :" + isBind + "==============");

        Log.i(TAG, "ForegroundService");
        //启动开源竞赛数据收集服务
        int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        if(userId == -1){
            Log.i(TAG,"SenseDataUploadService is not on because of logout.");
        }else{
            //启动开源竞赛数据收集服务
            Intent lIntent = new Intent(MainActivity.this, SenseDataUploadService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(lIntent);
            } else {
                startService(lIntent);
            }
        }
    }

    @Override
    protected void onStart() {
        //活跃度检测：上线
        livenessInit();
        super.onStart();
    }

    private void bottomNavigationViewIconFresh() {
        //添加底部导航栏的图标
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home);
        navigation.getMenu().getItem(1).setIcon(R.drawable.navi_publish);
        navigation.getMenu().getItem(2).setIcon(R.drawable.navi_remind);
        navigation.getMenu().getItem(3).setIcon(R.drawable.navi_mine);
    }

    private void openGpsSetting() {
        if (checkGpsIsOpen()) {
            Log.e(TAG, "GPS已开启");
            Toast.makeText(this, "GPS已开启", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(this, LOCATION_GPS, READ_PHONE_STATE);
                }
            }
        } else {
            MainAlertDilalogGenerator.getGPSPermissionDialog(MainActivity.this).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int temp) {
                    //跳转到手机原生设置页面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    //测试关注点
                    startActivityForResult(intent, GpsRequestCode);
                }
            }).show();
        }
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void initFragment() {
        //获取FragmentManager,初始化添加首页Fragment
        fragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragmentHome());
        mFragmentList.add(new FragmentPublish());
        mFragmentList.add(new FragmentRemind());
        mFragmentList.add(new FragmentMine());
        setFragmentPosition(0);
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragmentList.get(position);
        mBottomNavigationView.getMenu().getItem(lastFragmentIndex).setChecked(false);
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        Fragment lastFragment = mFragmentList.get(lastFragmentIndex);
        lastFragmentIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.fragment_container, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void buttonAddItem() {
        //bean =  new Bean_ListView_home(R.drawable.haimian_usericon, R.drawable.takephoto, R.drawable.star_1, R.drawable.testphoto_4, "海绵宝宝" , "20分钟前", "公共资源", "任务描述：国安大厦停车位还有吗……",  "5.0 元", "3 个", "截止时间：2018.12.12");
    }

    //接口设置
    //二级页面跳转入口，将二级页面得Tag名称以及处于对应页面ListView的position值作为能够唯一识别的触发条件
    @Override
    public void jumpTo2rdPage(String pageTag, int position) {
        Intent intent = new Intent(MainActivity.this, ActivitySecondPage.class);
        intent.putExtra("pageTag", pageTag);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void jumpToLoginPage() {
        Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
        startActivity(intent);
    }

    @Override
    public void jumpToTaskDetailActivity(String taskGson) {
        Intent intent = new Intent(MainActivity.this, ActivityTaskDetail.class);
        intent.putExtra("taskGson", taskGson);
        startActivity(intent);
    }

    public void jumpToSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void jumpToFuncSportActivity() {
        Intent intent = new Intent(MainActivity.this, ActivityFuncSportShare.class);
        startActivity(intent);
    }

    public void jumpToFuncFoodActivity() {
        Intent intent = new Intent(MainActivity.this, ActivityFuncFoodShare.class);
        startActivity(intent);
    }

    @Override
    public void jumpToEditInfo() {
        Intent intent = new Intent(MainActivity.this, ActivityEditInfo.class);
        startActivity(intent);
    }



    //检查最新版本
    private void checkVersion() {
        appName = null;
        Call<ResponseBody> call = MainRetrofitCallGenerator.getCheckVersionCall(MainActivity.this, getResources().getString(R.string.base_url));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "Response Code:" + response.code());
                if (response.code() == 200) {
                    downAlertDialog();
                    try {
                        appName = response.body().string();
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private void downAlertDialog() {
        //弹出下载的提示框口
        AlertDialog.Builder builder = MainAlertDilalogGenerator.getDownAlertDialog(MainActivity.this);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送下载应用请求，并显示progrossbar
                downloadNewApp();
            }
        });
        builder.show();
    }

    private void downloadNewApp() {
        File newApp = null;
        AlertDialog dialog = MainAlertDilalogGenerator.getProgressbarDownAlertDialog(MainActivity.this).show();
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
                if (!getPackageManager().canRequestPackageInstalls()) {
                    getInstallPermission(localPath);
                } else {
                    openAPK(new File(localPath));
                }
                Looper.loop();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Download Failure.", Toast.LENGTH_SHORT).show();
            }
        };
        //加入后台对标的网址 System.currentTimeMillis() + appName
        newApp = new DownloadFileUtils(getString(R.string.base_url)).downloadFile(System.currentTimeMillis() + appName, listener);
    }

    private void getInstallPermission(String localPath) {
        if (!getPackageManager().canRequestPackageInstalls()) {
            System.out.println("can not request installs");
            //弹出下载的提示框口
            AlertDialog.Builder builder = MainAlertDilalogGenerator.getInstallPermissionDialog(MainActivity.this);
            builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //前往设置页面检查开放权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10086);//注意此处要对返回的code进行识别并进行再判断
                }
            });
            builder.show();
        }
    }

    private void openAPK(File newApp) {
        OpenAPK lOpenAPK = new OpenAPK(MainActivity.this, newApp);
        lOpenAPK.openApk();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果没有打开GPS权限就一直打开!
        if (requestCode == GpsRequestCode) {
            openGpsSetting();
        }
        if (requestCode == 10086) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                getInstallPermission(apkLocalPath);
            } else {
                openAPK(new File(apkLocalPath));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //活跃度检测：下线
        userLogout();
    }

    @Override
    protected void onDestroy() {
        if (isBind) {
            isBind = false;
            unbindService(conn);
        }
        super.onDestroy();
    }

    //服务器活跃度检测功能向方法
    private void userLogout() {
        int loginUserId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        String url = getResources().getString(R.string.base_url);
        //检查是否登录
        if (loginUserId != -1) {
            UserLivenessFunction ulFunction = new UserLivenessFunction(MainActivity.this);
            ulFunction.userLogout(loginUserId, url);
        }
    }
}


