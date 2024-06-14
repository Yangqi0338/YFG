package com.base.sbc.config.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.exception.OtherException;
import lombok.SneakyThrows;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * {@code 描述：手动校验工具}
 * @author KC
 * @since 2024/1/11
 * @CopyRight @ 广州尚捷科技有限公司
 */
public class ValidationUtil {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @SneakyThrows
    public static <T> void validate(T t, Class<?>... group) {
        Set<ConstraintViolation<T>> constraintViolations;
        if (group.length > 1) {
            constraintViolations = VALIDATOR.validate(t, group);
        }else {
            constraintViolations = VALIDATOR.validate(t);
        }
        if (CollectionUtil.isNotEmpty(constraintViolations)) {
            StringJoiner errorJoiner = new StringJoiner(";");
            constraintViolations.forEach((constraintViolation)-> {
                errorJoiner.add(constraintViolation.getMessage());
            });
            throw new OtherException(errorJoiner.toString());
        }
    }

    @SneakyThrows
    public static <T> void validate(List<T> list, Class<?>... group) {
        list.forEach(it-> validate(it, group));
    }
}
