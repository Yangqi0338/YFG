package com.base.sbc.module.basicsdatum.enums;

public enum BasicsdatumMaterialBizTypeEnum {
    MATERIAL("material", "物料"),
    DEV("dev", "面料开发"),
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
