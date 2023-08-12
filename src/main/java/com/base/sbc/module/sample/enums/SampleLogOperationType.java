package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleLogOperationType {
    ALl("-1", "全部"),
    BORROW("1", "借出"),

    SAMPLE_RETURN("2", "归还"),

    SALES("3", "销售"),
    INVENTORY("4", "盘点"),
    ALLOCATION("5", "调拨"),
    INSERT("6", "新增"),
    UPDATE("7", "修改"),

    ;

    private final String k;
    private final String v;

    SampleLogOperationType(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }

    public static String getV(String k) {
        if (StringUtils.isEmpty(k)) {
            return null;
        }
        return Arrays.stream(SampleTypeEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleTypeEnum::getV)
                .findFirst()
                .orElse("");
    }
}
