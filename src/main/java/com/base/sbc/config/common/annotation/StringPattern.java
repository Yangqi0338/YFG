package com.base.sbc.config.common.annotation;

import com.base.sbc.config.common.validator.StringPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringPatternValidator.class)
@Documented
public @interface StringPattern {

    String message() default "string in list no match pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp();

    /**
     * Defines several {@link ListStringPattern} annotations on the same element.
     * 
     * @see ListStringPattern
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
         StringPattern[] value();
    } 

}
