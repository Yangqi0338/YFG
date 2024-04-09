package com.base.sbc.module.orderbook.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class OrderBookDetailPageVo extends PageInfo<OrderBookDetailVo> {

    private Map<String,Double> totalMap;

    @JsonAnyGetter
    public Map<String,Double> getTotalMap(){
        return totalMap;
    }

}
