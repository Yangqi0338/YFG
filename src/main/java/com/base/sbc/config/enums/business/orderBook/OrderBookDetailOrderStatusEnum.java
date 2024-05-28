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
public enum OrderBookDetailOrderStatusEnum {
    NOT_COMMIT("未下单"),
    PRODUCTION_FAILED("投产失败"),
    ORDERING("下单中"),
    PRODUCTION_IN("投产中"),
    ORDER("已下单"),
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

    OrderBookDetailOrderStatusEnum(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookDetailOrderStatusEnum findByCode(String code){
        return Arrays.stream(OrderBookDetailOrderStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    public boolean lessThan(OrderBookDetailOrderStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) < 0;
    }

    public boolean greatThan(OrderBookDetailOrderStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) > 0;
    }

}
