package com.hills.mcs_02.dataBeans;


//发布页任务模板单项类
public class Bean_ListView_publish {
    private String title;
    private String sensors;

    public Bean_ListView_publish(String title, String sensors) {
        this.title = title;
        this.sensors = sensors;
    }

    public String getTitle() {
        return title;
    }

    public String getSensors() {
        return sensors;
    }
}
