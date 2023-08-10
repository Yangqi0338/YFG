package com.base.sbc.client.amc.enums;

public enum DataPermissionsSelectTypeEnum {
    AND("and", "并且"),
    OR("or", "或者"),
    ;
    private final String K;
    private final String V;

    DataPermissionsSelectTypeEnum(String k, String v) {
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
