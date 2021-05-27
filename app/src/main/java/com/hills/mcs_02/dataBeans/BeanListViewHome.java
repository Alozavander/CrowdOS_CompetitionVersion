package com.hills.mcs_02.dataBeans;

import java.text.SimpleDateFormat;

/** Home task column form item class */
public class BeanListViewHome {
    private int userIcon;
    private int photo;
    private String userId;
    private String describe;
    private Task task;

    public BeanListViewHome(){
        super();
    }


    public BeanListViewHome(int userIcon, String userId, int photo, String describe, Task task){
        this.userIcon = userIcon;
        this.userId = userId;
        this.photo = photo;
        this.describe = describe;
        this.task = task;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public int getPhoto() {
        return photo;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostTime() {
        if (task.getPostTime()==null){
            return "";
        }
        return new SimpleDateFormat("yyyy.MM.dd").format(task.getPostTime());
    }

    public String getDescribe() {
        return describe;
    }

    public String getTaskContent() {
        return task.getDescribeTask();
    }

    public String getCoinsCount() {
        return task.getCoin() + "";
    }

    public Task getTask() {
        return task;
    }

    public String getDeadline() {
        if (task.getPostTime()==null){
            return "";
        }
        return new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadline());
    }

    public Integer getTaskCount(){
        return task.getTotalNum();
    }
}
