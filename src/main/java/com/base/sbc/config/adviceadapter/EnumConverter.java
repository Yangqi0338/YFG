package com.base.sbc.config.adviceadapter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EnumConverter implements ConverterFactory<String, Enum<?>> {
	@Override
	public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnum<>(targetType);
	}
	private static final class StringToEnum<T extends Enum> implements Converter<String, T> {
		private final Class<T> enumType;
		public StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}
		@Override
		public T convert(String source) {
			if (source.isEmpty()) {
				// It's an empty enum identifier: reset the enum value to null.
				return null;
			}
			try {
				//先通过name获取枚举
				return (T) Enum.valueOf(enumType, source);
			} catch (Exception e) {
				Field[] declaredFields = enumType.getDeclaredFields();
				for (Field declaredField : declaredFields) {
					EnumValue[] annotationsByType = declaredField.getAnnotationsByType(EnumValue.class);
					if (annotationsByType.length > 0) {
						String name = declaredField.getName();
						Object convert = Convert.convert(declaredField.getType(), source);
						return getEnumObj(enumType, name, convert);
					}
				}
			}
			return null;
		}
		/**
		 * 通过反射的方式 获取枚举实例对象
		 *
		 * @param clazz     枚举类型
		 * @param fieldName @JSONValue 所在的字段名
		 * @param source    前端传进来的值
		 * @return 对应的枚举实例
		 * @author jzw
		 * @since 2021/11/12 10:11
		 */
		private T getEnumObj(Class<T> clazz, String fieldName, Object source) {
			T[] enums = clazz.getEnumConstants();
			if (null != enums) {
				for (T e : enums) {
					Object fieldValue = ReflectUtil.getFieldValue(e, fieldName);
					if (fieldValue.equals(source)) {
						return e;
					}
				}
			}
			return null;
		}
	}
}