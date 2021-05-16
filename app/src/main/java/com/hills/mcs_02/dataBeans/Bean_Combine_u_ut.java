package com.hills.mcs_02.dataBeans;

import java.io.File;

public class Bean_Combine_u_ut {
    private int userIcon;
    private File pic;
    private Combine_u_ut mCombine_u_ut;

    public Bean_Combine_u_ut(int userIcon, Combine_u_ut combine_u_ut) {
        this.userIcon = userIcon;
        this.pic = null;
        mCombine_u_ut = combine_u_ut;
    }

    public Bean_Combine_u_ut(int userIcon, File pic, Combine_u_ut combine_u_ut) {
        this.userIcon = userIcon;
        this.pic = pic;
        mCombine_u_ut = combine_u_ut;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public Combine_u_ut getCombine_u_ut() {
        return mCombine_u_ut;
    }

    public void setCombine_u_ut(Combine_u_ut combine_u_ut) {
        mCombine_u_ut = combine_u_ut;
    }

    public User getUser() {
        return mCombine_u_ut.getU();
    }

    public User_Task getUt() {
        return mCombine_u_ut.getUt();
    }

    public File getPic() {
        return pic;
    }

    public void setPic(File pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "Bean_Combine_u_ut{" +
                "userIcon=" + userIcon +
                ", pic=" + pic +
                ", mCombine_u_ut=" + mCombine_u_ut +
                '}';
    }
}
