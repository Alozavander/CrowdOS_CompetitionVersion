package com.hills.mcs_02.dataBeans;

public class Bean_RecyclerView_mine_minor4_wallet {

    private int userId;
    private String userName;
    private int userIcon;
    private int userCoins;

    public Bean_RecyclerView_mine_minor4_wallet(){
        super();
    }

    public Bean_RecyclerView_mine_minor4_wallet(int userId, String userName, int userIcon, int userCoins){
        this.userId = userId;
        this.userName = userName;
        this.userIcon = userIcon;
        this.userCoins = userCoins;
    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public int getUserIcon(){
        return userIcon;
    }

    public void setUserIcon(int userIcon){
        this.userIcon = userIcon;
    }

    public int getUserCoins(){
        return userCoins;
    }

    public void setUserCoins(int userCoins){
        this.userCoins = userCoins;
    }

    @Override
    public String toString(){
        return "userId:" + userId + ","
                + "userName:" + userName + ","
                + "userCoins:" + userCoins;
    }

}
