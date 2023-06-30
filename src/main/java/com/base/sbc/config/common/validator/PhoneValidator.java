package com.base.sbc.config.common.validator;

import com.base.sbc.config.common.annotation.IsPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public class PhoneValidator implements ConstraintValidator<IsPhone, Long> {
	 
    private Pattern pattern = Pattern.compile("1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}");
 
    @Override
    public void initialize(IsPhone phone) {
    }
 
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(String.valueOf(value)).matches();
    }
}
