package com.hills.mcs_02.dataBeans;

public class LonAndLat {

    private Integer taskId;
    private double Lon;  /** longitude */
    private double Lat;  /**  latitude */

    public LonAndLat(Integer taskId, double Lon, double Lat){
        this.taskId = taskId;
        this.Lon = Lon;
        this.Lat = Lat;
    }

    public Integer getTaskId(){
        return taskId;
    }

    public double getLon(){
        return Lon;
    }

    public double getLat(){
        return Lat;
    }

    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }

    public void setLat(double Lat){
        this.Lat = Lat;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", Longitude=" + Lon +
                ", Latitude=" + Lat +
                "}";
    }
}
