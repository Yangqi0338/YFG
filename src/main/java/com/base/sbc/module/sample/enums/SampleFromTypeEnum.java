package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleFromTypeEnum {
    ADD("1", "新增"),
    IMPORT2("2", "导入"),
    EXTERNAL("3", "外部"),
    ;

    private final String k;
    private final String v;

    SampleFromTypeEnum(String k, String v) {
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
        return Arrays.stream(SampleFromTypeEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleFromTypeEnum::getV)
                .findFirst()
                .orElse("");
    }
}
