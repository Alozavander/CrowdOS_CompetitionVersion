package com.hills.mcs_02.networkClasses;

public class Task_GetDataParsw extends GetDataParse {
    private String id;
    private String taskName;
    private String postTime;
    private String deadLine;
    private String postID;
    private String coin;
    private String text;
    private String tag;

    public Task_GetDataParsw() {
        super();
    }

    public String getTaskName() {
        return taskName;
    }

    public String getId() {
        return id;
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
        return text;
    }

    public String getTag() {
        return tag;
    }

    public String getPostName(){
        String postName = "";

        return postName;
    }

}
