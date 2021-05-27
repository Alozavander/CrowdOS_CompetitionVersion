package com.hills.mcs_02.dataBeans;

import java.util.Date;

//当前Bean主要直接对接应用的直接使用
public class Task {
    private Integer taskId;
    private String taskName;
    private Date postTime;               /** Release date */
    private Date deadLine;
    private Integer userId;
    private String username;
    private Float coin;                  /** incentives */
    private String describeTask;         /**  Task description */
    private Integer totalNum;            /** The total number of people working on this mission */
    private Integer taskStatus;          /** The execution status of the task */
    private Integer taskKind;            /**  Task type: 0- Public Safety, 1- Environmental Research, 2- People's Daily, 3- Business Applications, 4- Other*/
    private String sensorTypes;         /** The sensing task requires the specified sensor */
    private float latitude;
    private float longitude;

    public Task() {
        super();
    }

    public Task(Integer taskId, String taskName, Date postTime, Date deadLine, Integer userId, String username, Float coin, String describeTask, Integer totalNum, Integer taskStatus, Integer taskKind) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.userId = userId;
        this.username = username;
        this.coin = coin;
        this.describeTask = describeTask;
        this.totalNum = totalNum;
        this.taskStatus = taskStatus;
        this.taskKind = taskKind;
        sensorTypes = "-1";    /** On behalf of the GPS -1 */
        latitude = -9999;      /** -9999 indicates an error or unassigned value */
        longitude = -9999;      /** -9999 indicates an error or unassigned value */
    }

    public Task(Integer taskId, String taskName, Date postTime, Date deadLine, Integer userId, String username, Float coin, String describeTask, Integer totalNum, Integer taskStatus, Integer taskKind,String sensorTypes) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.userId = userId;
        this.username = username;
        this.coin = coin;
        this.describeTask = describeTask;
        this.totalNum = totalNum;
        this.taskStatus = taskStatus;
        this.taskKind = taskKind;
        this.sensorTypes = sensorTypes;
        latitude = -9999;               /** -9999 indicates an error or unassigned value */
        longitude = -9999;
    }

    public Task(Integer pTaskId, String pTaskName, Date pPostTime, Date pDeadline, Integer pUserId, String pUsername, Float pCoin, String pDescribeTask, Integer pTotalNum, Integer pTaskStatus, Integer pTaskKind, String pSensorTypes, float pLatitude, float pLongitude) {
        taskId = pTaskId;
        taskName = pTaskName;
        postTime = pPostTime;
        deadLine = pDeadline;
        userId = pUserId;
        username = pUsername;
        coin = pCoin;
        describeTask = pDescribeTask;
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

    public Date getDeadline() {
        return deadLine;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Float getCoin() {
        return coin;
    }

    public String getDescribeTask() {
        return describeTask;
    }

    public Integer getTotalNum() {
        return totalNum;
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

    public void setDeadline(Date deadline) {
        this.deadLine = deadline;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescribeTask(String describeTask) {
        this.describeTask = describeTask;
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
                ", userName='" + username + '\'' +
                ", coin=" + coin +
                ", describe_task='" + describeTask + '\'' +
                ", totalNum=" + totalNum +
                ", taskStatus=" + taskStatus +
                ", taskKind=" + taskKind +
                ", sensorTypes='" + sensorTypes + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
