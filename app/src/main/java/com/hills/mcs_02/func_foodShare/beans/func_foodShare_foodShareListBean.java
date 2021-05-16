package com.hills.mcs_02.func_foodShare.beans;

public class func_foodShare_foodShareListBean {
    private String userIcon_path;
    private String userName;
    private String foodDescription;
    private String foodImage_path;
    private String publishTime;

    public func_foodShare_foodShareListBean(String userIcon_path, String userName, String foodDescription, String foodImage_path, String publishTime) {
        this.userIcon_path = userIcon_path;
        this.userName = userName;
        this.foodDescription = foodDescription;
        this.foodImage_path = foodImage_path;
        this.publishTime = publishTime;
    }

    public String getUserIcon_path() {
        return userIcon_path;
    }

    public String getUserName() {
        return userName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodImage_path() {
        return foodImage_path;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setUserIcon_path(String userIcon_path) {
        this.userIcon_path = userIcon_path;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public void setFoodImage_path(String foodImage_path) {
        this.foodImage_path = foodImage_path;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "func_foodShare_foodShareListBean{" +
                "userIcon_path='" + userIcon_path + '\'' +
                ", userName='" + userName + '\'' +
                ", foodDescription='" + foodDescription + '\'' +
                ", foodImage_path='" + foodImage_path + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
