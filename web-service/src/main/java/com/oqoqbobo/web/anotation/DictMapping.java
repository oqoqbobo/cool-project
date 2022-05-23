package com.oqoqbobo.web.anotation;

import java.lang.annotation.*;

/**
 * @author chenxin
 * @date 2021-07-12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictMapping {
    boolean isFomatTime() default false;
    //字典类型编码
    String dictCode() default "";

    //字典值来源字段
    String sourceField() default "";

    //目标字符串拼接，如值为200 targetJoinFiexStr为：A 则目标值为:200A
    String targetJoinFiexStr() default "";

    //是否校验值为254异常、255无效
    boolean isValidateError() default false;

    //校验数据长度 1-254异常、255无效,2-65534异常、65535无效, 3-4094异常、4095异常, 4-4294967294异常、4294967295异常
    int validateErrorLenth() default 0;
}
