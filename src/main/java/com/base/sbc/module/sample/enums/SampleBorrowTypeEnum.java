package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleBorrowTypeEnum {
    INTERNAL_BORROW("1", "内部借出"),
    EXTERNAL_BORROW("2", "外部借出"),
    ;
    private final String k;
    private final String v;

    SampleBorrowTypeEnum(String k, String v) {
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
        return Arrays.stream(SampleBorrowTypeEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleBorrowTypeEnum::getV)
                .findFirst()
                .orElse("");
    }
}
