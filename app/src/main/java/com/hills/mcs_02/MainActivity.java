package com.hills.mcs_02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.gson.Gson;
import com.hills.mcs_02.activities.ActivityEditInfo;
import com.hills.mcs_02.activities.ActivityFuncFoodShare;
import com.hills.mcs_02.activities.ActivityFuncSportShare;
import com.hills.mcs_02.activities.ActivityLogin;
import com.hills.mcs_02.activities.ActivitySecondPage;
import com.hills.mcs_02.activities.ActivityTaskDetail;
import com.hills.mcs_02.activities.SearchActivity;
import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.dataBeans.Liveness;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.fragmentsPack.FragmentHome;
import com.hills.mcs_02.fragmentsPack.FragmentMap;
import com.hills.mcs_02.fragmentsPack.FragmentMine;
import com.hills.mcs_02.fragmentsPack.FragmentPublish;
import com.hills.mcs_02.fragmentsPack.FragmentRemind;
import com.hills.mcs_02.main.OpenAPK;
import com.hills.mcs_02.main.UserLivenessFunction;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestLivenessLogin;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestMineMinor7Update;
import com.hills.mcs_02.sensorFunction.SenseDataUploadService;
import com.hills.mcs_02.sensorFunction.SenseHelper;
import com.hills.mcs_02.sensorFunction.SensorService;
import com.hills.mcs_02.sensorFunction.SensorService_Interface;

