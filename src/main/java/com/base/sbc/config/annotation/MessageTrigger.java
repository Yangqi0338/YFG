package com.base.sbc.config.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageTrigger {

    /** 业务对象 */
    String value();

    /** 操作触发 code */
    String code();
}
