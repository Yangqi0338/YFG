package com.base.sbc.config.enums.business.orderBook;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.base.sbc.config.enums.business.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code 描述：订货本渠道类型}
 * @author KC
 * @since 2024/3/7
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Getter
@AllArgsConstructor
public enum OrderBookChannelType {
    OFFLINE("线下", "00", "0"),
    ONLINE("线上","1", "2"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final String fill;
    private final String percentageFill;

    @JsonValue
    public String getCode() {
        return code;
    }

    OrderBookChannelType(String text, String fill, String percentageFill) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
//        this.code = this.ordinal()+"";
        this.text = text;
        this.fill = fill;
        this.percentageFill = percentageFill;
    }

}
