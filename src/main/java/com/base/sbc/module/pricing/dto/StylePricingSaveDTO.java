package com.base.sbc.module.pricing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StylePricingSaveDTO {
    /**
     * 款式定价id
     */
    private String id;
    /**
     * 资料包id
     */
    @NotBlank(message = "资料包id不可为空")
    private String packId;
    /**
     * 企划倍率
     */
    private BigDecimal planningRate;

    /**
     * 是否计控确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控确认 0.否、1.是")
    private String controlConfirm;
    /**
     * 是否商品吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否商品吊牌确认 0.否、1.是")
    private String productHangtagConfirm;
    /**
     * 是否计控吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控吊牌确认 0.否、1.是")
    private String controlHangtagConfirm;

    /** 系列编码 */
    @ApiModelProperty(value = "系列编码"  )
    private String series;
    /** 系列名称 */
    @ApiModelProperty(value = "系列名称"  )
    private String seriesName;
    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;
    /** 目标成本 */
    @ApiModelProperty(value = "目标成本"  )
    private BigDecimal targetCost;

    /**
     * 计控确认成本时间
     */
    @ApiModelProperty(value = "计控确认成本时间")
    private Date controlConfirmTime;


}
