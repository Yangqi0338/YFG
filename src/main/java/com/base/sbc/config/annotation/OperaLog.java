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

    /**
     * value spel
     *
     * @return
     */
    String valueSpEL() default "";

    OperationType operationType() default OperationType.INSERT_UPDATE;

    Class<?> service() default BaseService.class;

    /**
     * 路径的SpEL表达式
     *
     * @return
     */
    String pathSpEL() default "";

    /**
     * 删除时id获取方式 通过SpEL
     *
     * @return
     */
    String delIdSpEL() default "";

    /**
     * 父id sqEl
     *
     * @return
     */
    String parentIdSpEl() default "";

    /**
     * 主键key
     *
     * @return
     */
    String idKey() default "id";
}
