package com.base.sbc.config;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.IJsonTypeHandler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.base.sbc.config.annotation.ExtendField;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * mybatis-plus自动填充字段值配置
 *
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */

@Component
public class AutoFillFieldValueConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        UserCompany userCompany = userInfo();
        this.setFieldValByName("createDate", new Date(), metaObject);
        this.setFieldValByName("createName", userCompany.getAliasUserName(), metaObject);
        this.setFieldValByName("createId", userCompany.getUserId(), metaObject);
        this.setFieldValByName("companyCode", userCompany.getCompanyCode(), metaObject);
        this.setFieldValByName("delFlag", "0", metaObject);
        updateFill(metaObject);
    }

    private void decorateExtendFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        Field[] declaredFields = ReflectUtil.getFields(originalObject.getClass());
        List<Field> mayExtendFieldList = Arrays.stream(declaredFields).filter(it -> it.isAnnotationPresent(TableField.class))
                .filter(it -> IJsonTypeHandler.class.isAssignableFrom(it.getAnnotation(TableField.class).typeHandler())).collect(Collectors.toList());
        for (Field extendField : mayExtendFieldList) {
            Class<? extends TypeHandler> typeHandler = extendField.getAnnotation(TableField.class).typeHandler();

            String extendFieldName = extendField.getName();
            Map<String, Object> map = new HashMap<>();
            Arrays.stream(declaredFields).filter(it -> it.isAnnotationPresent(ExtendField.class))
                    .filter(it -> it.getAnnotation(ExtendField.class).value().equals(extendFieldName)).forEach(field -> {
                        try {
                            field.setAccessible(true);
                            Object obj = field.get(originalObject);
                            if (obj != null) {
                                String value;
                                if (typeHandler.equals(JacksonExtendHandler.class)) {
                                    value = JacksonExtendHandler.getObjectMapper().writeValueAsString(obj);
                                } else if (typeHandler.equals(FastJson2ExtendHandler.class)) {
                                    value = JSON.toJSONString(obj, JSONWriter.Feature.WriteMapNullValue,
                                            JSONWriter.Feature.WriteNullListAsEmpty, JSONWriter.Feature.WriteNullStringAsEmpty);
                                } else {
                                    throw new UnsupportedOperationException();
                                }
                                map.put(field.getName(), value);
                            }
                            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                                String comment = field.getAnnotation(ApiModelProperty.class).value();
                                map.put(field.getName() + "_REMARK", comment);
                            }
                        } catch (IllegalAccessException | JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
            // 枚举加入
            Arrays.stream(declaredFields).filter(Field::isEnumConstant).forEach(field -> {
                try {
                    field.setAccessible(true);
                    Enum<?> enumObj = (Enum<?>) field.get(originalObject);
                    if (enumObj != null) {
                        String value;
                        if (typeHandler.equals(JacksonExtendHandler.class)) {
                            value = JacksonExtendHandler.getObjectMapper().writeValueAsString(enumObj);
                        } else if (typeHandler.equals(FastJson2ExtendHandler.class)) {
                            value = JSON.toJSONString(enumObj, JSONWriter.Feature.WriteMapNullValue,
                                    JSONWriter.Feature.WriteNullListAsEmpty, JSONWriter.Feature.WriteNullStringAsEmpty);
                        } else {
                            throw new UnsupportedOperationException();
                        }
                        map.put(field.getName() + "Name", value);
                    }
                } catch (IllegalAccessException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            this.setFieldValByName(extendFieldName, map, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserCompany userCompany = userInfo();
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("updateName", userCompany.getAliasUserName(), metaObject);
        this.setFieldValByName("updateId", userCompany.getUserId(), metaObject);
        decorateExtendFill(metaObject);
    }

    private UserCompany userInfo() {
        UserCompany userCompany = 	companyUserInfo.get();
        if (userCompany == null) {
            userCompany =new UserCompany();
            userCompany.setAliasUserName("系统管理员");
            userCompany.setUserId("0");
            userCompany.setCompanyCode("677447590605750272");
        }
        if (StringUtils.isEmpty(userCompany.getUserId()) || StringUtils.isEmpty(userCompany.getCompanyCode())) {
            throw new RuntimeException("非法操作，当前登录用户信息不完整");
        }
        return userCompany;
    }
}
