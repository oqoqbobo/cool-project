package com.oqoqbobo.web.utils;

import com.oqoqbobo.web.data.MyException;

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

    public static String camelToUnderline(String str) throws MyException {
        int step = 'a'-'A';
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<str.length();i++){
            if('A' <= str.charAt(i) && str.charAt(i) <= 'Z'){
                Character charLe = new Character((char) (str.charAt(i) + step));
                if(i==0){
                    sb.append(charLe);
                }else{
                    sb.append("_").append(charLe);
                }
            }else if(('a' <= str.charAt(i) && str.charAt(i) <= 'z') || str.charAt(i)=='_'){
                sb.append(str.charAt(i));
            }else{
                throw new MyException("请输入只有英文的字符串");
            }
        }
        return sb.toString();
    }
}
