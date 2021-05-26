package com.hills.mcs_02.dataBeans;

public class BeanListViewMineMinor2Accepted {
    private int pic;
    private String taskID;
    private String taskState;
    private String describe;
    private String taskContent;
    private String coinsCount;
    private String taskCount;
    private String taskReward;

    public BeanListViewMineMinor2Accepted() {
    }

    public BeanListViewMineMinor2Accepted(int pic, String taskId, String taskState, String describe, String taskContent, String coinsCount, String taskCount, String taskReward) {
        this.pic = pic;
        this.taskID = taskId;
        this.taskState = taskState;
        this.describe = describe;
        this.taskContent = taskContent;
        this.coinsCount = coinsCount;
        this.taskCount = taskCount;
        this.taskReward = taskReward;
    }

    public int getPic() {
        return pic;
    }

    public String getTaskId() {
        return taskID;
    }

    public String getTaskState() {
        return taskState;
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

    public void setTaskId(String taskID) {
        this.taskID = taskID;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
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
