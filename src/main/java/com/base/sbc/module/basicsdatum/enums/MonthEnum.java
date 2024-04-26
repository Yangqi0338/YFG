package com.base.sbc.module.basicsdatum.enums;

import java.util.Objects;

/**
 * 描述：月份枚举
 * @address com.base.sbc.module.basicsdatum.enums.SeasonEnum
 * @author 谭博文
 * @email your email
 * @date 创建时间：2024-4-9 9:42:49
 * @version 1.0
 */
public enum MonthEnum {
    January("01", "01"),
    February("02", "02"),
    March("03", "03"),
    April("04", "04"),
    May("05", "05"),
    June("06", "06"),
    July("07", "07"),
    August("08", "08"),
    September("09", "09"),
    October("10", "10"),
    November("11", "11"),
    December("12", "12");

    MonthEnum(String k, String v) {
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

    public static MonthEnum getBycode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (MonthEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
