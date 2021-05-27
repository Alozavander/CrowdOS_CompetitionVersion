package com.hills.mcs_02.dataBeans;

class BeanPublishTask {
    private String postTime;
    private String deadline;
    private String postId;
    private String coin;
    private String Text;

    public BeanPublishTask(String postTime, String deadline, String postId, String coin, String text) {
        this.postTime = postTime;
        this.deadline = deadline;
        this.postId = postId;
        this.coin = coin;
        Text = text;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getPostId() {
        return postId;
    }

    public String getCoin() {
        return coin;
    }

    public String getText() {
        return Text;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setText(String text) {
        Text = text;
    }

    @Override
    public String toString() {
        return "Bean_publish_Task{" +
                "postTime='" + postTime + '\'' +
                ", deadLine='" + deadline + '\'' +
                ", postID='" + postId + '\'' +
                ", coin='" + coin + '\'' +
                ", Text='" + Text + '\'' +
                '}';
    }
}
