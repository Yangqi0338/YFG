package com.base.sbc.config.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否开启数据隔离
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataIsolation {
    boolean state() default  true; //默认开启数据隔离,一定会角色的数据隔离
    boolean defaultState() default  true; //默认只开启部门，岗位，角色的数据隔离
    boolean deptState() default  false; //开启部门的数据隔离
    boolean jobState() default  false; //开启岗位的数据隔离
    /**
     * 指定要被数据隔离的方法,不指定，默认隔离所有SELECT方法
     * 例子 groups={"'SELECT'"}
     */
    String[] groups() default {};
}
