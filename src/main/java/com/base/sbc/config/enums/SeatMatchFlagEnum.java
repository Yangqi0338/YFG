package com.base.sbc.config.enums;

/**
 * 类描述： 坑位匹配枚举
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.config.enums.SeatMatchFlagEnum
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-14 11:18
 */
public enum SeatMatchFlagEnum {

    NO("z0", "坑位(未匹配)"),

    YES("a1", "匹配"),
    CUSTOM_SEAT("b2", "手动匹配(坑位)"),
    CUSTOM_COLOR("c3", "手动匹配(配色)"),
    NO_MATCH_COLOR("d4", "未匹配(配色)"),
    ;

    private final String value;
    private final String name;


    SeatMatchFlagEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
