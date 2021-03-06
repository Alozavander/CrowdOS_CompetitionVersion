package com.hills.mcs_02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hills.mcs_02.activities.Activity_2rdPage;
import com.hills.mcs_02.activities.Activity_Task_Detail;
import com.hills.mcs_02.activities.Activity_editInfo;
import com.hills.mcs_02.activities.Activity_func_FoodShare;
import com.hills.mcs_02.activities.Activity_func_sportShare;
import com.hills.mcs_02.activities.Activity_login;
import com.hills.mcs_02.activities.SearchActivity;
import com.hills.mcs_02.dataBeans.Bean_ListView_home;
import com.hills.mcs_02.dataBeans.Liveness;
import com.hills.mcs_02.downloadPack.DownloadFileUtils;
import com.hills.mcs_02.downloadPack.DownloadListener;
import com.hills.mcs_02.fragmentsPack.Fragment_home;
import com.hills.mcs_02.fragmentsPack.Fragment_map;
import com.hills.mcs_02.fragmentsPack.Fragment_mine;
import com.hills.mcs_02.fragmentsPack.Fragment_publish;
import com.hills.mcs_02.fragmentsPack.Fragment_remind;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_LivenessLogin;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_LivenessLogout;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequest_mine_minor7_update;
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
  //???????????????????????????????????????
  private String appName;
  //GPS
  private LocationManager locationManager;
  private int GPS_REQUEST_CODE = 1;
  static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.READ_PHONE_STATE};
  private static final int READ_PHONE_STATE = 100;//??????????????????
  private FragmentManager fragmentManager;
  //??????????????????????????????????????????????????????????????????tag
  private String BNV_tag = "None";
  //Test_?????????????????????
  //????????????????????????????????????????????????
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
  private String apkLocalPath;

  //??????sensorService??????
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

    //???????????????Context
    sendContex = this;

    initBNV();


        /*//??????
        User user = new User(123, "??????????????????1", "??????????????????2", "??????????????????", 1000000000);
        User_serialized user_serialized = new User_serialized();
        user_serialized.setUser_serialized(user);
        System.out.println("user: " + user_serialized);
        String user_info = null;
        try {
            user_info = SerializableUtil.obj2Str(user_serialized);
            System.out.println("user_info_seri: " + user_info);
            SharedPreferences tem_sp = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor tem_editor = tem_sp.edit();
            tem_editor.putString("user_info", user_info);
            tem_editor.commit();

            String reverseInfo = tem_sp.getString("user_info","notFind");
            System.out.println("reverseInfo: " + reverseInfo);
            User_serialized reverse_user_2 = (User_serialized) SerializableUtil.str2Obj(reverseInfo);
            System.out.println("reverseUser_Info:" + reverse_user_2);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    //?????????Fragment??????????????????,initFragment???????????????????????????????????????????????????InstanceState??????????????????
    if (savedInstanceState == null) {
      initFragment();
    }
    //Toast.makeText(this,"?????????sharePreference???",Toast.LENGTH_SHORT).show();


    initUser_Info();
    initService();


    initPermission();


    checkVersion();
  }


  private void LivenessInit() {
    int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
    //??????????????????
    if (login_userID != -1) {
      Liveness lLiveness = new Liveness(null, login_userID, null, null, null, null, null, null, null, null);
      Gson lGson = new Gson();
      String content = lGson.toJson(lLiveness);
      //??????Retrofit??????   getResources().getString(R.string.base_url)   "http://192.168.43.60:8889/"
      //????????????url
      Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
      //????????????????????????
      PostRequest_LivenessLogin lLivenessLogin = retrofit.create(PostRequest_LivenessLogin.class);
      //??????RequestBody
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
    //??????BNV?????????????????????
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
            //??????????????????
            if (login_userID == -1) {
              //??????????????????????????????
              BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
              //???remind??????false
              navigationView.getMenu().getItem(3).setChecked(false);
              //?????????????????????????????????
              navigationView.getMenu().getItem(JudgeFragmentMenuItemIndex(BNV_tag)).setChecked(true);
              BNVIconFresh();
              //?????????????????????
              Intent intent = new Intent(MainActivity.this, Activity_login.class);
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
    //???????????????????????????????????????
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    //?????????????????????????????????
    Resources resource = (Resources) getBaseContext().getResources();
    ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.bottom_navigation_color_list, null);
    navigation.setItemTextColor(csl);
    navigation.setItemIconTintList(null);
    navigation.getMenu().getItem(0).setChecked(true);
    navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
    //?????????????????????
    BNV_tag = "Tag_fragment_home";
  }

  private void initUser_Info() {
    //??????????????????????????????sharedPreference
    SharedPreferences userSP = getSharedPreferences("user", MODE_PRIVATE);
    userSP.edit().putString("userID", "-1");   //userID?????????-1?????????
    userSP.edit().commit();
  }

  private void initService() {
    //mReadableDatabase = new SensorSQLiteOpenHelper(this).getReadableDatabase();
    //isBind = false;
    Log.i(TAG, "=======Now Init the sensor Service...===========");
    //??????????????????????????????Service
    sh = new SenseHelper(this);
    Intent intent = new Intent(MainActivity.this, SensorService.class);
    if (conn == null) {
      Log.i(TAG, "===========connection creating...============");
      conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

          sensorService_interface = (SensorService_Interface) service;
          Log.i(TAG, "sensorService_interface connection is done.");
          //????????????????????????????????????????????????
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
  }

  @Override
  protected void onStart() {
    //????????????????????????
    LivenessInit();
    super.onStart();
  }

  private void openGPSsetting() {
    if (checkGpsIsOpen()) {
      Log.e(TAG, "GPS?????????");
      Toast.makeText(this, "GPS?????????", Toast.LENGTH_SHORT).show();
      if (Build.VERSION.SDK_INT >= 23) { //???????????????android6.0???????????????????????????????????????????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// ??????????????????????????????
          ActivityCompat.requestPermissions(this, LOCATIONGPS, READ_PHONE_STATE);
        }
      }
    } else {
      new AlertDialog.Builder(this).setTitle("??????GPS")
          .setMessage("????????????")
          //  ????????????
          .setNegativeButton("??????", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              Log.e(TAG, "GPS??????????????????");
              // ??????dialog
              dialogInterface.dismiss();
            }
          })
          //  ????????????
          .setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //?????????????????????????????????
              Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              //???????????????
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
    //??????FrgamentManager,?????????????????????Fragment
    fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(R.id.fragment_container, new Fragment_home(), "Tag_fragment_home");
    transaction.addToBackStack("Tag_fragment_home");
    transaction.commit();
  }

  private void BNVIconFresh() {
    //??????????????????????????????
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home);
    navigation.getMenu().getItem(1).setIcon(R.drawable.navi_map);
    navigation.getMenu().getItem(2).setIcon(R.drawable.navi_publish);
    navigation.getMenu().getItem(3).setIcon(R.drawable.navi_remind);
    navigation.getMenu().getItem(4).setIcon(R.drawable.navi_mine);
  }

  private void AddFragment(String tag) {
    //??????tag
    BNV_tag = tag;
    //??????Transaction
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //????????????Fragment
    hideCurrentFragment(transaction);
    //ICON??????
    BNVIconFresh();

    //??????????????????????????????
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

    //?????????????????????Fragment,????????????????????????
    if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
      switch (tag) {
        case "Tag_fragment_home": {
          transaction.add(R.id.fragment_container, new Fragment_home(), tag);
          navigation.getMenu().getItem(0).setIcon(R.drawable.navi_home_press);
          break;
        }
        case "Tag_fragment_mine": {
          transaction.add(R.id.fragment_container, new Fragment_mine(), tag);
          navigation.getMenu().getItem(4).setIcon(R.drawable.navi_mine_press);
          break;
        }
        case "Tag_fragment_map": {
          transaction.add(R.id.fragment_container, new Fragment_map(), tag);
          navigation.getMenu().getItem(1).setIcon(R.drawable.navi_map_press);
          break;
        }
        case "Tag_fragment_publish": {
          transaction.add(R.id.fragment_container, new Fragment_publish(), tag);
          navigation.getMenu().getItem(2).setIcon(R.drawable.navi_publish_press);
          break;
        }
        case "Tag_fragment_remind": {
          transaction.add(R.id.fragment_container, new Fragment_remind(), tag);
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

  //???????????????????????????????????????
  private void hideCurrentFragment(FragmentTransaction currentTransaction) {
    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
      if (fragment != null && fragment.isVisible()) {
        currentTransaction.hide(fragment);
      }
    }
  }


  //MainActivity?????????????????????Bean???????????????????????????????????????????????????????????????
  //??????????????????????????????????????????
  public void setBean(Bean_ListView_home bean) {
    this.bean = bean;
  }

  public Bean_ListView_home getBean() {
    return bean;
  }


  //??????For_Test????????????????????????????????????????????????????????????????????????
  @Override
  public void button_AddItem() {
    //bean =  new Bean_ListView_home(R.drawable.haimian_usericon, R.drawable.takephoto, R.drawable.star_1, R.drawable.testphoto_4, "????????????" , "20?????????", "????????????", "???????????????????????????????????????????????????",  "5.0 ???", "3 ???", "???????????????2018.12.12");
  }

  //????????????????????????
  //?????????????????????????????????????????????Tag??????????????????????????????ListView???position??????????????????????????????????????????
  @Override
  public void jump_to_2rdPage(String pageTag, int position) {
    Intent intent = new Intent(MainActivity.this, Activity_2rdPage.class);
    intent.putExtra("pageTag", pageTag);
    intent.putExtra("position", position);
    startActivity(intent);
  }

  public void jump_to_loginPage() {
    Intent intent = new Intent(MainActivity.this, Activity_login.class);
    startActivity(intent);
  }

  @Override
  public void jump_to_TaskDetailActivity(String taskGson) {
    Intent intent = new Intent(MainActivity.this, Activity_Task_Detail.class);
    intent.putExtra("taskGson", taskGson);
    startActivity(intent);
  }

  public void jump_to_SearchActivity() {
    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
    startActivity(intent);
  }

  public void jump_to_func_sportActivity() {
    Intent intent = new Intent(MainActivity.this, Activity_func_sportShare.class);
    startActivity(intent);
  }

  public void jump_to_func_foodActivity() {
    Intent intent = new Intent(MainActivity.this, Activity_func_FoodShare.class);
    startActivity(intent);
  }

  @Override
  public void jump_to_editInfo() {
    Intent intent = new Intent(MainActivity.this, Activity_editInfo.class);
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
    PostRequest_mine_minor7_update request = retrofit.create(PostRequest_mine_minor7_update.class);
    int versionCode = BuildConfig.VERSION_CODE;
    Log.i(TAG, "VersionCode:" + versionCode);
    appName = null;
    Call<ResponseBody> call = request.query_published(versionCode);
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
    //???????????????????????????
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setCancelable(false);
    builder.setTitle(R.string.App_Update);
    builder.setMessage(R.string.update_message);
    builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //????????????????????????????????????progrossbar
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
    //??????????????????dialog
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

    //???????????????????????????
    NumberProgressBar bar = dialog.findViewById(R.id.dialog_progressbar);

    //?????????????????????
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
    //??????????????????????????? System.currentTimeMillis() + appName
    newApp = new DownloadFileUtils(getString(R.string.base_url)).downloadFile(System.currentTimeMillis() + appName, listener);
  }

  private void getInstallPermission(String localPath) {
    if (!getPackageManager().canRequestPackageInstalls()) {
      System.out.println("can not request installs");
      //???????????????????????????
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setCancelable(false);
      builder.setTitle("????????????");             //???????????????string?????????
      builder.setMessage("?????????????????????");
      builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          //????????????????????????????????????
          Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
          startActivityForResult(intent, 10086);//???????????????????????????code??????????????????????????????
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
      //??????Intent?????????????????????????????????
      Intent intent1 = new Intent(Intent.ACTION_VIEW);
      Uri uri;
      //??????????????????????????????
      uri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".fileprovider", newApp);
      intent1.setDataAndType(uri, "application/vnd.android.package-archive");
      try {
        if (getPackageManager().canRequestPackageInstalls()) {
          System.out.println("JUMP TO APK");
          intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          startActivity(intent1);
        }
      } catch (ActivityNotFoundException e) {
        e.printStackTrace();
      }
      System.out.println("openAPK over");
    } else {
      Toast.makeText(MainActivity.this, "Download Failure.", Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // ??????????????????????????????
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      // ???????????????????????????Fragment??????
      int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
      // ???????????????????????????fragment??????,????????????1??????????????????????????????????????????????????????
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



                /*
                // ??????????????????
                fragmentManager.popBackStackImmediate();
                // ??????????????????????????????Fragment???,?????????????????????Fragment?????????????????????
                FragmentManager.BackStackEntry backStack = fragmentManager
                        .getBackStackEntryAt(fragmentManager
                                .getBackStackEntryCount() - 1);
                // ?????????????????????Fragment????????????
                String tag = backStack.getName();
                BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                // ??????????????????????????????
                if ("Tag_fragment_home".equals(tag)) {
                    // ??????????????????
                    navigationView.getMenu().getItem(0).setChecked(true);
                } else if ("Tag_fragment_map".equals(tag)) {
                    // ?????????????????????
                    navigationView.getMenu().getItem(1).setChecked(true);
                } else if ("Tag_fragment_publish".equals(tag)) {
                    // ?????????????????????
                    navigationView.getMenu().getItem(2).setChecked(true);
                } else if ("Tag_fragment_remind".equals(tag)) {
                    // ?????????????????????
                    navigationView.getMenu().getItem(3).setChecked(true);
                } else if ("Tag_fragment_mine".equals(tag)) {
                    // ?????????????????????
                    navigationView.getMenu().getItem(4).setChecked(true);
                }
                */
      } else {
        //???????????????????????????,????????????
        finish();
      }
    }
    return true;
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //??????????????????GPS?????????????????????!
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

  public static Context getMainContext() {
    return sendContex;
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
    //????????????????????????
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

  //???????????????????????????????????????
  private void userLogout() {
    int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
    //??????????????????
    if (login_userID != -1) {
      Liveness lLiveness = new Liveness(null, login_userID, null, null, null, null, null, null, null, null);
      Gson lGson = new Gson();
      String content = lGson.toJson(lLiveness);
      //??????Retrofit??????
      Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
      //????????????url
      //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();
      //????????????????????????
      PostRequest_LivenessLogout lLivenessLogout = retrofit.create(PostRequest_LivenessLogout.class);
      //??????RequestBody
      RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);
      Call<ResponseBody> call = lLivenessLogout.livenessLogout(contentBody);
      Log.i(TAG, content);
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.code() == 200) {
            Log.i(TAG, "LivenessLogout success.");
          }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
      });
    }
  }

}
