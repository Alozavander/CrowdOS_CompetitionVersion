package com.hills.mcs_02.func_sportsShare;

import android.util.Log;

public class StepCount implements StepCountListener {
    private int mCount; /** The number of current steps */
    private int count;  /** Cache the number of steps. If the number of steps is less than 10 within 3 seconds, the count is not counted */
    private long timeOfLastPeak = 0; /** If the number of steps is less than 10 within 3 seconds, the count will not be counted */
    private long timeOfThisPeak = 0; /** // Time now does not count if the number of time steps is less than 10 within 3 seconds */
    private StepValuePassListener stepValuePassListener; /** The interface is used to pass step changes */
    private StepSensorDetector stepSensorDetector; /** sensor SensorEventListener subclass instance */

    public StepCount() {
        stepSensorDetector = new StepSensorDetector();
        stepSensorDetector.initListener(this);
    }

    @Override
    public void countStep() {
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        Log.i("countStep","传感器数据刷新回调");
        if (this.timeOfThisPeak - this.timeOfLastPeak <= 3000L) {
            if (this.count < 9) {
                this.count++;
            } else if (this.count == 9) {
                this.count++;
                this.mCount += this.count;
                notifyListener();
            } else {
                this.mCount++;
                notifyListener();
            }
        } else {
            /** timeout */
            this.count = 1;
        }

    }
    public void setStep(int stepCount){
        this.mCount = stepCount; /** Receive the current number of steps passed by the upper call */
        this.count = 0;
        timeOfLastPeak = 0;
        timeOfThisPeak = 0;
        notifyListener();
    }

    public StepSensorDetector getStepSensorDetector(){
        return stepSensorDetector;
    }

    public void notifyListener(){
        if(this.stepValuePassListener != null){
            Log.i("countStep","数据更新");
            this.stepValuePassListener.stepChanged(this.mCount); /** The current step count is passed through the interface to the caller */
        }
    }
    public  void initListener(StepValuePassListener listener){
        this.stepValuePassListener = listener;
    }
}
