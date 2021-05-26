package com.hills.mcs_02.dataBeans;

public class UserTask {
    private Integer userTaskId;                //用户-任务ID
    private Integer userId;                      //用户ID
    private Integer taskId;                      //任务ID
    private Integer userTaskStatus;             //用户-任务执行状态  0&1
    private String content;                     //内容
    private String image;                       //图片信息   （暂时并不需要使用）
    private Integer type;                        //上传的类型信息；0为纯文字；1为图片；2为音频；3为视频

    public UserTask() {
        super();
    }

    public UserTask(Integer userTaskId, Integer userId, Integer taskId,
                     int userTaskStatus, String content, String image, Integer type) {
        this.userTaskId = userTaskId;
        this.userId = userId;
        this.taskId = taskId;
        this.userTaskStatus = userTaskStatus;
        this.content = content;
        this.image = image;
        this.type = type;
    }

    public Integer getUserTaskId() {
        return userTaskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public int getUserTaskStatus() {
        return userTaskStatus;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public void setUserTaskId(Integer userTaskId) {
        this.userTaskId = userTaskId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setUserTaskStatus(Integer userTaskStatus) {
        this.userTaskStatus = userTaskStatus;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User_Task{" +
                "user_taskId=" + userTaskId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", user_taskStatus=" + userTaskStatus +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
