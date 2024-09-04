package com.base.sbc.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解类
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /**
     * 锁的key，默认使用方法名作为key spEl
     */
    String key() default "";

    /**
     * 获取锁的最大等待时间（单位：秒）
     */
    long waitTime() default 3;

    /**
     * 上锁后有效时间（单位：秒）
     */
    long leaseTime() default 15;

    String errorMsg() default "系统繁忙，请稍后重试";
}