package com.base.sbc.config.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

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
}
