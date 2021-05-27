package com.hills.mcs_02.dataBeans;

import java.util.Date;

public class SensorDetail {
    private Integer sensorDetailId;
    private Integer userId;
    private Integer taskId;
    private Date onlineTime;        /** Data upload date */
    private String fileName;
    private String sensorType;
    private String acquisitionTime;  /** Data collection time */
    private String sensorValue;     /** The data value may have multiple data in one sensor, and the "-" is used to segment multiple data in the uploaded file */
    private String temp1;           /** Test ： reserve data */
    private String temp2;              /** Test ： reserve data */


    public SensorDetail(Integer sensorDetailId, Integer userId, Integer taskId, Date onlineTime, String fileName, String sensorType, String acquisitionTime, String sensorValue, String temp1, String temp2) {
        this.sensorDetailId = sensorDetailId;
        this.userId = userId;
        this.taskId = taskId;
        this.onlineTime = onlineTime;
        this.fileName = fileName;
        this.sensorType = sensorType;
        this.acquisitionTime = acquisitionTime;
        this.sensorValue = sensorValue;
        this.temp1 = temp1;
        this.temp2 = temp2;
    }

    public Integer getSensorDetailId() {
        return sensorDetailId;
    }

    public void setSensorDetailId(Integer sensorDetailId) {
        this.sensorDetailId = sensorDetailId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setAcquisitionTime(String acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    @Override
    public String toString() {
        return "Sensor_Detail{" +
                "sensor_detailId=" + sensorDetailId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", onlineTime=" + onlineTime +
                ", fileName='" + fileName + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", acquisitionTime='" + acquisitionTime + '\'' +
                ", sensorValue='" + sensorValue + '\'' +
                ", temp1='" + temp1 + '\'' +
                ", temp2='" + temp2 + '\'' +
                '}';
    }
}

