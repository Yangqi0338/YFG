package com.base.sbc.module.orderbook.vo;

import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class OrderBookDetailPageConfigVo {

    @JsonIgnore
    private OrderBookChannelType channel;
    private List<String> sizeRange;
    private Integer sameDesignCount;



}
