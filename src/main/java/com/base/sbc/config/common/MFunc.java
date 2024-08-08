package com.base.sbc.config.common;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface MFunc<T, R> extends Function<T, R>, Serializable {

    @SneakyThrows
    default String getFieldName() {
        String methodName = getMethodName();
        if (methodName.startsWith("get")) {
            methodName = methodName.substring(3);
        }
        return CharSequenceUtil.lowerFirst(methodName);
    }

    @SneakyThrows
    default String getMethodName() {
        return getSerializedLambda().getImplMethodName();
    }

    @SneakyThrows
    default SerializedLambda getSerializedLambda() {
        Method method = getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        return (SerializedLambda) method.invoke(this);
    }

    @SneakyThrows
    default Class<?> getReturnType() {
        SerializedLambda lambda = getSerializedLambda();
        Class<?> className = Class.forName(lambda.getImplClass().replace("/", "."));
        Method method = className.getMethod(getMethodName());
        return method.getReturnType();
    }

}