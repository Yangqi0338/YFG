package com.base.sbc.module.orderbook.vo;

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
    private Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> channelSizeMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Double> getSizeMap(){
        Map<String, Double> map = new HashMap<>(16);
        channelSizeMap.forEach((channel, sizeMapVo)-> {
            sizeMapVo.getNumSizeMap().forEach((key,value)-> {
                map.put(key+channel.getFill(),value);
            });
            sizeMapVo.getPercentageSizeMap().forEach((key,value)-> {
                map.put(key+channel.getPercentageFill(),value);
            });
        });
        return map;
    }

}
