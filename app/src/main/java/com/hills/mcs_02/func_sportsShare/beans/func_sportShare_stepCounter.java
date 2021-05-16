package com.hills.mcs_02.func_sportsShare.beans;

public class func_sportShare_stepCounter extends func_sportShare_BaseBean {
    private final int STEP_COUNTER_VIEW = 1000;
    private String userIcon_mine;
    private String stepCount;

    public func_sportShare_stepCounter() {
    }

    public String getUserIcon_mine() {
        return userIcon_mine;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setUserIcon_mine(String userIcon_mine) {
        this.userIcon_mine = userIcon_mine;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public int getViewType() {
        return STEP_COUNTER_VIEW;
    }
}
