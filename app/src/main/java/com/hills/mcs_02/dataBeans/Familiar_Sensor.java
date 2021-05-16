package com.hills.mcs_02.dataBeans;

public class Familiar_Sensor {
    private Integer familiar_sensorId;          //主键
    private Integer userId;                    //用户ID
    private Integer taskId;                    //任务ID

    private Float longitude;  //经度
    private Float latiude;  //维度
    private Float speed; //速度

    private Float sensorType;  //类型

    private String sensorFile;   //位置存储

    public Familiar_Sensor(Integer pFamiliar_sensorId, Integer pUserId, Integer pTaskId, Float pLongitude, Float pLatiude, Float pSpeed, Float pSensorType, String pSensorFile) {
        familiar_sensorId = pFamiliar_sensorId;
        userId = pUserId;
        taskId = pTaskId;
        longitude = pLongitude;
        latiude = pLatiude;
        speed = pSpeed;
        sensorType = pSensorType;
        sensorFile = pSensorFile;
    }

    public Integer getFamiliar_sensorId() {
        return familiar_sensorId;
    }

    public void setFamiliar_sensorId(Integer pFamiliar_sensorId) {
        familiar_sensorId = pFamiliar_sensorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer pUserId) {
        userId = pUserId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer pTaskId) {
        taskId = pTaskId;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float pLongitude) {
        longitude = pLongitude;
    }

    public Float getLatiude() {
        return latiude;
    }

    public void setLatiude(Float pLatiude) {
        latiude = pLatiude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float pSpeed) {
        speed = pSpeed;
    }

    public Float getSensorType() {
        return sensorType;
    }

    public void setSensorType(Float pSensorType) {
        sensorType = pSensorType;
    }

    public String getSensorFile() {
        return sensorFile;
    }

    public void setSensorFile(String pSensorFile) {
        sensorFile = pSensorFile;
    }

    @Override
    public String toString() {
        return "Familiar_Sensor{" +
                "familiar_sensorId=" + familiar_sensorId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", longitude=" + longitude +
                ", latiude=" + latiude +
                ", speed=" + speed +
                ", sensorType=" + sensorType +
                ", sensorFile='" + sensorFile + '\'' +
                '}';
    }
}
