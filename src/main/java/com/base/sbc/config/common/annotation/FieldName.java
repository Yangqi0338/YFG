package com.base.sbc.config.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * bean中文名注解
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)  
public @interface FieldName {

	String value();
	 
}
