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

    default <V> V decorateResult(Optional<V> opt, V defaultValue) {
        String msg = getWarnMsg();
        if (msg != null) {
            removeMsg();
            return opt.orElseThrow(() -> new OtherException(msg));
        } else {
            return opt.orElse(defaultValue);
        }
    }


}
