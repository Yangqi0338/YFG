package com.base.sbc.module.pokaYoke.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.Getter;

/**
 * @Create : 2024/7/17 15:20
 **/
@Getter
public enum YokeExecuteLevelEnum {

    LEVEL_1("1","弱控"),
    LEVEL_2("2","强控"),

    ;

    @EnumValue
    private String level;

    private String desc;

    YokeExecuteLevelEnum(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }

}
