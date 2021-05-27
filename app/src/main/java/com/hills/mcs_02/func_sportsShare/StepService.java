package com.hills.mcs_02.func_sportsShare;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class StepService extends Service implements SensorEventListener {

    private LcBinder lcBinder = new LcBinder();
     /** Number of current steps */
    private int nowStepCount = 0;
     /** Sensor management objects */
    private SensorManager sensorManager;
    /**  The number of steps acquired in the acceleration sensor */
    private StepCount sStepCount;
    /** The data callback interface notifies the upper caller of the data refresh*/
    private UpdateUiCallBack mCallback;
    /**  Type of pedometer sensor*/
     private static int STEP_SENSOR_TYPE = -1;
    /** Whether the existing step count records are obtained from the system when the step counting service is first started*/
    private boolean hasRecord = false;
    /** The number of existing steps retrieved from the system */
    private int hasStepCount = 0;
    /** The number of previous steps */
    private int previousStepCount = 0;
    public StepService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("StepService—onCreate", "开启计步");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
                Log.i("StepService—子线程", "startStepDetector()");
            }
        }).start();
    }

    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        /** Get the sensor management class */
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        int versionCodes = Build.VERSION.SDK_INT;
        if (versionCodes >= 19) {
            /** The SDK version is greater than or equal to 19 pedometer sensors */
            addCountStepListener();
        } else {        /** Use the accelerometer if less than */
            addBasePedometerListener();
        }
    }

  /** Start the step counting sensor step counting */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            STEP_SENSOR_TYPE = Sensor.TYPE_STEP_COUNTER;
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("计步传感器类型", "Sensor.TYPE_STEP_COUNTER");
        } else if (detectorSensor != null) {
            STEP_SENSOR_TYPE = Sensor.TYPE_STEP_DETECTOR;
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("计步传感器类型", "Sensor.TYPE_STEP_DETECTOR");
        } else {
            Log.i("计步传感器类型", "使用加速度传感器计步");
            addBasePedometerListener();
        }
    }

  /** Start the accelerometer step meter */
    private void addBasePedometerListener() {
        sStepCount = new StepCount();
        sStepCount.setStep(nowStepCount);
        /** Get the acceleration sensor */
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(sStepCount.getStepSensorDetector(), sensor, SensorManager.SENSOR_DELAY_UI);
        sStepCount.initListener(new StepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                nowStepCount = steps; /** Get the current step count through the interface callback */
                updateNotification();    /** Update the step count notification */
            }
        });
    }

    /** Notifies the caller of the number of steps to update the data interaction */
    private void updateNotification() {
        if (mCallback != null) {
            Log.i("StepService", "数据更新");
            mCallback.updateUi(nowStepCount);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return lcBinder;
    }

    /**  Pedestrian sensor data change callback interface */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (STEP_SENSOR_TYPE == Sensor.TYPE_STEP_COUNTER) {
            /** Gets the number of temporary steps returned by the current sensor */
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                /** Gets the total number of steps since the APP was opened */
                int thisStepCount = tempStep - hasStepCount;
                /** The number of effective steps */
                int thisStep = thisStepCount - previousStepCount;
                /** the total number */
                nowStepCount += (thisStep);
                /** Record the total number of steps since the last time the APP was opened */
                previousStepCount = thisStepCount;
            }
        }

        else if (STEP_SENSOR_TYPE == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                nowStepCount++;
            }
        }
        updateNotification();

    }

   /** Stepmeter sensor precision change callback interface */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /** Bind the callback interface */
    public class LcBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }


    public void registerCallback(UpdateUiCallBack paramCallback) {
        this.mCallback = paramCallback;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /** Cancel the foreground process */
        stopForeground(true);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
