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

}
