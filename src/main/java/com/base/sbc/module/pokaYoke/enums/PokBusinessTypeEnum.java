package com.base.sbc.module.pokaYoke.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PokBusinessTypeEnum {

    BOM_UNIT_USE("1","BOM设计用量"),


    ;

    @EnumValue
    private String type;

    private String desc;

    PokBusinessTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
