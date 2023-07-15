package com.base.sbc.module.pricing.dto;

import com.base.sbc.module.pricing.enums.PricingCountTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@Data
public class PricingCountDTO {
    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 损耗
     */
    private BigDecimal lossRate;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 倍数
     */
    private BigDecimal multiple;

    /**
     * 计算类型
     *
     * @see PricingCountTypeEnum
     */
    @NotBlank(message = "计算类型不可为空")
    private String countType;
}