import java.io.File;
import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements For_test {
  private static Context sendContex;
  public static final String TAG = "MainActivity";
  public static final String dbPath = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";
  public int PERMISSION_REQUEST_CODE = 10001;
  //for now
  private Bean_ListView_home bean;
  //自己封装的传感器获取信息类
  private String appName;
  //GPS
  private LocationManager locationManager;
  private int GPS_REQUEST_CODE = 1;
  static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.READ_PHONE_STATE};
  private static final int READ_PHONE_STATE = 100;//定位权限请求
  private FragmentManager fragmentManager;
  //为解决多重点击底部导航栏导致回退栈错误设立的tag
  private String BNV_tag = "None";
  //Test_为判定是否添加
  //自定义底部导航栏的点击相应监听器
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
  private String apkLocalPath;

  //开启sensorService服务
  private SensorService_Interface sensorService_interface;
  private boolean isBind;
  private ServiceConnection conn;
  public SenseHelper sh;
  public SQLiteDatabase mReadableDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

    //初始化全局Context
    sendContex = this;

    initBNV();

    //涉及到Fragment返回重建问题,initFragment时初始化函数，只有在没有前置保留地InstanceState情况下才执行
    if (savedInstanceState == null) {
      initFragment();
    }

    initUser_Info();
    initService();
    initPermission();
    checkVersion();
  }


  private void LivenessInit() {
    int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
    //检查是否登录
    if (login_userID != -1) {
      Liveness lLiveness = new Liveness(null, login_userID, null, null, null, null, null, null, null, null);
      Gson lGson = new Gson();
      String content = lGson.toJson(lLiveness);
      //测试使用url
      Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
      //创建网络接口实例
      PostRequestLivenessLogin lLivenessLogin = retrofit.create(PostRequestLivenessLogin.class);
      //创建RequestBody
      RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);
      Call<ResponseBody> call = lLivenessLogin.livenessLogin(contentBody);
      Log.i(TAG, content);
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.code() == 200) {
            Log.i(TAG, "LivenessLogin success.");
          }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
      });
    }
  }

  private void initPermission() {
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.INSTALL_PACKAGES};
    if (EasyPermissions.hasPermissions(this, perms)) {

    } else {
      // Do not have permissions, request them now
      EasyPermissions.requestPermissions(this, getString(R.string.permission),
          PERMISSION_REQUEST_CODE, perms);
    }
  }

  private void initBNV() {
    //创建BNV底部点击监听器
    mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.navigation_home:
            if (!BNV_tag.equals("Tag_fragment_home")) {
              AddFragment("Tag_fragment_home");
            }
            return true;
          case R.id.navigation_mine:
            if (!BNV_tag.equals("Tag_fragment_mine")) {
              AddFragment("Tag_fragment_mine");
            }
            return true;
          case R.id.navigation_map:
            if (!BNV_tag.equals("Tag_fragment_map")) {
              AddFragment("Tag_fragment_map");
            }
            return true;
          case R.id.navigation_publish:
            if (!BNV_tag.equals("Tag_fragment_publish")) {
              AddFragment("Tag_fragment_publish");
            }
            return true;
          case R.id.navigation_remind:
            int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
            //检查是否登录
            if (login_userID == -1) {
              BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
              navigationView.getMenu().getItem(3).setChecked(false);
              navigationView.getMenu().getItem(JudgeFragmentMenuItemIndex(BNV_tag)).setChecked(true);
              BNVIconFresh();
              //跳转到登录页面
              Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
              startActivity(intent);
            } else {
              if (!BNV_tag.equals("Tag_fragment_remind")) {
                AddFragment("Tag_fragment_remind");
              }
            }
            return true;
        }
        return false;
      }
    };
    //获取底部导航栏并添加监听器
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    //导航栏颜色，文本等设置
    Resources resource = (Resources) getBaseContext().getResources();
    ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.bottom_navigation_color_list, null);
    navigation.setItemTextColor(csl);
    navigation.setItemIconTintList(null);
    navigation.getMenu().getItem(0).setChecked(true);
    navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
    //初始化当前标签
    BNV_tag = "Tag_fragment_home";
  }

  private void initUser_Info() {
    //为用户登录信息初始化sharedPreference
    SharedPreferences userSP = getSharedPreferences("user", MODE_PRIVATE);
    userSP.edit().putString("userID", "-1");   //userID设置为-1初始化
    userSP.edit().commit();
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

          sensorService_interface = (SensorService_Interface) service;
          Log.i(TAG, "sensorService_interface connection is done.");
          //测试使用调用接口，启动所有传感器
          sensorService_interface.binder_sensorOn(sh.getSensorList_TypeInt_Integers());
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

    Log.i(TAG,"ForegroundService");

    Intent lIntent = new Intent(MainActivity.this, SenseDataUploadService.class);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForegroundService(lIntent);
    } else {
      startService(lIntent);
    }
  }

  @Override
  protected void onStart() {
    //活跃度检测：上线
    LivenessInit();
    super.onStart();
  }

  private void openGPSsetting() {
    if (checkGpsIsOpen()) {
      Log.e(TAG, "GPS已开启");
      Toast.makeText(this, "GPS已开启", Toast.LENGTH_SHORT).show();
      if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
          ActivityCompat.requestPermissions(this, LOCATIONGPS, READ_PHONE_STATE);
        }
      }
    } else {
      new AlertDialog.Builder(this).setTitle("打开GPS")
          .setMessage("前去设置")
          //  取消选项
          .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              Log.e(TAG, "GPS开启过程关闭");
              // 关闭dialog
              dialogInterface.dismiss();
            }
          })
          .setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //跳转到手机原生设置页面
              Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              //测试关注点
              startActivityForResult(intent, GPS_REQUEST_CODE);
            }
          })
          .setCancelable(false)
          .show();
    }
  }

  private boolean checkGpsIsOpen() {
    boolean isOpen;
    isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    return isOpen;
  }


  private void initFragment() {
    //获取FrgamentManager,初始化添加首页Fragment
    fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(R.id.fragment_container, new FragmentHome(), "Tag_fragment_home");
    transaction.addToBackStack("Tag_fragment_home");
    transaction.commit();
  }

  private void BNVIconFresh() {
    //添加底部导航栏的图标
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home);
    navigation.getMenu().getItem(1).setIcon(R.drawable.navi_map);
    navigation.getMenu().getItem(2).setIcon(R.drawable.navi_publish);
    navigation.getMenu().getItem(3).setIcon(R.drawable.navi_remind);
    navigation.getMenu().getItem(4).setIcon(R.drawable.navi_mine);
  }

  private void AddFragment(String tag) {
    BNV_tag = tag;
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    hideCurrentFragment(transaction);
    BNVIconFresh();
    //添加底部导航栏的图标
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    //判定是否添加了Fragment,已存在就直接显示
    if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
      switch (tag) {
        case "Tag_fragment_home": {
          transaction.add(R.id.fragment_container, new FragmentHome(), tag);
          navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
          break;
        }
        case "Tag_fragment_mine": {
          transaction.add(R.id.fragment_container, new FragmentMine(), tag);
          navigation.getMenu().getItem(4).setIcon(R.drawable.navi_mine_press);
          break;
        }
        case "Tag_fragment_map": {
          transaction.add(R.id.fragment_container, new FragmentMap(), tag);
          navigation.getMenu().getItem(1).setIcon(R.drawable.navi_map_press);
          break;
        }
        case "Tag_fragment_publish": {
          transaction.add(R.id.fragment_container, new FragmentPublish(), tag);
          navigation.getMenu().getItem(2).setIcon(R.drawable.navi_publish_press);
          break;
        }
        case "Tag_fragment_remind": {
          transaction.add(R.id.fragment_container, new FragmentRemind(), tag);
          navigation.getMenu().getItem(3).setIcon(R.drawable.navi_remind_press);
          break;
        }
      }
      transaction.addToBackStack(tag);
    } else {
      getSupportFragmentManager().popBackStackImmediate(tag, 0);
      navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
    }
    transaction.commit();
  }

  //自定义方法，隐藏当前分页面
  private void hideCurrentFragment(FragmentTransaction currentTransaction) {
    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
      if (fragment != null && fragment.isVisible()) {
        currentTransaction.hide(fragment);
      }
    }
  }

  @Override
  public void button_AddItem() {
    //bean =  new Bean_ListView_home(R.drawable.haimian_usericon, R.drawable.takephoto, R.drawable.star_1, R.drawable.testphoto_4, "海绵宝宝" , "20分钟前", "公共资源", "任务描述：国安大厦停车位还有吗……",  "5.0 元", "3 个", "截止时间：2018.12.12");
  }

  //实验性的接口设置
  //二级页面跳转入口，将二级页面得Tag名称以及处于对应页面ListView的position值作为能够唯一识别的触发条件
  @Override
  public void jump_to_2rdPage(String pageTag, int position) {
    Intent intent = new Intent(MainActivity.this, ActivitySecondPage.class);
    intent.putExtra("pageTag", pageTag);
    intent.putExtra("position", position);
    startActivity(intent);
  }

  public void jump_to_loginPage() {
    Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
    startActivity(intent);
  }

  @Override
  public void jump_to_TaskDetailActivity(String taskGson) {
    Intent intent = new Intent(MainActivity.this, ActivityTaskDetail.class);
    intent.putExtra("taskGson", taskGson);
    startActivity(intent);
  }

  public void jump_to_SearchActivity() {
    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
    startActivity(intent);
  }

  public void jump_to_func_sportActivity() {
    Intent intent = new Intent(MainActivity.this, ActivityFuncSportShare.class);
    startActivity(intent);
  }

  public void jump_to_func_foodActivity() {
    Intent intent = new Intent(MainActivity.this, ActivityFuncFoodShare.class);
    startActivity(intent);
  }

  @Override
  public void jump_to_editInfo() {
    Intent intent = new Intent(MainActivity.this, ActivityEditInfo.class);
    startActivity(intent);
  }


  private int JudgeFragmentMenuItemIndex(String tag) {
    if (tag.equals("Tag_fragment_home")) return 0;
    if (tag.equals("Tag_fragment_map")) return 1;
    if (tag.equals("Tag_fragment_publish")) return 2;
    if (tag.equals("Tag_fragment_remind")) return 3;
    if (tag.equals("Tag_fragment_mine")) return 4;
    return -1;
  }

  private void checkVersion() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create()).build();
    PostRequestMineMinor7Update request = retrofit.create(PostRequestMineMinor7Update.class);
    int versionCode = BuildConfig.VERSION_CODE;
    Log.i(TAG, "VersionCode:" + versionCode);
    appName = null;
    Call<ResponseBody> call = request.queryPublished(versionCode);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.i(TAG, "Response Code:" + response.code());
        if (response.code() == 200) {
          downAlertDialog();
          try {
            appName = response.body().string();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

      }
    });
  }

  private void downAlertDialog() {
    //弹出下载的提示框口
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    OpenAPK lOpenAPK = new OpenAPK(MainActivity.this,newApp);
    lOpenAPK.openApk();
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // 判断当前按键是返回键
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      // 获取当前回退栈中的Fragment个数
      int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
      // 判断当前回退栈中的fragment个数,如果大于1就直接返回到首页，如果只有首页则退出
      if (backStackEntryCount > 1) {
        String nowPageTag = getSupportFragmentManager()
            .getBackStackEntryAt(getSupportFragmentManager()
                .getBackStackEntryCount() - 1).getName();
        String prePageTag = getSupportFragmentManager()
            .getBackStackEntryAt(getSupportFragmentManager()
                .getBackStackEntryCount() - 2).getName();
        int temPrePosition = JudgeFragmentMenuItemIndex(prePageTag);
        int temNowPosition = JudgeFragmentMenuItemIndex(nowPageTag);
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.getMenu().getItem(temNowPosition).setChecked(false);
        navigationView.getMenu().getItem(temPrePosition).setChecked(true);

        AddFragment(prePageTag);
        Log.i(TAG, "now:" + nowPageTag + "\n" + "pre:" + prePageTag);
        Log.i(TAG, "nowPosition:" + temNowPosition + "\n" + "prePosition:" + temPrePosition);
      } else {
        //回退栈中只剩一个时,退出应用
        finish();
      }
    }
    return true;
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //如果没有打开GPS权限就一直打开!
    if (requestCode == GPS_REQUEST_CODE) {
      openGPSsetting();
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
    int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
    String url = getResources().getString(R.string.base_url);
    //检查是否登录
    if (login_userID != -1) {
      UserLivenessFunction ulFunction = new UserLivenessFunction(MainActivity.this);
      ulFunction.userLogout(login_userID,url);
    }
  }
}
