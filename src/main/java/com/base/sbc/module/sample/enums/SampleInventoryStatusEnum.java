package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleInventoryStatusEnum {
    NOT_STARTED("0", "未开始"),
    INVENTORY_IN_PROGRESS("1", "盘点中"),
    COMPLETE("2", "完成"),
    OVERDUE("3", "过期"),
    ;

    private final String k;
    private final String v;

    SampleInventoryStatusEnum(String k, String v) {
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
        return Arrays.stream(SampleInventoryStatusEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleInventoryStatusEnum::getV)
                .findFirst()
                .orElse("");
    }
}
