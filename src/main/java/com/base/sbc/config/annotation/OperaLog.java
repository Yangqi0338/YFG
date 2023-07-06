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

    /**
     * value是否使用SpEL表达式
     *
     * @return
     */
    boolean SqEL() default false;

    /**
     * 删除时id获取方式 通过SpEL
     *
     * @return
     */
    String delIdSpEL() default "";
}
