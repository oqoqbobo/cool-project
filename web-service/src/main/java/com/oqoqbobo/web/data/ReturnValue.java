package com.oqoqbobo.web.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by victorlau on 2019/4/25.
 */
@ApiModel
public class ReturnValue implements Serializable {

    public static final String INSERT_SUCCESS = "添加成功!";
    public static final String INSERT_FAIL = "添加失败!";
    public static final String INSERT_ERROR = "添加异常,请联系管理员!";

    public static final String UPDATE_SUCCESS = "修改成功!";
    public static final String UPDATE_FAIL = "修改失败!";
    public static final String UPDATE_ERROR = "修改异常,请联系管理员!";

    public static final String DELETE_SUCCESS = "删除成功!";
    public static final String DELETE_FAIL = "删除失败!";
    public static final String DELETE_ERROR = "删除异常,请联系管理员!";

    public static final String OPERATE_ERROR = "操作异常,请联系管理员!";

    public static final String SELECT_FAIL = "查询失败!";
    public static final String SELECT_ERROR = "查询异常,请联系管理员!";

    public static final String CHECK_FAIL = "不可用";
    public static final String CHECK_ERROR = "校验重复异常,请联系管理员!";

    // 绑定和解绑
    public static final String BING_SUCCESS = "绑定成功!";
    public static final String BING_FAIL = "绑定失败!";
    public static final String UNBING_SUCCESS = "解绑成功!";
    public static final String UNBING_FAIL = "解绑失败!";

    public static final String EXECUTE_SUCCESS = "执行成功!";
    public static final String EXECUTE_FAIL = "执行失败!";
    public static final String EXECUTE_ERROR = "执行异常,请联系管理员!";

    public static final String NO_AUTH = "接口未授权！";

    public static final String CODE_SUCCESS = "200";

    public static final String CODE_FAIL = "500";

    public ReturnValue() {
    }

    public ReturnValue(String code, String msg) {
        checkLogin();
        this.code = code;
        this.msg = msg;
    }

    public static ReturnValue success() {
        ReturnValue returnValue = new ReturnValue(CODE_SUCCESS, "success");
        return returnValue;
    }

    public static ReturnValue success(String msg) {
        ReturnValue returnValue = new ReturnValue(CODE_SUCCESS, msg);
        return returnValue;
    }

    public static ReturnValue fail(String msg) {
        ReturnValue returnValue = new ReturnValue(CODE_FAIL, msg);
        return returnValue;
    }

    protected void checkLogin() {
        /*UserAccount currentUser = SecurityContext.getCurrentUser();
        if (currentUser != null) {
            this.setIsLogined(true);
        } else {
            this.setIsLogined(false);
        }*/
        this.setIsLogined(true);
    }

    public boolean isSuccess() {
        if (CODE_SUCCESS.equals(this.code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 编码
     */
    @ApiModelProperty("返回码 200:成功 500：失败")
    private String code;
    /**
     * 提示
     */
    @ApiModelProperty("返回描述")
    private String msg;

    @ApiModelProperty("是否登录")
    private boolean isLogined;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getIsLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }
}
