package com.base.sbc.module.orderbook.vo;

import cn.hutool.core.lang.Opt;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBookSimilarStyleVo extends StyleSaleIntoDto {

    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;

    public String getChannelName(){
        return Opt.ofNullable(this.getChannel()).map(OrderBookChannelType::getText).orElse("");
    }

    /**
     * 总投产
     */
    @ApiModelProperty(value = "总投产")
    private Integer totalInto;

    /**
     * 总销售
     */
    @ApiModelProperty(value = "总销售")
    private Integer totalSale;

    /**
     * 总产销
     */
    @ApiModelProperty(value = "总产销")
    private Integer totalSaleInto;

}
