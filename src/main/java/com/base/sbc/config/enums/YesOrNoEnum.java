package com.base.sbc.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @Author xhj
 * @Date 2023/6/16 17:26
 */
public enum YesOrNoEnum {

    NO(0, "0", "否"),

    YES(1, "1", "是"),
    ;

    private final Integer value;
    @EnumValue
    private final String valueStr;
    private final String name;

    YesOrNoEnum(Integer value, String valueStr, String name) {
        this.value = value;
        this.valueStr = valueStr;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    @JsonValue
    public String getValueStr() {
        return valueStr;
    }

    public String getName() {
        return name;
    }

    public YesOrNoEnum reverse() {
        return this == YesOrNoEnum.YES ? YesOrNoEnum.NO : (this == YesOrNoEnum.NO ? YesOrNoEnum.YES : null);
    }

    public static YesOrNoEnum findByValue(String value) {
        return Arrays.stream(YesOrNoEnum.values()).filter(it-> it.getValueStr().equals(value)).findFirst().orElse(null);
    }

    public static YesOrNoEnum findByValue(Integer value) {
        return Arrays.stream(YesOrNoEnum.values()).filter(it-> it.getValue().equals(value)).findFirst().orElse(null);
    }

    public static YesOrNoEnum findByValue(boolean value) {
        return value ? YesOrNoEnum.YES : YesOrNoEnum.NO;
    }
}
