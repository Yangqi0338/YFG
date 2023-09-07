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
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DataIsolation {
    boolean state() default  true; //默认开启数据隔离
    boolean defaultState() default  true; //默认只开启部门，岗位，角色的数据隔离
    boolean deptState() default  false; //开启部门的数据隔离
    boolean jobState() default  false; //开启岗位的数据隔离

    boolean operateType() default  true; //是否是查询，其它操作为false
    /**
     * 定义在类上：指定要被数据隔离的sql方法,不指定，默认隔离改类下面所有sql方法；定义在方法上：指定要被数据隔离的sql方法,不指定，默认使用该方法
     * 例子 groups={"'SELECT'"}
     */
    String[] groups() default {};
    /**
     * 指定要被数据隔离的标签
     */
    String authority() default "";
    /**
     * 如果数据隔离表没有指定隔离字段的表别人，则该定义有效。指定使用的数据隔离字段,,,不写的话默认是主表的字段
     * 例子 authorityField={"s.prod_category"}
     */
    String[] authorityFields() default {};
}
