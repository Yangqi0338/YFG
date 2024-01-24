package com.base.sbc.config.enums.business.orderBook;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.base.sbc.config.enums.business.StandardColumnType;
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
public enum OrderBookDetailStatusEnum {
    NOT_COMMIT("未提交"),
    DESIGNER("分配设计师"),
    BUSINESS("分配商企"),
    NOT_AUDIT("待审核"),
    AUDIT("已审核"),
    AUDIT_SUSPEND("已驳回"),
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

    OrderBookDetailStatusEnum(String text) {
//        String code = this.name().toLowerCase();
//        this.code = StrUtil.toCamelCase(code);
        this.code = this.ordinal()+"";
        this.text = text;
    }

    public static OrderBookDetailStatusEnum findByCode(String code){
        return Arrays.stream(OrderBookDetailStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    public boolean lessThan(OrderBookDetailStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) < 0;
    }

    public boolean greatThan(OrderBookDetailStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) > 0;
    }

}
