package com.hills.mcs_02.dataBeans;


import java.util.Date;

//当前Bean主要直接对接应用的直接使用
public class Task {
    private Integer taskId;               //任务ID
    private String taskName;              //任务名称
    private Date postTime;                //发布日期
    private Date deadLine;                //截止日期
    private Integer userId;               //任务发布者ID
    private String userName;              //任务发布者的名字
    private Float coin;                   //激励金
    private String describe_task;         //任务描述
    private Integer totalNum;             //该任务的执行总人数
    private Integer taskStatus;           //该任务的执行状态
    private Integer taskKind;             //任务的类型，通过整数型和对应的约定映射表来规定任务类型,0-公共安全，1-环境调研，2-民生日常，3-商业应用，4-其他
    //todo:integer[] -> string
    private String sensorTypes;        //感知任务需要指明的传感器
    private float latitude;              //todo:waiting for finishing it.
    private float longitude;

    public Task() {
        super();
    }

    public Task(Integer taskId, String taskName, Date postTime, Date deadLine, Integer userId, String userName, Float coin, String describe_task, Integer totalNum, Integer taskStatus, Integer taskKind) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.userId = userId;
        this.userName = userName;
        this.coin = coin;
        this.describe_task = describe_task;
        this.totalNum = totalNum;
        this.taskStatus = taskStatus;
        this.taskKind = taskKind;
        sensorTypes = "-1";  //-1代表GPS
        latitude = -9999;                //-9999表示错误或者未赋值
        longitude = -9999;
    }

    public Task(Integer taskId, String taskName, Date postTime, Date deadLine, Integer userId, String userName, Float coin, String describe_task, Integer totalNum, Integer taskStatus, Integer taskKind,String sensorTypes) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.userId = userId;
        this.userName = userName;
        this.coin = coin;
        this.describe_task = describe_task;
        this.totalNum = totalNum;
        this.taskStatus = taskStatus;
        this.taskKind = taskKind;
        this.sensorTypes = sensorTypes;
        latitude = -9999;                //-9999表示错误或者未赋值
        longitude = -9999;
    }

    public Task(Integer pTaskId, String pTaskName, Date pPostTime, Date pDeadLine, Integer pUserId, String pUserName, Float pCoin, String pDescribe_task, Integer pTotalNum, Integer pTaskStatus, Integer pTaskKind, String pSensorTypes, float pLatitude, float pLongitude) {
        taskId = pTaskId;
        taskName = pTaskName;
        postTime = pPostTime;
        deadLine = pDeadLine;
        userId = pUserId;
        userName = pUserName;
        coin = pCoin;
        describe_task = pDescribe_task;
        totalNum = pTotalNum;
        taskStatus = pTaskStatus;
        taskKind = pTaskKind;
        sensorTypes = pSensorTypes;
        latitude = pLatitude;
        longitude = pLongitude;
    }

    public Integer getTaskId() {
        return this.taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Date getPostTime() {
        return postTime;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Float getCoin() {
        return coin;
    }

    public String getDescribe_task() {
        return describe_task;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public Integer getTaskKind() {
        return taskKind;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCoin(Float coin) {
        this.coin = coin;
    }

    public void setDescribe_task(String describe_task) {
        this.describe_task = describe_task;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskKind(Integer taskKind) {
        this.taskKind = taskKind;
    }

    public String getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(String pSensorTypes) {
        sensorTypes = pSensorTypes;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float pLatitude) {
        latitude = pLatitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float pLongitude) {
        longitude = pLongitude;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", postTime=" + postTime +
                ", deadLine=" + deadLine +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", coin=" + coin +
                ", describe_task='" + describe_task + '\'' +
                ", totalNum=" + totalNum +
                ", taskStatus=" + taskStatus +
                ", taskKind=" + taskKind +
                ", sensorTypes='" + sensorTypes + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
