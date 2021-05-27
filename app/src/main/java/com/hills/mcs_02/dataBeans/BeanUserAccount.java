package com.hills.mcs_02.dataBeans;

public class BeanUserAccount {
    private String state;                      /**  Indicates the validation status. 0 is incorrect and 1 is correct */
    private String id;
    private String name;
    private String pwd;
    private String phone;

    public BeanUserAccount(){

    }

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

}
