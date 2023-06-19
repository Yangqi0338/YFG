package com.base.sbc.config.annotation;

import com.base.sbc.config.common.base.BaseService;
import com.base.sbc.config.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author 卞康
 * @date 2023/6/17 15:58:13
 * @mail 247967116@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperaLog {
    String value() default "";
    OperationType operationType() default OperationType.INSERT_UPDATE;
    Class<?> service() default BaseService.class;

}
