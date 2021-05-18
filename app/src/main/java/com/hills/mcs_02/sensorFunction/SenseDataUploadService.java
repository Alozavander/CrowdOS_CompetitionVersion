package com.hills.mcs_02.sensorFunction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hills.mcs_02.MainActivity;

public class SenseDataUploadService extends Service {
    private static final String TAG = "SenseDataUploadService";

    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"SenseDataUploadService is on.");

        //获取notification的manager
        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建channel
        NotificationChannel lChannel = new NotificationChannel("channel_sensedataupload","sensedataupload", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(lChannel);
        Intent lnotificationIntent = new Intent(this, MainActivity.class);
        PendingIntent lPendingIntent = PendingIntent.getActivity(this,0,lnotificationIntent,0);
        //Use builder to build a notification
        Notification.Builder lBuilder = new Notification.Builder(this,"channel_sensedataupload").setContentIntent(lPendingIntent).setContentTitle("SenseDataUpload").setContentText("collecting data...");
        Notification lNotification = lBuilder.build();
        startForeground(1,lNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
