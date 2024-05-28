package com.base.sbc.config.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.common.annotation.ValidCondition;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ValidConditionValidator implements ConstraintValidator<ValidCondition, Object> {


    private String[] columnValue;

    private String column;

    private static Validator validator;

    /**
     * 初始化数据
     *
     * @param constraintAnnotation OtherAffect注解实例
     * @return void
     * @author Gangbb
     * @date 2021/9/20
     **/
    @Override
    public void initialize(ValidCondition constraintAnnotation) {
        columnValue = constraintAnnotation.columnValue();
        column = constraintAnnotation.column();
        // 获取校验Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    /**
     * 校验逻辑方法
     *
     * @param o       当前字段的值
     * @param context ConstraintValidatorContext校验上下文
     * @return boolean
     * @author Gangbb
     * @date 2021/9/20
     **/
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        // 获取column的请求值,为空的话校验失败
        String columnRequestValue = getColumnRequestValue();
//        if (StrUtil.isBlank(columnRequestValue)) {
//            return ValidatorUtils.getResult(StrUtil.format("请求值{}不能为空", column), context);
//        }
//
//        // 如果column的值存在于columnValue中
//        if (Arrays.asList(columnValue).contains(columnRequestValue)) {
//            // 被注解字段的值为空直接校验不通过
//            if (ObjectUtil.isEmpty(o)) {
//                return false;
//            }
//            // notAllowedValues、allowedValues存在情况
//            boolean b = (notAllowedValues != null && notAllowedValues.length > 0) || (allowedValues != null && allowedValues.length > 0);
//            if (b) {
//                String validResult = ValidatorUtils.validateValues(allowedValues, notAllowedValues, o);
//                return ValidatorUtils.getResult(validResult, context);
//            }
//
//            // 如果开启校验
//            if (isCheck) {
//                return validObject(o, context);
//            }
//        }
        return true;
    }

    /**
     * 获取column的请求值
     *
     * @return String
     * @date 2022/1/13
     **/
    private String getColumnRequestValue() {
//        String paramValue = ServletUtils.getParameter(column);
//        // 如果从param获取不到,再找body
//        if(StringUtils.isBlank(paramValue)){
//            HttpServletRequest request = ServletUtils.getRequest();
//            // 获取column请求参数值
//            String body = ServletUtil.getBody(request);
//            if(StrUtil.isNotBlank(body)){
//                JSONObject jsonObject = JSONUtil.parseObj(body);
//                paramValue = String.valueOf(jsonObject.get(column));
//            }
//        }
        return "";
    }

    /**
     * 校验传入对象
     *
     * @param o       当前字段的值
     * @param context ConstraintValidatorContext校验上下文
     * @return boolean
     * @author Gangbb
     * @date 2021/9/20
     **/
    private boolean validObject(Object o, ConstraintValidatorContext context) {
        // 定义错误信息
        StringBuffer errorMsg = new StringBuffer();
        boolean result = true;
        // 当被注解属性为列表时
        if (o instanceof ArrayList<?>) {
            List<Set<ConstraintViolation<Object>>> collect = new ArrayList<>();
            for (Object oItem : (List<?>) o) {
                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(oItem);
                collect.add(constraintViolations);
            }
            result = getListResult(collect);
//            ValidatorUtils.getListInfo(errorMsg, collect);
        } else {
            // 当被注解属性为单个对象时
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);
            result = constraintViolations.isEmpty();
//            ValidatorUtils.getOneInfo(errorMsg, constraintViolations);
        }

        // 把自定义返回错误信息写入上下文
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMsg.toString())
                    .addConstraintViolation();
        }
        return result;
    }


    /**
     * 获取列表属性值校验结果
     *
     * @param collect 校验结果集合
     * @return boolean
     * @author Gangbb
     * @date 2021/9/20
     **/
    private boolean getListResult(List<Set<ConstraintViolation<Object>>> collect) {
        boolean result = true;
        for (Set<ConstraintViolation<Object>> constraintViolations : collect) {
            result = constraintViolations.isEmpty();
        }
        return result;
    }
}

