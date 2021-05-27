package com.hills.mcs_02.func_sportsShare.beans;

public class FuncSportShareStepShareListBean extends FuncSportShareBaseBean {
    private String userIconPath;
    private String username;
    private String uploadTime;
    private String stepAmount;

    private final int SPORT_SHARE_VIEW = 1001;

    public FuncSportShareStepShareListBean(String userIconPath, String username, String uploadTime, String stepAmount) {
        this.userIconPath = userIconPath;
        this.username = username;
        this.uploadTime = uploadTime;
        this.stepAmount = stepAmount;
    }

    public String getUserIconPath() {
        return userIconPath;
    }

    public String getUsername() {
        return username;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getStepAmount() {
        return stepAmount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setStepAmount(String stepAmount) {
        this.stepAmount = stepAmount;
    }

    @Override
    public int getViewType() {
        return SPORT_SHARE_VIEW;
    }
}
