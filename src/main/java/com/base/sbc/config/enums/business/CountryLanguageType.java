package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum CountryLanguageType {
    TAG("吊牌",StandardColumnType.TAG_ROOT),

    WASHING("洗唛", StandardColumnType.WASHING_ROOT),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final StandardColumnType standardColumnType;

    @JsonValue
    public String getCode() {
        return code;
    }

    CountryLanguageType(String text, StandardColumnType standardColumnType) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
        this.standardColumnType = standardColumnType;
    }

    public static CountryLanguageType findByStandardColumnType(StandardColumnType type){
        return Arrays.stream(CountryLanguageType.values()).filter(it-> it.standardColumnType.equals(type) || it.standardColumnType.getChildrenTypeList().contains(type)).findFirst().orElse(null);
    }

    public static CountryLanguageType findByCode(String code){
        return Arrays.stream(CountryLanguageType.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

}
