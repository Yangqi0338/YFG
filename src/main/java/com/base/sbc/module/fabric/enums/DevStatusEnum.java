package com.base.sbc.module.fabric.enums;

public enum DevStatusEnum {
    UNPROCESSED("0", "未处理"),
    ADOPT("1", "通过"),
    FAIL("2", "失败"),
    ;
    private final String K;
    private final String V;

    DevStatusEnum(String k, String v) {
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
