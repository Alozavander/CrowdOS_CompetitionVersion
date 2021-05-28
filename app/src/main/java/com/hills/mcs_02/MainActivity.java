
package com.hills.mcs_02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hills.mcs_02.activities.ActivityEditInfo;
import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.activities.ActivityTaskDetail;
import com.hills.mcs_02.activities.SearchActivity;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.fragmentsPack.FragmentHome;
import com.hills.mcs_02.fragmentsPack.FragmentMine;
import com.hills.mcs_02.fragmentsPack.FragmentPublish;
import com.hills.mcs_02.fragmentsPack.FragmentRemind;
import com.hills.mcs_02.main.MainAlertDialogGenerator;
import com.hills.mcs_02.main.MainRetrofitCallGenerator;
import com.hills.mcs_02.main.OpenApk;
import com.hills.mcs_02.main.UserLivenessFunction;
import com.hills.mcs_02.sensorFunction.SenseDataUploadService;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorService;
import com.hills.mcs_02.sensorFunction.SensorServiceInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ForTest {
    public static final String TAG = "MainActivity";
    public static final String dbPath = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";
    public int PERMISSION_REQUEST_CODE = 10001;
    private String appName;
    private LocationManager locationManager;
    private int GpsRequestCode = 1;
    /** Location permission requests */
    static final String[] LOCATION_GPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE};
    private static final int READ_PHONE_STATE = 100;
    private FragmentManager fragmentManager;
    /** Customize the click listener in the BottomNavigationBar  */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private String apkLocalPath;
    public List<Fragment> mFragmentList;
    public int lastFragmentIndex = 0;

    /** Open sensorService */
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
        /** The initialization function when initFragment is executed only when there is no pre-reserved InstanceState  */
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
        /** Check login or logout */
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
            /** Do not have permissions, request them now */
            EasyPermissions.requestPermissions(this, getString(R.string.permission),
                PERMISSION_REQUEST_CODE, perms);
        }
    }

    private void initBottomNavigationView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
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
                        /** Check Login or Logout */
                        if (loginUserId == -1) {
                            bottomNavigationViewIconFresh();
                            /** Jump to the login page  */
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
        /** Navigation bar color, text and other settings  */
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.bottom_navigation_color_list, null);
        mBottomNavigationView.setItemTextColor(csl);
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
        mBottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
    }

    private void initUserInfo() {
        /** Init the user login infomation with sharedPreference */
        SharedPreferences userSp = getSharedPreferences("user", MODE_PRIVATE);
        userSp.edit().putString("userID", "-1");   //userID设置为-1初始化
        userSp.edit().commit();
    }

    private void initService() {
        Log.i(TAG, "=======Now Init the sensor Service...===========");
        /** Init the sensor sensing function Service */
        sh = new SenseHelper(this);
        Intent intent = new Intent(MainActivity.this, SensorService.class);
        if (conn == null) {
            Log.i(TAG, "===========connection creating...============");
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    sensorServiceInterface = (SensorServiceInterface) service;
                    Log.i(TAG, "sensorService_interface connection is done.");
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
        /** Open the SensorDataUploadService */
        int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        if(userId == -1){
            Log.i(TAG,"SenseDataUploadService is not on because of logout.");
        }else{
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
        /** Init liveness function */
        livenessInit();
        super.onStart();
    }

    private void bottomNavigationViewIconFresh() {
        /** Add the icon for the BottomNavigationBar */
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
            MainAlertDialogGenerator
                .getGPSPermissionDialog(MainActivity.this).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int temp) {
                    /** Jump to phone setting page */
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
        /** Get the FragmentManager and init the homepage Fragment */
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

    @Override
    public void jumpToEditInfo() {
        Intent intent = new Intent(MainActivity.this, ActivityEditInfo.class);
        startActivity(intent);
    }

    /** Check the new version */
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
        /** Pop up the download box  */
        AlertDialog.Builder builder = MainAlertDialogGenerator.getDownAlertDialog(MainActivity.this);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Sengd download request and display the progressbar */
                downloadNewApp();
            }
        });
        builder.show();
    }

    private void downloadNewApp() {
        File newApp = null;
        AlertDialog dialog = MainAlertDialogGenerator.getProgressbarDownAlertDialog(MainActivity.this).show();
        /** Get the progressbar view in layout */
        NumberProgressBar bar = dialog.findViewById(R.id.dialog_progressbar);
        /** Create the download listener */
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
        /** Get the file after dowoloading */
        newApp = new DownloadFileUtils(getString(R.string.base_url)).downloadFile(System.currentTimeMillis() + appName, listener);
    }

    private void getInstallPermission(String localPath) {
        if (!getPackageManager().canRequestPackageInstalls()) {
            System.out.println("can not request installs");
            /** Pop up the update remind box */
            AlertDialog.Builder builder = MainAlertDialogGenerator.getInstallPermissionDialog(MainActivity.this);
            builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /** Go to the phone setting page to request the setup permission */
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 10086);//注意此处要对返回的code进行识别并进行再判断
                }
            });
            builder.show();
        }
    }

    private void openAPK(File newApp) {
        OpenApk lOpenApk = new OpenApk(MainActivity.this, newApp);
        lOpenApk.openApk();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        /** Forward results to EasyPermissions */
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        /** Get the logout state */
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

    /** The liveness function method */
    private void userLogout() {
        int loginUserId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        String url = getResources().getString(R.string.base_url);
        if (loginUserId != -1) {
            UserLivenessFunction ulFunction = new UserLivenessFunction(MainActivity.this);
            ulFunction.userLogout(loginUserId, url);
        }
    }
}