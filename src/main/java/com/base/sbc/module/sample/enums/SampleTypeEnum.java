package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleTypeEnum {
    Internal("1", "内部研发"),
    Outsourcing("2", "外采"),
    ODM_provides("3", "ODM提供"),
    ;

    private final String k;
    private final String v;

    SampleTypeEnum(String k, String v) {
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
