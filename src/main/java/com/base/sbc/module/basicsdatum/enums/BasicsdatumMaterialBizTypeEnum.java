package com.base.sbc.module.basicsdatum.enums;

public enum BasicsdatumMaterialBizTypeEnum {
    MATERIAL("material", "物料"),
    DEV_APPLY("devApply", "面料开发申请"),
    DEV("dev", "面料开发"),
    FABRIC_LIBRARY("fabricLibrary", "面料基础库"),
    ;

    private final String K;
    private final String V;

    BasicsdatumMaterialBizTypeEnum(String k, String v) {
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
