package com.base.sbc.client.amc.enums;

public enum DataPermissionsRangeEnum {
    ALL("1", "全部"),
    CUSTOM("2", "自定义"),
    ALL_INOPERABLE("3", "全部不可操作"),
    ;
    private final String K;
    private final String V;

    DataPermissionsRangeEnum(String k, String v) {
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
