package com.base.sbc.client.ccm.enums;

/**
 * CCM配置key
 * @author lizan
 * @date 2023-08-26 16:22
 */
public enum CcmBaseSettingEnum {
    // 单款多色开关
    STYLE_MANY_COLOR("STYLE_MANY_COLOR","单款多色开关")
    ;
    private final String keyCode;

    private final String keyName;


    CcmBaseSettingEnum(String keyCode, String keyName) {
        this.keyCode = keyCode;
        this.keyName = keyName;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public String getKeyName() {
        return keyName;
    }
}
