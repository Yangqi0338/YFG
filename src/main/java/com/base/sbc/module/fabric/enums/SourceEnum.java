package com.base.sbc.module.fabric.enums;

public enum SourceEnum {

    INSERT("1", "新增"),

    BASIC_FABRIC_LIBRARY("2", "基础面料库"),

    FABRIC_DEV_APPLICATION("3", "面料开发申请"),

    MATERIAL("4", "物料档案"),
    ;

    private final String K;
    private final String V;

    SourceEnum(String k, String v) {
        K = k;
        V = v;
    }

    public String getK() {
        return K;
    }

    public String getV() {
        return V;
    }
}
