package com.hills.mcs_02.dataBeans;

import java.text.SimpleDateFormat;

public class Bean_ListView_remind {
    private int userIcon;
    private int picture;
    private String kind;
    private Task task;

    public Bean_ListView_remind() {

    }

    public Bean_ListView_remind(int userIcon, int picture, String kind, Task task) {
        this.userIcon = userIcon;
        this.picture = picture;
        this.kind = kind;
        this.task = task;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public int getPicture() {
        return picture;
    }

    public String getKind() {
        return kind;
    }

    public Task getTask() {
        return task;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getPsotTime() {
        return new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine());
    }

    public String getDeadline() {
        return new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine());
    }

    @Override
    public String toString() {
        return "Bean_ListView_remind{" +
                "userIcon=" + userIcon +
                ", picture=" + picture +
                ", kind='" + kind + '\'' +
                ", task=" + task +
                '}';
    }
}
