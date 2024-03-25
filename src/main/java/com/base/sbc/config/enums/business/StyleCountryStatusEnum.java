package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum StyleCountryStatusEnum {
    UNCHECK("未审核"),
    CHECK("已审核"),
    MULTI_CHECK("已反审"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    @JsonValue
    public String getCode() {
        return code;
    }

    StyleCountryStatusEnum(String text) {
        this.code = this.ordinal()+"";
        this.text = text;

    }

    public static StyleCountryStatusEnum findByCode(String code){
        return Arrays.stream(StyleCountryStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

}
