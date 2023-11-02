package com.base.sbc.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 重复提交校验
 * @author 卞康
 * @date 2023/10/26 11:12:13
 * @mail 247967116@qq.com
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface DuplicationCheck {
    /**
     * 是否开启重复提交校验
     */
    boolean value() default true;

    /**
     * 重复提交校验时间间隔
     */
    long time() default 3;

    /**
     * 重复提交校验提示信息
     */
    String message() default "请勿重复提交";


    /**
     * 校验类型,
     * 1:校验请求地址,
     * 2:校验请求地址 + RequestBody 请求参数,
     * 3:校验请求地址 + RequestParam 请求参数,
     * 4:校验请求地址 + RequestBody+ RequestParam 请求参数
     */
    int type() default 2;

}
