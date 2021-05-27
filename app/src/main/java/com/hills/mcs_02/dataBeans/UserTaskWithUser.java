package com.hills.mcs_02.dataBeans;

public class UserTaskWithUser {
    private User user;
    private UserTask userTask;

    public UserTaskWithUser(User user, UserTask userTask) {
        this.user = user;
        this.userTask = userTask;
    }

    public User getUser() {
        return user;
    }

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Combine_u_ut{" +
                "u=" + user +
                ", ut=" + userTask +
                '}';
    }
}
