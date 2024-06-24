package com.base.sbc.module.customFile.enums;

import com.base.sbc.config.exception.OtherException;

import java.util.List;
import java.util.function.Function;

import lombok.Getter;

@Getter
public enum FileBusinessType {

    material("1","素材库相关"),
    material_collect("2","素材收藏相关");

    private final String code;

    private final String desc;

    FileBusinessType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FileBusinessType getByCode(String code){
        for (FileBusinessType value : FileBusinessType.values()) {
            if (value.code.equalsIgnoreCase(code)){
                return value;
            }
        }
        throw new OtherException("创建文件类型不存在：" + code);
    }

    public void check(List<String> ids, Function<List<String>, Boolean > function) {

        Boolean apply = function.apply(ids);
        if (apply){
            throw new OtherException("删除的文件夹关联有数据，暂不能删除");
        }

    }
}
