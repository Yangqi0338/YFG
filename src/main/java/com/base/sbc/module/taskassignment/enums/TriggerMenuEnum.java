package com.base.sbc.module.taskassignment.enums;

import lombok.Getter;

@Getter
public enum TriggerMenuEnum {

    CPJZL("产品季总览"),
    JSZXKB( "技术中心看板");

    private String value;

    TriggerMenuEnum(String value) {
        this.value = value;
    }

}
