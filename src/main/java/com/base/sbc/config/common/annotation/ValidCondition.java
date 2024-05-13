package com.base.sbc.config.common.annotation;

import com.base.sbc.config.common.ValidConditionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ValidCondition.List.class)
@Constraint(validatedBy = {ValidConditionValidator.class})
public @interface ValidCondition {

    /**
     * 决定该字段是否需要校验的字段名
     */
    String column();

    /**
     * column的会触发该校验的条件值
     **/
    String[] columnValue();

    Class<? extends Annotation>[] checkClass()  default { };

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidCondition[] value();
    }

}