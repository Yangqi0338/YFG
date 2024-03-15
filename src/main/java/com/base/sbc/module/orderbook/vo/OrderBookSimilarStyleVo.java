package com.base.sbc.module.orderbook.vo;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Data
public class OrderBookSimilarStyleVo extends StyleSaleIntoDto {

    /**
     * 款式配色图
     */
    @ApiModelProperty(value = "款式配色图")
    private String styleColorPic;

    public String getChannelName(){
        return Opt.ofNullable(this.getChannel()).map(OrderBookChannelType::getText).orElse("");
    }

    private Map<StyleSaleIntoCalculateResultType, Map<String, Double>> calculateSizeMap;

    @JsonAnyGetter
    public Map<StyleSaleIntoCalculateResultType, Map<String, Double>> getCalculateSizeMap(){
        return calculateSizeMap;
    }

    /**
     * 总投产
     */
    @ApiModelProperty(value = "总投产")
    private BigDecimal totalInto = BigDecimal.ZERO;

    /**
     * 总销售
     */
    @ApiModelProperty(value = "总销售")
    private BigDecimal totalSale = BigDecimal.ZERO;

    /**
     * 总产销
     */
    @ApiModelProperty(value = "总产销")
    public BigDecimal getTotalSaleInto(){
        return BigDecimalUtil.dividePercentage(totalSale, totalInto);
    }


}
