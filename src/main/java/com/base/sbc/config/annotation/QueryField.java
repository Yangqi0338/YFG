package com.base.sbc.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 卞康
 * @date 2023/10/21 18:45:20
 * @mail 247967116@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryField {
    /**
     * BaseQueryWrapper 的方法名称
     * 查询类别 eq 等于 ne  大于 lt 小于 ge 大于等于 le 小于等于 like 模糊查询
     */
    String type() default "eq";


    //列头筛选字段

    //字段名
    String value() default "";

    //类型处理，例如时间类型，或需要替换
    String property() default "";

    String columnFilter() default "";

    String columnFilterExtent() default "";

}
