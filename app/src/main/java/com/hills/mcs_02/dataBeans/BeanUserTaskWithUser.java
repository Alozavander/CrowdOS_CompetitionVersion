package com.hills.mcs_02.dataBeans;

import java.io.File;

public class BeanUserTaskWithUser {
    private int userIcon;
    private File pic;
    private UserTaskWithUser mUserTaskWithUser;

    public BeanUserTaskWithUser(int userIcon, UserTaskWithUser userTaskWithUser) {
        this.userIcon = userIcon;
        this.pic = null;
        mUserTaskWithUser = userTaskWithUser;
    }

    public BeanUserTaskWithUser(int userIcon, File pic, UserTaskWithUser userTaskWithUser) {
        this.userIcon = userIcon;
        this.pic = pic;
        mUserTaskWithUser = userTaskWithUser;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserTaskWithUser(UserTaskWithUser userTaskWithUser) {
        mUserTaskWithUser = userTaskWithUser;
    }

    public User getUser() {
        return mUserTaskWithUser.getUser();
    }

    public UserTask getUserTask() {
        return mUserTaskWithUser.getUserTask();
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
                ", mCombine_u_ut=" + mUserTaskWithUser +
                '}';
    }
}
