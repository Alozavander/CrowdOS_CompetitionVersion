package com.hills.mcs_02.dataBeans;

public class BeanRecyclerViewMineMinor4Wallet {

    private int userId;
    private String username;
    private int userIcon;
    private int userCoin;

    public BeanRecyclerViewMineMinor4Wallet(){
        super();
    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public int getUserIcon(){
        return userIcon;
    }

    public void setUserIcon(int userIcon){
        this.userIcon = userIcon;
    }

    public int getUserCoin(){
        return userCoin;
    }

    public void setUserCoin(int userCoin){
        this.userCoin = userCoin;
    }

    @Override
    public String toString(){
        return "userId:" + userId + ","
                + "userName:" + username + ","
                + "userCoins:" + userCoin;
    }

}
