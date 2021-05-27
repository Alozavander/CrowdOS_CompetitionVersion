package com.hills.mcs_02.func_foodShare.beans;

public class FuncFoodShareFoodShareListBean {
    private String userIconPath;
    private String username;
    private String foodDescription;
    private String foodImagePath;
    private String publishTime;

    public FuncFoodShareFoodShareListBean(String userIconPath, String username, String foodDescription, String foodImagePath, String publishTime) {
        this.userIconPath = userIconPath;
        this.username = username;
        this.foodDescription = foodDescription;
        this.foodImagePath = foodImagePath;
        this.publishTime = publishTime;
    }

    public String getUserIconPath() {
        return userIconPath;
    }

    public String getUsername() {
        return username;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodImagePath() {
        return foodImagePath;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "func_foodShare_foodShareListBean{" +
                "userIcon_path='" + userIconPath + '\'' +
                ", userName='" + username + '\'' +
                ", foodDescription='" + foodDescription + '\'' +
                ", foodImage_path='" + foodImagePath + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
