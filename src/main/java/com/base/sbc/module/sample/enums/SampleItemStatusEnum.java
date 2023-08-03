package com.base.sbc.module.sample.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum SampleItemStatusEnum {
    NOT_RECEIVED("0", "未入库", "存在未入库数据"),
    IN_LIBRARY("1", "在库", "存在未在库数据"),
    LENDING("2", "借出", "存在借出数据"),
    DEL("3", "删除", ""),
    SOLD("4", "销售", ""),
    INVENTORY_IN_PROGRESS("5", "盘点中", "存在 不在盘点中数据"),
    ;

    private final String k;
    private final String v;
    private final String tips;

    SampleItemStatusEnum(String k, String v, String tips) {
        this.k = k;
        this.v = v;
        this.tips = tips;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }

    public String getTips() {
        return tips;
    }

    public static String getTips(String k) {
        if (StringUtils.isEmpty(k)) {
            return null;
        }
        return Arrays.stream(SampleItemStatusEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleItemStatusEnum::getTips)
                .findFirst()
                .orElse("未定义错误");
    }

    public static String getV(String k) {
        if (StringUtils.isEmpty(k)) {
            return null;
        }
        return Arrays.stream(SampleItemStatusEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(SampleItemStatusEnum::getV)
                .findFirst()
                .orElse("");
    }
}
