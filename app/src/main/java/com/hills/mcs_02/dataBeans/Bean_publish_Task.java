package com.hills.mcs_02.dataBeans;

class Bean_publish_Task {
    private String postTime;
    private String deadLine;
    private String postID;
    private String coin;
    private String Text;

    public Bean_publish_Task(String postTime, String deadLine, String postID, String coin, String text) {
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.postID = postID;
        this.coin = coin;
        Text = text;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getPostID() {
        return postID;
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

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public void setText(String text) {
        Text = text;
    }

    @Override
    public String toString() {
        return "Bean_publish_Task{" +
                "postTime='" + postTime + '\'' +
                ", deadLine='" + deadLine + '\'' +
                ", postID='" + postID + '\'' +
                ", coin='" + coin + '\'' +
                ", Text='" + Text + '\'' +
                '}';
    }
}
