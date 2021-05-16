package com.hills.mcs_02.func_sportsShare.beans;

public class func_sportShare_stepShareListBean extends func_sportShare_BaseBean {
    private String userIcon_path;
    private String userName;
    private String upLoadTime;
    private String stepAmout;

    private final int SPORT_SHARE_VIEW = 1001;

    public func_sportShare_stepShareListBean(String userIcon_path, String userName, String upLoadTime, String stepAmout) {
        this.userIcon_path = userIcon_path;
        this.userName = userName;
        this.upLoadTime = upLoadTime;
        this.stepAmout = stepAmout;
    }

    public String getUserIcon_path() {
        return userIcon_path;
    }

    public String getUserName() {
        return userName;
    }

    public String getUpLoadTime() {
        return upLoadTime;
    }

    public String getStepAmout() {
        return stepAmout;
    }

    public void setUserIcon_path(String userIcon_path) {
        this.userIcon_path = userIcon_path;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUpLoadTime(String upLoadTime) {
        this.upLoadTime = upLoadTime;
    }

    public void setStepAmout(String stepAmout) {
        this.stepAmout = stepAmout;
    }

    @Override
    public int getViewType() {
        return SPORT_SHARE_VIEW;
    }
}
