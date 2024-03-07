package com.base.sbc.config.enums.business.orderBook;

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
public enum OrderBookOrderStatusEnum {
    NOT_COMMIT("待投产"),
    PART_ORDER("部分投产"),
    ORDER("已投产"),
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

    OrderBookOrderStatusEnum(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookOrderStatusEnum findByCode(String code){
        return Arrays.stream(OrderBookOrderStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

}
