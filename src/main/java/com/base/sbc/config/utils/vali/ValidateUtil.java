package com.base.sbc.config.utils.vali;

import com.base.sbc.config.exception.OtherException;

/**
 * @description: 自定义验证
 * @author: fred
 * @date: 2022/4/1
 * @version: 1.0
 */
public class ValidateUtil {

    /**
     * 其他异常验证
     *
     * @param expression
     * @param message
     * @param values
     */
    public static void validState(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new OtherException(String.format(message, values));
        }
    }
}
