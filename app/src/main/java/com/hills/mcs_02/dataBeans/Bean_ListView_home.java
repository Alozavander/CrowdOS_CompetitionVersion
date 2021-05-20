package com.hills.mcs_02.dataBeans;

import java.text.SimpleDateFormat;

//首页任务列表单项类
public class Bean_ListView_home {
    private int userIcon;
    //private int categoryIcon;
    //private int starIcon;
    private int photo;
    private String userID;
    private String describe;
    private Task task;

    public Bean_ListView_home(){
        super();
    }


    public Bean_ListView_home(int userIcon, String userID, int photo, String describe, Task _task){
        this.userIcon = userIcon;
        this.userID = userID;
        this.photo = photo;
        this.describe = describe;
        task = _task;
    }

    public int getUserIcon() {
        return userIcon;
    }

    /*public int getCategoryIcon() {
        return categoryIcon;
    }

    public int getStarIcon() {
        return starIcon;
    }*/

    public int getPhoto() {
        return photo;
    }

    public String getUserID() {
        return userID;
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
        return task.getDescribe_task();
    }

    public String getCoinsCount() {
        return task.getCoin() + "";
    }

    /*public String getTaskCount() {
        return taskCount;
    }*/

    public Task getTask() {
        return task;
    }

    public String getDeadline() {
        if (task.getPostTime()==null){
            return "";
        }
        return new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine());
    }

    public Integer getTaskCount(){
        return task.getTotalNum();
    }
}
