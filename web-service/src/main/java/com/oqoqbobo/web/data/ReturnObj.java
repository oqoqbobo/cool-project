package com.oqoqbobo.web.data;

import io.swagger.annotations.ApiModelProperty;

public class ReturnObj<T> extends ReturnValue {

    @ApiModelProperty("返回的数据对象")
    private T data;

    public ReturnObj() {
    }

    public ReturnObj(String code, String desc) {
        super(code, desc);
    }

    public ReturnObj(String code, String desc, T data) {
        super(code, desc);
        this.data = data;
    }

    public static <T> ReturnObj<T> success(T data) {
        ReturnObj<T> returnData = new ReturnObj(CODE_SUCCESS, "success");
        returnData.setData(data);
        return returnData;
    }

    public static <T> ReturnObj<T> success(String msg, T data) {
        ReturnObj<T> returnData = new ReturnObj(CODE_SUCCESS, msg);
        returnData.setData(data);
        return returnData;
    }

    public static <T> ReturnObj<T> fail(String msg, T data) {
        ReturnObj<T> returnData = new ReturnObj(CODE_FAIL, msg);
        returnData.setData(data);
        return returnData;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
