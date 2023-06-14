package com.base.sbc.config.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述： 用户头像
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.config.common.annotation.UserAvatar
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-14 13:34
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UserAvatar {
    /**
     * 用户ID
     *
     * @return
     */
    String value();
}
