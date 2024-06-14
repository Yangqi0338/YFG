package com.base.sbc.config.enums.business.replay;

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
public enum ReplayRatingType {
    /**/
    SALE("销售复盘"),
    PATTERN("版型复盘"),
    FABRIC("面料复盘"),
    COLOR("颜色复盘"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    ReplayRatingType(String text) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }

    public static ReplayRatingType findByCode(String code) {
        return Arrays.stream(ReplayRatingType.values()).filter(it -> it.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

}
