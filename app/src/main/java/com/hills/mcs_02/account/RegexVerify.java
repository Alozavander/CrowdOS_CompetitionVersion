package com.hills.mcs_02.account;

import java.util.HashMap;
import java.util.Map;

public class RegexVerify {
    private Map<String, String> regexes = new HashMap<>();

    public RegexVerify() {
        /** Account password check regular expression. */
        this.regexes.put("pwdRegex", "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$");
        /**  User name regular expression. */
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
}
