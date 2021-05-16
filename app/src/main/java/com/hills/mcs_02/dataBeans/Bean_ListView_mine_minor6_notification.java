package com.hills.mcs_02.dataBeans;

public class Bean_ListView_mine_minor6_notification {
    private int icon;
    private String ID;
    private String time;
    private String content;

    public Bean_ListView_mine_minor6_notification(int icon, String ID, String time, String content) {
        this.icon = icon;
        this.ID = ID;
        this.time = time;
        this.content = content;
    }

    public int getIcon() {
        return icon;
    }

    public String getID() {
        return ID;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
