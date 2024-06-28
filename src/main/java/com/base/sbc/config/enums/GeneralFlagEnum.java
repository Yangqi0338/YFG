package com.base.sbc.config.enums;

import lombok.Getter;

@Getter
public enum GeneralFlagEnum {

    NO("0", "否"),
    YES("1", "是");

    private String code;
    private String value;

    GeneralFlagEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(String code) {
        for (GeneralFlagEnum value : GeneralFlagEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getValue();
            }
        }
        return "";
    }

}
