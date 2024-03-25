package com.base.sbc.module.orderbook.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@code 描述：销售投产类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/3/15
 */
@Getter
@AllArgsConstructor
public enum StyleSaleIntoResultType {
    OFFLINE_SALE("线下销售"),
    ONLINE_SALE("线上销售"),
    FIRST_INTO("线上/线下首单投产"),
    APPEND_INTO("线上/线下追单投产"),
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

    StyleSaleIntoResultType(String text) {
        this.code = this.name().toLowerCase();
//        this.code = this.ordinal()+"";
        this.text = text;
    }
}
