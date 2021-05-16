package com.hills.mcs_02.dataBeans;

public class Lon_Lat {

    private Integer taskId;  //任务ID
    private double Lon;  //经度
    private double Lat;  //纬度

    public Lon_Lat(Integer taskId, double Lon, double Lat){
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

    public void setLon(double Lon){
        this.Lon = Lon;
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
