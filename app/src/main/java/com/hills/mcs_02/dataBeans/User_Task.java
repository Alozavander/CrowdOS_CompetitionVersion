package com.hills.mcs_02.dataBeans;

public class User_Task {
    private Integer user_taskId;                //用户-任务ID
    private Integer userId;                      //用户ID
    private Integer taskId;                      //任务ID
    private Integer user_taskStatus;             //用户-任务执行状态  0&1
    private String content;                     //内容
    private String image;                       //图片信息   （暂时并不需要使用）
    private Integer type;                        //上传的类型信息；0为纯文字；1为图片；2为音频；3为视频

    public User_Task() {
        super();
    }

    public User_Task(Integer user_taskId, Integer userId, Integer taskId,
                     int user_taskStatus, String content, String image, Integer type) {
        this.user_taskId = user_taskId;
        this.userId = userId;
        this.taskId = taskId;
        this.user_taskStatus = user_taskStatus;
        this.content = content;
        this.image = image;
        this.type = type;
    }

    public Integer getUser_taskId() {
        return user_taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public int getUser_taskStatus() {
        return user_taskStatus;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public void setUser_taskId(Integer user_taskId) {
        this.user_taskId = user_taskId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setUser_taskStatus(Integer user_taskStatus) {
        this.user_taskStatus = user_taskStatus;
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
                "user_taskId=" + user_taskId +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", user_taskStatus=" + user_taskStatus +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
