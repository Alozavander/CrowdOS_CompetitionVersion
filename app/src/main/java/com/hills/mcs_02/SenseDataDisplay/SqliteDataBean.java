package com.hills.mcs_02.SenseDataDisplay;

public class SqliteDataBean {
    private int senseDataId;
    private int sensorType;
    private String senseTime;
    private String senseValue1;
    private String senseValue2;
    private String senseValue3;

    public int getSenseDataId() {
        return senseDataId;
    }

    public void setSenseDataId(int pSenseDataId) {
        senseDataId = pSenseDataId;
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

    public String getSenseValue1() {
        return senseValue1;
    }

    public void setSenseValue1(String pSenseValue1) {
        senseValue1 = pSenseValue1;
    }

    public String getSenseValue2() {
        return senseValue2;
    }

    public void setSenseValue2(String pSenseValue2) {
        senseValue2 = pSenseValue2;
    }

    public String getSenseValue3() {
        return senseValue3;
    }

    public void setSenseValue3(String pSenseValue3) {
        senseValue3 = pSenseValue3;
    }
}
