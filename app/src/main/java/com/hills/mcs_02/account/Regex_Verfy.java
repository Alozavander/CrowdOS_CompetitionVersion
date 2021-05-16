package com.hills.mcs_02.account;

import java.util.HashMap;
import java.util.Map;

public class Regex_Verfy {
    private Map<String, String> regexes = new HashMap<>();

    public Regex_Verfy() {
        //账户密码校验正则表达式：必填字母数字及特殊字符，且以字母开头，6-18位
        this.regexes.put("pwd_regex", "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$");
        //注册账户用户名正则表达式：用户名由字母和数字组成的4-16位字符，字母开头
        this.regexes.put("registe_username_regex", "^[a-zA-Z][a-zA-Z0-9]{4,16}$");
    }

    public boolean pwd_verfy(String pwd_input) {
        String regex = this.regexes.get("pwd_regex");
        if (pwd_input.matches(regex)) return true;
        else return false;
    }

    public boolean registe_username_verfy(String username_input){
        String regex = this.regexes.get("registe_username_regex");
        if(username_input.matches(regex)) return true;
        else return false;
    }

    public void regexs_add(String key,String value){
        this.regexes.put(key,value);
    }

    public String regexs_get(String key){
        return this.regexes.get(key);
    }

    public void regexs_remove(String key){
        this.regexes.remove(key);
    }

    public boolean regexs_contain_key(String key){
        return this.regexes.containsKey(key);
    }

    public boolean regexs_contain_value(String value){
        return this.regexes.containsValue(value);
    }
}
