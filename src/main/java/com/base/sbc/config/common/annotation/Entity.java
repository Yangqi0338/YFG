package com.base.sbc.config.common.annotation;

/**
 * 标记对象为一个实体
 * @author LL
 *
 */
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public abstract @interface Entity{
  public abstract String value() default "";
  public abstract boolean noCache() default false;
}
