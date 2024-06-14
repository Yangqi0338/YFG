package com.base.sbc.module.orderbook.vo;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class OrderBookSimilarStyleSizeMapVo {
    @JsonIgnore
    private StyleSaleIntoCalculateResultType resultType;
    @JsonIgnore
    private Map<String, Double> numSizeMap;
    @JsonIgnore
    private Map<String, Double> percentageSizeMap;

    public Map<String, Double> getPercentageSizeMap(){
        if (resultType != StyleSaleIntoCalculateResultType.SALE_INTO) {
            double sum = numSizeMap.values().stream().mapToDouble(it -> it).sum();
            numSizeMap.forEach((size, num)-> {
                percentageSizeMap.put(size, BigDecimalUtil.dividePercentage(num, sum));
            });
            return percentageSizeMap;
        }
        return numSizeMap;
    };


}
