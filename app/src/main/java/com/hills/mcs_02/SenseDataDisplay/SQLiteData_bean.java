package com.hills.mcs_02.SenseDataDisplay;

public class SQLiteData_bean {
    private int senseData_id;
    private int sensorType;
    private String senseTime;
    private String senseValue_1;
    private String senseValue_2;
    private String senseValue_3;

    public int getSenseData_id() {
        return senseData_id;
    }

    public void setSenseData_id(int pSenseData_id) {
        senseData_id = pSenseData_id;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int pSensorType) {
        sensorType = pSensorType;
    }

    public String getSenseTime() {
        return senseTime;
    }

    public void setSenseTime(String pSenseTime) {
        senseTime = pSenseTime;
    }

    public String getSenseValue_1() {
        return senseValue_1;
    }

    public void setSenseValue_1(String pSenseValue_1) {
        senseValue_1 = pSenseValue_1;
    }

    public String getSenseValue_2() {
        return senseValue_2;
    }

    public void setSenseValue_2(String pSenseValue_2) {
        senseValue_2 = pSenseValue_2;
    }

    public String getSenseValue_3() {
        return senseValue_3;
    }

    public void setSenseValue_3(String pSenseValue_3) {
        senseValue_3 = pSenseValue_3;
    }
}
