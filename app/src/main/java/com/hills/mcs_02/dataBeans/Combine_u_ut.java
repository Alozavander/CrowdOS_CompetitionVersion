package com.hills.mcs_02.dataBeans;

public class Combine_u_ut {
    private User u;
    private User_Task ut;

    public Combine_u_ut(User u, User_Task ut) {
        this.u = u;
        this.ut = ut;
    }

    public User getU() {
        return u;
    }

    public User_Task getUt() {
        return ut;
    }

    public void setU(User u) {
        this.u = u;
    }

    public void setUt(User_Task ut) {
        this.ut = ut;
    }

    @Override
    public String toString() {
        return "Combine_u_ut{" +
                "u=" + u +
                ", ut=" + ut +
                '}';
    }
}
