package com.base.sbc.module.orderbook.vo;

import com.base.sbc.config.enums.business.StylePutIntoType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoResultType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Functions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class StyleSaleIntoDto {
    /* ----------------------------通用---------------------------- */

    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String bulkStyleNo;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private OrderBookChannelType channel;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /* ----------------------------详情---------------------------- */

    /**
     * 结果类型
     */
    @JsonIgnore
    @ApiModelProperty(value = "结果类型")
    private StyleSaleIntoResultType resultType;

    /**
     * 是否正价
     */
    @ApiModelProperty(value = "是否正价")
    @JsonIgnore
    private Boolean correctValue;

    /**
     * 款式投产类型
     */
    @ApiModelProperty(value = "款式投产类型")
    private StylePutIntoType type;

    /**
     * 尺码的投产和销售详细数据
     */
    @ApiModelProperty(value = "尺码的投产和销售详细数据")
    @JsonIgnore
    private Map<String, Double> sizeMap;

    /**
     * 合计
     */
    @JsonIgnore
    @ApiModelProperty(value = "合计")
    private Integer sum;
}
