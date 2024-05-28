package com.base.sbc.module.basicsdatum.enums;

import java.util.Objects;

/**
 * 描述：季节枚举
 * @address com.base.sbc.module.basicsdatum.enums.SeasonEnum
 * @author 谭博文
 * @email your email
 * @date 创建时间：2024-4-9 9:42:49
 * @version 1.0
 */
public enum SeasonEnum {
    S("S", "春"),
    X("X", "夏"),
    A("A", "秋"),
    W("W", "冬");

    SeasonEnum(String k, String v) {
        code = k;
        name = v;
    }
    private final String code;
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SeasonEnum getBycode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (SeasonEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
