package com.oqoqbobo.web.model.util;

public class CheckBO implements CharSequence {
    private String content;
    @Override
    public int length() {
        return content.length();
    }

    @Override
    public char charAt(int index) {
        return content.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return content.substring(start,end);
    }

    public CheckBO(String str){
        this.content = str;
    }
}
