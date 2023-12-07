package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum StandardColumnType implements IEnum<String> {
    TAG("吊牌&洗唛","DP"),
    TAG_ROOT("吊牌字段", "DP"),

    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final String preCode;

    @JsonValue
    public String getCode() {
        return code;
    }

    StandardColumnType(String text, String preCode) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
        this.preCode = preCode;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
