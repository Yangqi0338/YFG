package com.base.sbc.config.enums;

/**
 * @Author xhj
 * @Date 2023/6/16 17:26
 */
public enum YesOrNoEnum {

    NO(0, "0", "否"),

    YES(1, "1", "是"),
    ;

    private final Integer value;
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

    public String getValueStr() {
        return valueStr;
    }

    public String getName() {
        return name;
    }
}
