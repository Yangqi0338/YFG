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
public enum OrderBookDetailOrderActionType {
    /* 需要调用下游投产接口 */
    FINISH("完成下单"),
    /* 下游投产失败 */
    FAILED("下单失败"),
    /* 需要调用下游取消接口 */
    CANCEL("下单取消"),
    /* 下游取消失败 */
    CANCEL_FAILED("下单取消失败"),
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

    OrderBookDetailOrderActionType(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookDetailOrderActionType findByCode(String code){
        return Arrays.stream(OrderBookDetailOrderActionType.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

}
