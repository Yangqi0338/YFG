package com.base.sbc.module.basicsdatum.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

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
    SPRING("S", "春"),
    SUMMER("X", "夏"),
    AUTUMN("A", "秋"),
    WINTER("W", "冬");

    SeasonEnum(String k, String v) {
        code = k;
        name = v;
    }

    @EnumValue
    private final String code;
    private final String name;

    @JsonValue
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
