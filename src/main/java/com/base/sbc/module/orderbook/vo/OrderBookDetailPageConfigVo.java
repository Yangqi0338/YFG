package com.base.sbc.module.orderbook.vo;

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
    private String channel;
    private Set<String> sizeRange;
    private Integer sameDesignCount;



}
