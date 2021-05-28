package com.hills.mcs_02.sensorFunction;

import com.google.gson.Gson;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hills.mcs_02.dataBeans.SensorDetail;
import com.hills.mcs_02.MainActivity;
import com.hills.mcs_02.networkClasses.interfacesPack.PostRequestSensorDetailUploadService;
import com.hills.mcs_02.saveFile.FileExport;
import com.hills.mcs_02.StringStore;
import com.hills.mcs_02.utils.SqliteTimeUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenseDataUploadService extends Service {
  private static final String TAG = "SenseDataUploadService";
  private Timer mTimer;
  private ExecutorService threadPool;
  private int UserID;

  @Nullable
  @Override
  public IBinder onBind(Intent pIntent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "SenseDataUploadService is on.");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForeground(1, initFrontServiceNotification());
    }
    ;
    initTimer();
    threadPool = Executors.newFixedThreadPool(3);
  }


  private Notification initFrontServiceNotification() {
    //获取notification的manager
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    //创建channel
    NotificationChannel lChannel = new NotificationChannel("channel_sensedataupload", "sensedataupload", NotificationManager.IMPORTANCE_DEFAULT);
    notificationManager.createNotificationChannel(lChannel);
    Intent lNotificationIntent = new Intent(this, MainActivity.class);
    PendingIntent lPendingIntent = PendingIntent.getActivity(this, 0, lNotificationIntent, 0);
    //Use builder to build a notification
    Notification.Builder lBuilder = new Notification.Builder(this, "channel_sensedataupload").setContentIntent(lPendingIntent).setContentTitle("SenseDataUpload").setContentText("collecting data...");
    Notification lNotification = lBuilder.build();
    return lNotification;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  private void initTimer() {
    Log.i(TAG, "Upload Timer starts");
    mTimer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        UploadTask();
      }
    };
    //per hour a collection for a sensing data
    mTimer.schedule(task, 1, 60 * 60 * 1000);
    Log.i(TAG, "Upload Timer Task now starts");
  }

  public void UploadTask() {
    String[] nowTime = SqliteTimeUtil.getStartAndEndTime();
    String startTime = nowTime[0];
    String endTime = nowTime[1];
    Log.i(TAG, "StartTime is : " + startTime);
    Log.i(TAG, "EndTime is : " + endTime);
    createAllSensorDataFile(startTime, endTime);
  }

  private void createAllSensorDataFile(String pStartTime, String pEndTime) {
    int[] sensorTypeList = new SenseHelper(this).getSensorListTypeIntIntegers();
    for (int i : sensorTypeList) {
      String whereClaus = StringStore.SENSOR_DATATABLE_SENSE_TYPE + "=?" + " AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME
          + " > ? AND " + StringStore.SENSOR_DATATABLE_SENSE_TIME + " < ?";
      Cursor cursor = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SENSOR_DATATABLE_NAME,
          new String[]{StringStore.SENSOR_DATATABLE_SENSE_TYPE,
              StringStore.SENSOR_DATATABLE_SENSE_TIME,
              StringStore.SENSOR_DATATABLE_SENSE_DATA_1,
              StringStore.SENSOR_DATATABLE_SENSE_DATA_2,
              StringStore.SENSOR_DATATABLE_SENSE_DATA_3},
          whereClaus, new String[]{i + "", pStartTime, pEndTime}, null, null, null);
      if (cursor.getCount() > 0) {
        Log.i(TAG, "There has " + cursor.getCount() + " data for sensor " + i);
        File saveFile = FileExport.exportToTextForEachSensor(cursor, i + "_" + SqliteTimeUtil.getCurrentTimeNoSpaceAndColon() + ".txt", null);
        Log.i(TAG, "The sensor data file size is " + saveFile.length());
        if (saveFile == null) continue;
        //使用线程池中的线程执行uploadFile任务
        threadPool.execute(new Runnable() {
          @Override
          public void run() {
            Log.i(TAG, "Now the " + Thread.currentThread().getName() + " upload the sensor " + i + " data");
            uploadFile(saveFile, i);
            Log.i(TAG, "Now the " + Thread.currentThread().getName() + " upload the sensor " + i + " data task done");
          }
        });
      } else Log.i(TAG, "The sensor " + i + " has no new data");
      cursor.close();
    }
  }

  private void uploadFile(File pSaveFile, int sensorType) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");// HH:mm:ss
    // 获取当前时间
    Date date = new Date(System.currentTimeMillis());
    //构建上传的数据类
    SensorDetail lSensorDetail = new SensorDetail(null, UserID, null, null, pSaveFile.getName(), sensorType + "", simpleDateFormat.format(date), null, null, null);
    Gson gson = new Gson();
    String postTask = gson.toJson(lSensorDetail);
    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postTask);
    //构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
    MultipartBody.Part body = MultipartBody.Part.createFormData("file", pSaveFile.getName(), RequestBody.create(okhttp3.MediaType.parse("file/*"), pSaveFile));
    //创建Retrofit实例
    //Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
    //TestUpload
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:10101/").addConverterFactory(GsonConverterFactory.create()).build();
    //创建网络接口实例
    PostRequestSensorDetailUploadService request = retrofit.create(
        PostRequestSensorDetailUploadService.class);
    Call<ResponseBody> call = request.uploadSensorMessage(requestBody, body);
    Log.i(TAG, "The Upload URL is: " + call.request().url() + "\n The content is:" + postTask);

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200) {
          Log.i(TAG, pSaveFile.getName() + " upload done.");
        } else {
          Log.i(TAG, "The response code is not 200, upload failed.");
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable throwable) {

      }
    });
  }
}
