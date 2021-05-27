package com.hills.mcs_02.func_sportsShare;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StepSensorDetector implements SensorEventListener {

    /** store three axes of data */
    float[] oriValues = new float[3];
    final int VALUE_NUM = 4;
    /** The peak-trough difference used to store the calculation threshold */
    float[] tempValue = new float[VALUE_NUM];
    int tempCount = 0;
    /** Whether to raise the flag bit */
    boolean isDirectionUp = false;
    /** Continue to increase the number of times */
    int continueUpCount = 0;
    /** The number of times the wave crest has risen */
    int continueUpFormerCount = 0;
    /** The state of the previous point, up or down */
    boolean lastStatus = false;
    /** peak value */
    float peakOfWave = 0;
    /**  trough value */
    float valleyOfWave = 0;
    /** The time of the peak of the wave */
    long timeOfThisPeak = 0;
    /**  The time of the last wave peak */
    long timeOfLastPeak = 0;
    /** The current time */
    long timeOfNow = 0;
    /**The current time*/
    float gravityNew = 0;
    /** The value of the last sensor */
    float gravityOld = 0;
    /** Dynamic thresholds require dynamic data. This value is used for the threshold value of these dynamic data */
    final float INITIAL_VALUE = (float) 1.3;
    /** Initial threshold value */
    float threadValue = (float) 2.0;
    /**  Time difference between peaks and troughs */
    int timeInterval = 250;
    private StepCountListener mStepListeners;

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (int temp = 0; temp < 3; temp++) {
            oriValues[temp] = event.values[temp];
        }
        gravityNew = (float) Math.sqrt(oriValues[0] * oriValues[0]
                + oriValues[1] * oriValues[1] + oriValues[2] * oriValues[2]);
        detectorNewStep(gravityNew);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void initListener(StepCountListener listener) {
        this.mStepListeners = listener;
    }

    public void detectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (detectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();
                if (timeOfNow - timeOfLastPeak >= timeInterval
                        && (peakOfWave - valleyOfWave >= threadValue)) {
                    timeOfThisPeak = timeOfNow;
                    mStepListeners.countStep();
                }
                if (timeOfNow - timeOfLastPeak >= timeInterval
                        && (peakOfWave - valleyOfWave >= INITIAL_VALUE)) {
                    timeOfThisPeak = timeOfNow;
                    threadValue = peakValleyThread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }

    public boolean detectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

        if (!isDirectionUp && lastStatus
                && (continueUpFormerCount >= 2 || oldValue >= 20)) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    public float peakValleyThread(float value) {
        float tempThread = threadValue;
        if (tempCount < VALUE_NUM) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, VALUE_NUM);
            for (int temp = 1; temp < VALUE_NUM; temp++) {
                tempValue[temp - 1] = tempValue[temp];
            }
            tempValue[VALUE_NUM - 1] = value;
        }
        return tempThread;

    }

    public float averageValue(float value[], int num) {
        float ave = 0;
        for (int temp = 0; temp < num; temp++) {
            ave += value[temp];
        }
        ave = ave / VALUE_NUM;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        return ave;
    }

}
