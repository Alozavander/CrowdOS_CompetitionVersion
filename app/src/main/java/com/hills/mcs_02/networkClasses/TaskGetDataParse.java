package com.hills.mcs_02.networkClasses;

public class TaskGetDataParse extends GetDataParse {
    private String id;
    private String taskName;
    private String postTime;
    private String deadLine;
    private String postId;
    private String coin;
    private String text;
    private String tag;

    public TaskGetDataParse() {
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

    public String getDeadline() {
        return deadLine;
    }

    public String getPostId() {
        return postId;
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
