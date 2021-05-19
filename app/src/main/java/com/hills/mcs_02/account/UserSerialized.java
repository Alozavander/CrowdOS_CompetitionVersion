package com.hills.mcs_02.account;

import com.hills.mcs_02.dataBeans.User;

import java.io.Serializable;

public class UserSerialized extends User implements Serializable {
    private static final long  SERIAL_VERSION_UID = -2396608765989592491L;

    private Integer userId;
    private String userName;
    private String passWord;
    private String realName;
    private int coins;

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

    public void setUserSerialized(User user){
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.passWord = user.getPassWord();
        this.realName = user.getRealName();
        this.coins = user.getCoins();
    }

    @Override
    public String toString() {
        return "User_serialized{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", realName='" + realName + '\'' +
                ", coins=" + coins +
                '}';
    }
}
