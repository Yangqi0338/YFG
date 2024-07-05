/******************************************************************************
 * Copyright (C) 2018 celizi.com
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.config.FastJson2ExtendHandler;
import com.base.sbc.config.JacksonExtendHandler;
import com.base.sbc.config.annotation.ExtendField;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */
@Data
public class BaseDataExtendEntity extends BaseDataEntity<String> {

    private static Map<Class<?>, List<Field>> declaredFieldMap = new HashMap<>();
    private static Class<? extends TypeHandler> typeHandlerClass;
    /** 扩展字段 */
    @JsonIgnore
    @ApiModelProperty(value = "扩展字段")
    @TableField(typeHandler = JacksonExtendHandler.class)
    private Map<String, Object> extend = new HashMap<>();

    public static Class<? extends TypeHandler> getTypeHandlerClass() {
        if (typeHandlerClass == null) {
            try {
                Field field = BaseDataExtendEntity.class.getDeclaredField("extend");
                TableField tableField = field.getAnnotation(TableField.class);
                typeHandlerClass = tableField.typeHandler();
            } catch (NoSuchFieldException ignored) {
            }
        }
        return typeHandlerClass;
    }

    @JsonAnyGetter
    public Map<Object, Object> decorateWebMap() {
        Map<Object, Object> map = new HashMap<>();
        Field[] declaredFields = ReflectUtil.getFields(this.getClass());
        List<Field> handlerFieldList = Arrays.stream(declaredFields).filter(it -> it.isAnnotationPresent(ExtendField.class)).collect(Collectors.toList());
        if (handlerFieldList.isEmpty()) return map;

        for (Field declaredField : handlerFieldList) {
            declaredField.setAccessible(true);
            try {
                decorateMapByDetail(map, declaredField.getName(), declaredField.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    public void decorateMapByDetail(Map<Object, Object> map, String pre, Object obj) throws IllegalAccessException {
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            if (obj instanceof CharSequence) {
                map.put(pre, obj.toString());
            } else {
                Field[] declaredFields = ReflectUtil.getFields(clazz);
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    // 先只支持List和普通
                    String key = pre + StrUtil.upperFirst(declaredField.getName());
                    if (declaredField.getType().isAssignableFrom(List.class)) {
                        List<?> objects = (List<?>) declaredField.get(obj);
                        if (CollUtil.isNotEmpty(objects)) {
                            this.decorateMapByDetail(map, key, objects);
                        }
                    } else {
                        map.put(key, declaredField.get(obj).toString());
                    }
                }
            }
        }
    }

    public void decorateMapByDetail(Map<Object, Object> map, String pre, List<?> objects) throws IllegalAccessException {
        for (Object object : objects) {
            decorateMapByDetail(map, pre, object);
        }
    }

    public void build() {
        if (MapUtil.isEmpty(this.extend)) return;
        List<Field> declaredFields = declaredFieldMap.getOrDefault(this.getClass(), new ArrayList<>());
        if (CollUtil.isEmpty(declaredFields)) {
            declaredFields = Arrays.asList(this.getClass().getDeclaredFields());
            declaredFields.forEach(it -> it.setAccessible(true));
            declaredFieldMap.put(this.getClass(), declaredFields);
        }
        try {
            for (Map.Entry<String, Object> entry : this.extend.entrySet()) {
                String fieldName = entry.getKey();
                Optional<Field> fieldOpt = declaredFields.stream().filter(it -> it.getName().equals(fieldName)).findFirst();
                if (fieldOpt.isPresent()) {
                    Field field = fieldOpt.get();
                    Object obj;
                    String json = entry.getValue().toString();
                    Class<?> type = field.getType();
                    Class<? extends TypeHandler> typeHandlerClass = getTypeHandlerClass();
                    if (JacksonExtendHandler.class.isAssignableFrom(typeHandlerClass)) {
                        obj = JacksonExtendHandler.getObjectMapper().readValue(json, type);
                    } else if (FastJson2ExtendHandler.class.isAssignableFrom(typeHandlerClass)) {
                        obj = JSON.parseObject(json, type);
                    } else {
                        throw new UnsupportedOperationException("不支持该操作");
                    }
                    field.set(this, obj);
                }
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
