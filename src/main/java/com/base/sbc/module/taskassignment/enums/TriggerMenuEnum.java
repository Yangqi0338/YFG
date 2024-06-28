package com.base.sbc.module.taskassignment.enums;

import com.base.sbc.config.enums.GeneralFlagEnum;
import lombok.Getter;

@Getter
public enum TriggerMenuEnum {

    CPJZL("产品季总览"),
    JSZXKB( "技术中心看板");

    private String value;

    TriggerMenuEnum(String value) {
        this.value = value;
    }

    public static String checkValue(String value) {
        for (TriggerMenuEnum item : TriggerMenuEnum.values()) {
            if (item.getValue().equals(value)) {
                return item.getValue();
            }
        }
        return "";
    }

}
