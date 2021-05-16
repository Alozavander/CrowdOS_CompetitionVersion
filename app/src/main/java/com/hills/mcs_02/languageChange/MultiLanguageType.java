package com.hills.mcs_02.languageChange;

public enum MultiLanguageType {
    CN("cn"),EN("en"),TW("tw");

    public String typeKey;

    MultiLanguageType(String typeKey){
        this.typeKey = typeKey;
    }

    public static MultiLanguageType match(String codeName){
        if (codeName == null){
            return null;
        }
        for(MultiLanguageType type: values()){
            if(type.typeKey.equals(codeName)){
                return type;
            }
        }
        return null;
    }
}
