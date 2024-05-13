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
public enum OrderBookDetailAuditStatusEnum {
    NOT_COMMIT("未审核"),
    AWAIT("待审核"),
    FINISH("已审核"),
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

    OrderBookDetailAuditStatusEnum(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookDetailAuditStatusEnum findByCode(String code){
        return Arrays.stream(OrderBookDetailAuditStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    public boolean lessThan(OrderBookDetailAuditStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) < 0;
    }

    public boolean greatThan(OrderBookDetailAuditStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) > 0;
    }

}
