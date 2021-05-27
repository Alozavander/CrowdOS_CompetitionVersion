package com.hills.mcs_02.dataBeans;


public class BeanListViewMine {
    private int icon;
    private String title;

    public BeanListViewMine(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
