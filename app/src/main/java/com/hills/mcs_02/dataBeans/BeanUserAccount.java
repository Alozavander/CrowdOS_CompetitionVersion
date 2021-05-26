package com.hills.mcs_02.dataBeans;

////当前Bean主要直接对接应用的直接使用
public class BeanUserAccount {
    private String state;                       //表示验证状态，0为不正确，1为正确
    private String id;
    private String name;
    private String pwd;
    private String phone;
    private String email;
    private String sex;
    //private String sign;                       //个人签名,暂时预留

    public BeanUserAccount(){

    }

    /*
    public Bean_UserAccount(UserAccount_GetDataParse uadp){
        this.id = uadp.getId();
        this.name = uadp.getName();
        this.sex = uadp.getSex();
    }*/

    public String getState() {
        return state;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    /*
    public String getSign() {
        return sign;
    }*/

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /*public void setSign(String sign) {
        this.sign = sign;
    }*/
}
