package com.hills.mcs_02.func_sportsShare.beans;

public class func_sportShare_stepCounter extends FuncSportShareBaseBean {
    private final int STEP_COUNTER_VIEW = 1000;
    private String userIconMine;
    private String stepCount;

    public String getUserIconMine() {
        return userIconMine;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public int getViewType() {
        return STEP_COUNTER_VIEW;
    }
}
