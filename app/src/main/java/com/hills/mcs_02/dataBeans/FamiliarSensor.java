package com.hills.mcs_02.dataBeans;

public class FamiliarSensor {
    private Integer familiarSensorId;          /** the primary key */
    private Integer userId;                    /** user ID */
    private Integer taskId;                    /* task ID */

    private Float longitude;
    private Float latitude;
    private Float speed;

    private Float sensorType;

    private String sensorFile;   /** Sensor data file */

    public FamiliarSensor(Integer pFamiliarSensorId, Integer pUserId, Integer pTaskId, Float pLongitude, Float pLatitude, Float pSpeed, Float pSensorType, String pSensorFile) {
        familiarSensorId = pFamiliarSensorId;
        userId = pUserId;
        taskId = pTaskId;
        longitude = pLongitude;
        latitude = pLatitude;
        speed = pSpeed;
        sensorType = pSensorType;
        sensorFile = pSensorFile;
    }

    public void setFamiliarSensorId(Integer pFamiliarSensorId) {
        familiarSensorId = pFamiliarSensorId;
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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float pLatitude) {
        latitude = pLatitude;
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
                "familiar_sensorId=" + familiarSensorId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", longitude=" + longitude +
                ", latiude=" + latitude +
                ", speed=" + speed +
                ", sensorType=" + sensorType +
                ", sensorFile='" + sensorFile + '\'' +
                '}';
    }
}
