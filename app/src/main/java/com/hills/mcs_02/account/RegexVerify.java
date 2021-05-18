package com.hills.mcs_02.account;

import java.util.HashMap;
import java.util.Map;

public class RegexVerify {
    private Map<String, String> regexes = new HashMap<>();

    public RegexVerify() {
        //账户密码校验正则表达式：必填字母数字及特殊字符，且以字母开头，6-18位
        this.regexes.put("pwdRegex", "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$");
        //注册账户用户名正则表达式：用户名由字母和数字组成的4-16位字符，字母开头
        this.regexes.put("registerUsernameRegex", "^[a-zA-Z][a-zA-Z0-9]{4,16}$");
    }

    public boolean pwdVerify(String pwdInput) {
        String regex = this.regexes.get("pwdRegex");
        if (pwdInput.matches(regex)) return true;
        else return false;
    }

    public boolean registerUsernameVerfy(String usernameInput){
        String regex = this.regexes.get("registeUsernameRegex");
        if(usernameInput.matches(regex)) return true;
        else return false;
    }

    public void regexesAdd(String key,String value){
        this.regexes.put(key,value);
    }

    public String regexesGet(String key){
        return this.regexes.get(key);
    }

    public void regexesRemove(String key){
        this.regexes.remove(key);
    }

    public boolean regexesContainKey(String key){
        return this.regexes.containsKey(key);
    }

    public boolean regexesContainValue(String value){
        return this.regexes.containsValue(value);
    }
}
