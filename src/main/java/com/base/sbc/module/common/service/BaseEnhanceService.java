package com.base.sbc.module.common.service;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.config.exception.OtherException;

import java.util.Optional;

/**
 * @author 卞康
 * @date 2023/4/13 11:49:17
 * 自定义增强
 */
public interface BaseEnhanceService<T> {

    ThreadLocal<String> warnMsg = new TransmittableThreadLocal<>();
    ThreadLocal<Object> defaultValue = new TransmittableThreadLocal<>();

    default void warnMsg(String warnMsg) {
        while (getWarnMsg() == null) {
            try {
                Thread.sleep(50);
                BaseEnhanceService.warnMsg.set(warnMsg);
            } catch (InterruptedException ignored) {
            }
        }
    }

    default void removeMsg() {
        if (getWarnMsg() != null) {
            BaseEnhanceService.warnMsg.remove();
        }
    }

    default String getWarnMsg() {
        return BaseEnhanceService.warnMsg.get();
    }

    default void defaultValue(Object defaultValue) {
        while (getDefaultValue() == null) {
            try {
                Thread.sleep(50);
                BaseEnhanceService.defaultValue.set(defaultValue);
            } catch (InterruptedException ignored) {
            }
        }
    }

    default void removeDefaultValue() {
        if (getDefaultValue() != null) {
            BaseEnhanceService.defaultValue.remove();
        }
    }

    default Object getDefaultValue() {
        return BaseEnhanceService.defaultValue.get();
    }

    default <V> V decorateResult(Optional<V> opt) {
        String msg = getWarnMsg();
        V result;
        if (msg != null) {
            result = opt.orElseThrow(() -> new OtherException(msg));
        } else {
            Object defaultValue = getDefaultValue();
            if (defaultValue != null) {
                result = opt.orElse((V) defaultValue);
            } else {
                result = opt.orElse(null);
            }
        }
        removeMsg();
        removeDefaultValue();
        return result;
    }


}
