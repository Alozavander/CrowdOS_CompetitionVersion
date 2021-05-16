package com.hills.mcs_02.dataBeans;

public class User {
    private Integer userId;
    private String userName;
    private String passWord;
    private String realName;
    private int coins;

    public User() {
        super();
    }

    public User(Integer userId, String userName, String passWord, String realName, int coins) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.realName = realName;
        this.coins = coins;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", realName='" + realName + '\'' +
                ", coins=" + coins +
                '}';
    }
}
