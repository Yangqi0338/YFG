package com.base.sbc.client.amc.enums;

public enum DataPermissionsConditionTypeEnum {
    IN("in", "包含"),
    EQ("=", "等于"),
    ;
    private final String K;
    private final String V;

    DataPermissionsConditionTypeEnum(String k, String v) {
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
