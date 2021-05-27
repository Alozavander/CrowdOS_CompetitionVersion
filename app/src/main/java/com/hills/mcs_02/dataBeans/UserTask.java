package com.hills.mcs_02.dataBeans;

public class UserTask {
    private Integer userTaskId;
    private Integer userId;                    /** User-task ID */
    private Integer taskId;
    private Integer userTaskStatus;             /** User-task execution status */
    private String content;                    /** Task Completed Content */
    private String image;                       /** Mission completed picture information */
    private Integer type;                        /** Upload type information; 0 is plain text; 1 is the picture; 2 is audio; 3 for the video */


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
