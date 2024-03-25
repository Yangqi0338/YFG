package com.base.sbc.config.enums.business.orderBook;

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
public enum OrderBookStatusEnum {
    NOT_COMMIT("待提交"),
    NOT_CONFIRM("待审核"),
    PART_CONFIRM("部分审核"),
    CONFIRM("已审核"),
    SUSPEND("已驳回"),
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

    OrderBookStatusEnum(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookStatusEnum findByCode(String code){
        return Arrays.stream(OrderBookStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

}
