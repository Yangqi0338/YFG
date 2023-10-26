package com.base.sbc.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.base.sbc.module.smp.dto.SampleBean;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/21 11:06:18
 * @mail 247967116@qq.com
 */
public class JsonStringUtils {

    /**
     * 将对象转为json字符串,空字段转为默认值或者空字符串
     *
     * @param o 对象
     * @return json字符串
     */
    public static String toJSONString(Object o) {
        List<Field> allFields = JsonStringUtils.getAllFields(o.getClass());
        for (Field field : allFields) {
            Class<?> type = field.getType();
            String name = type.getName();
            field.setAccessible(true);
            System.out.println(name);
            try {
                if ("java.math.BigDecimal".equals(name)) {
                    if (field.get(o) == null) {
                        field.set(o, new BigDecimal(0));
                    }
                }
                // if ("com.base.sbc.module.smp.dto.SampleBean$Colorway".equals(name)) {
                //     if (field.get(o) == null) {
                //         field.set(o, new SampleBean.Colorway());
                //     }
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return JSON.toJSONString(o, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse);
    }


    /**
     * 获取所有的字段
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
