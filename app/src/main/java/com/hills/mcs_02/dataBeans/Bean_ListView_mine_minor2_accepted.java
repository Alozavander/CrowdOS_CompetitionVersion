package com.hills.mcs_02.dataBeans;

public class Bean_ListView_mine_minor2_accepted {
    private int pic;
    private String taskID;
    private String taksState;
    private String describe;
    private String taskContent;
    private String coinsCount;
    private String taskCount;
    private String taskReward;

    public Bean_ListView_mine_minor2_accepted() {
    }

    public Bean_ListView_mine_minor2_accepted(int pic, String taskID, String taksState, String describe, String taskContent, String coinsCount, String taskCount, String taskReward) {
        this.pic = pic;
        this.taskID = taskID;
        this.taksState = taksState;
        this.describe = describe;
        this.taskContent = taskContent;
        this.coinsCount = coinsCount;
        this.taskCount = taskCount;
        this.taskReward = taskReward;
    }

    public int getPic() {
        return pic;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaksState() {
        return taksState;
    }

    public String getDescribe() {
        return describe;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public String getCoinsCount() {
        return coinsCount;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public String getTaskReward() {
        return taskReward;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setTaksState(String taksState) {
        this.taksState = taksState;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public void setCoinsCount(String coinsCount) {
        this.coinsCount = coinsCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public void setTaskReward(String taskReward) {
        this.taskReward = taskReward;
    }
}
