package com.oqoqbobo.web.utils;

public class  StringUtils {

    public static Boolean isBlank(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

    public static Boolean isNotBlank(String str){
        return !isBlank(str);
    }
}
