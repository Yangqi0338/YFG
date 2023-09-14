package com.base.sbc.config.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 卞康
 * @date 2023/3/31 15:46:55
 */
public class CommonUtils {
    /**
     * 取多个set集合相交的数据，集合可为null，不进行筛选，但是任何一个set集合的长度为0，则无任何相交对象
     * @param sets 可变集合数组
     * @param <T>  集合类型
     * @return 相交的set集合
     */
    @SafeVarargs
    public static <T> Set<T> findCommonElements(Set<T>... sets) {
        List<Set<T>> setList = new ArrayList<>();
        for (Set<T> set : sets) {
            if (set != null) {
                if (set.size() == 0) {
                    return new HashSet<>();
                }
                setList.add(set);
            }
        }

        Set<T> commonSet = setList.get(0);
        for (Set<T> set : setList) {
            commonSet.retainAll(set);
        }
        return commonSet;
    }


    /**
     * 判断是否是初始化id
     * 为空 或者包含 -
     *
     * @param id
     * @return
     */
    public static boolean isInitId(String id) {
        return StringUtils.isBlank(id) || StringUtils.contains(id, StrUtil.DASHED);
    }

    public static StringBuilder newStr(JSONObject fieldJson, Object newObj) {
        StringBuilder istr = new StringBuilder();
        istr.append("{");
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());
        for (int i = 0; i < arrayList.size(); i++) {
            Object val = BeanUtil.getProperty(newObj, fieldJson.getString(arrayList.get(i)));
            if (ObjectUtil.isEmpty(val)) {
                continue;
            }

            istr.append(arrayList.get(i)).append(":").append(val);
            istr.append(";");
        }
        istr.append("}");
        return istr;
    }

    /**
     * 获取所有ApiModelProperty注解字段
     */
    public static JSONObject getFieldJson(Object model) {
        Class<?> clazz = model.getClass();
        return getFieldJson(clazz);
    }

    public static JSONObject getFieldJson(Class<?> clazz) {
        Field[] declaredFields = ReflectUtil.getFields(clazz);
//        Field[] fields = ReflectUtil.getFields(clazz);
        JSONObject jsonObject = new JSONObject();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                String value = annotation.value();
                value = value.split("[:：(（;]")[0];
                jsonObject.put(value, field.getName());
            }
        }
        return jsonObject;
    }

    /**
     * 比较变更字段
     *
     * @param oldObj
     * @param newObj
     * @return
     */
    public static StringBuilder updateStr(Object oldObj, Object newObj, JSONObject fieldJson, JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{(id=" + BeanUtil.getProperty(oldObj, "id") + ")");
        for (int i = 0; i < arrayList.size(); i++) {
            String name = arrayList.get(i);
            String key = fieldJson.getString(name);
            Object oldStr = BeanUtil.getProperty(oldObj, key);
            Object newStr = BeanUtil.getProperty(newObj, key);
            if (ObjectUtil.isEmpty(oldStr) && ObjectUtil.isEmpty(newStr)) {
                continue;
            }

            if (ObjectUtil.equals(newStr, oldStr)) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("oldStr", oldStr);
            jsonObject.put("newStr", newStr);
            jsonArray.add(jsonObject);
            stringBuilder.append(name).append(":");
            stringBuilder.append(oldStr).append("->").append(newStr);

            stringBuilder.append(";");
        }
        stringBuilder.append("}");
        return stringBuilder;
    }

    /**
     * 将创建修改信息设置为空
     *
     * @param t
     * @param <T>
     */
    public static <T extends BaseDataEntity> void resetCreateUpdate(T t) {
        t.setCreateId(null);
        t.setUpdateId(null);
        t.setCreateName(null);
        t.setUpdateName(null);
        t.setCreateDate(null);
        t.setUpdateDate(null);
    }

}
