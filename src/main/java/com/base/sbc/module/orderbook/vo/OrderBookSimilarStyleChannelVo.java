package com.base.sbc.module.orderbook.vo;

import com.alibaba.druid.sql.visitor.functions.If;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;

@Data
public class OrderBookSimilarStyleChannelVo {
    @JsonIgnore
    private StyleSaleIntoCalculateResultType resultType;

    @JsonIgnore
    private Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> channelSizeMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getSizeMap(){
        Map<String, String> map = new HashMap<>(16);
        channelSizeMap.forEach((channel, sizeMapVo)-> {
            sizeMapVo.getNumSizeMap().forEach((key,value)-> {
                map.put(key+channel.getFill(),value.toString() + (resultType == StyleSaleIntoCalculateResultType.SALE_INTO ? "%": ""));
            });
            sizeMapVo.getPercentageSizeMap().forEach((key,value)-> {
                map.put(key+channel.getPercentageFill(),value.toString() + "%");
            });
        });
        return map;
    }

}
