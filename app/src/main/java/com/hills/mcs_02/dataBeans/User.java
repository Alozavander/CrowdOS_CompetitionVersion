package com.hills.mcs_02.dataBeans;

public class User {
    private Integer userId;
    private String username;
    private String password;
    private String realName;
    private int coin;

    public User() {
        super();
    }

    public User(Integer userId, String username, String password, String realName, int coin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.coin = coin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCoin() {
        return coin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + username + '\'' +
                ", passWord='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", coins=" + coin +
                '}';
    }
}
