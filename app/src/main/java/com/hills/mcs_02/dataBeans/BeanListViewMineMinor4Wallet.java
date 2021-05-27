package com.hills.mcs_02.dataBeans;

public class BeanListViewMineMinor4Wallet {
    private int userIcon;
    private String title;
    private String mount;
    private String time;

    public BeanListViewMineMinor4Wallet(int userIcon, String title, String mount, String time) {
        this.userIcon = userIcon;
        this.title = title;
        this.mount = mount;
        this.time = time;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public String getTitle() {
        return title;
    }

    public String getMount() {
        return mount;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
