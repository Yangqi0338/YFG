package com.base.sbc.module.orderbook.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code 描述：销售投产类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/3/15
 */
@Getter
@AllArgsConstructor
public enum StyleSaleIntoCalculateResultType {
    SALE("销售","sell"),
    INTO("投产","startup"),
    SALE_INTO("产销","marketing"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    private final String prefix;




    @JsonValue
    public String getCode() {
        return code;
    }




    StyleSaleIntoCalculateResultType(String text, String prefix) {
        this.prefix = prefix;
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
//        this.code = this.ordinal()+"";
        this.text = text;
    }
}
