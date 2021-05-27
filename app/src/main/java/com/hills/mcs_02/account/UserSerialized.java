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

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCoin() {
        return coins;
    }

    public void setCoin(int coin) {
        this.coins = coin;
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
