package com.base.sbc.module.orderbook.vo;

import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

@Data
public class OrderBookSimilarStyleVo extends StyleSaleIntoDto {

    /**
     * 款式Id
     */
    @ApiModelProperty(value = "款式Id")
    private String styleId;

    /**
     * 款式配色图
     */
    @ApiModelProperty(value = "款式配色图")
    private String styleColorPic;

    @ApiModelProperty(value = "设计款号"  )
    private String designNo;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    @JsonIgnore
    private List<OrderBookChannelType> channelList = new ArrayList<>();

    @ApiModelProperty(value = "参考款式配色图")
    private String referStyleColorPic;

    @ApiModelProperty(value = "参考款号")
    private String referBulkStyleNo;

    public String getChannelCode(){
        return channelList.stream().map(OrderBookChannelType::getCode).collect(Collectors.joining(COMMA));
    }

    public String getChannelName(){
        return channelList.stream().map(OrderBookChannelType::getText).collect(Collectors.joining(COMMA));
    }


    private Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> calculateSizeMap;

    @JsonAnyGetter
    public Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> getCalculateSizeMap(){
        return calculateSizeMap;
    }

    /**
     * 线上总投产
     */
    @ApiModelProperty(value = "线上总投产")
    public BigDecimal getOnlineTotalInto() {
        return BigDecimal.valueOf(
                calculateSizeMap.get(StyleSaleIntoCalculateResultType.INTO)
                        .getChannelSizeMap()
                        .get(OrderBookChannelType.ONLINE)
                        .getNumSizeMap()
                        .values()
                        .stream().mapToDouble(it-> it)
                        .sum()
        );
    };

    /**
     * 线上总销售
     */
    @ApiModelProperty(value = "线上总销售")
    public BigDecimal getOnlineTotalSale(){
        return BigDecimal.valueOf(
                calculateSizeMap.get(StyleSaleIntoCalculateResultType.SALE)
                        .getChannelSizeMap()
                        .get(OrderBookChannelType.ONLINE)
                        .getNumSizeMap()
                        .values()
                        .stream().mapToDouble(it-> it)
                        .sum()
        );
    };

    /**
     * 线下总投产
     */
    @ApiModelProperty(value = "线下总投产")
    public BigDecimal getOfflineTotalInto(){
        return BigDecimal.valueOf(
                calculateSizeMap.get(StyleSaleIntoCalculateResultType.INTO)
                        .getChannelSizeMap()
                        .get(OrderBookChannelType.OFFLINE)
                        .getNumSizeMap()
                        .values()
                        .stream().mapToDouble(it-> it)
                        .sum()
        );
    };

    /**
     * 线下总销售
     */
    @ApiModelProperty(value = "线下总销售")
    public BigDecimal getOfflineTotalSale(){
        return BigDecimal.valueOf(
                calculateSizeMap.get(StyleSaleIntoCalculateResultType.SALE)
                        .getChannelSizeMap()
                        .get(OrderBookChannelType.OFFLINE)
                        .getNumSizeMap()
                        .values()
                        .stream().mapToDouble(it-> it)
                        .sum()
        );
    };


    /**
     * 线下总产销
     */
    @ApiModelProperty(value = "线下总产销")
    public BigDecimal getOfflineTotalSaleInto(){
        return BigDecimalUtil.dividePercentage(getOfflineTotalSale(), getOfflineTotalInto());
    };

    /**
     * 线上总产销
     */
    @ApiModelProperty(value = "线上总产销")
    public BigDecimal getOnlineTotalSaleInto(){
        return BigDecimalUtil.dividePercentage(getOnlineTotalSale(), getOnlineTotalInto());
    };

    /**
     * 总投产
     */
    @ApiModelProperty(value = "总投产")
    public BigDecimal getTotalInto(){
        return BigDecimal.ZERO.add(getOnlineTotalInto()).add(getOfflineTotalInto());
    };

    /**
     * 总销售
     */
    @ApiModelProperty(value = "总销售")
    public BigDecimal getTotalSale(){
        return BigDecimal.ZERO.add(getOnlineTotalSale()).add(getOfflineTotalSale());
    };

    /**
     * 总产销
     */
    @ApiModelProperty(value = "总产销")
    public String getTotalSaleInto(){
        return BigDecimalUtil.dividePercentage(getTotalSale(), getTotalInto()).toString() + "%";
    }


    public String getReferStyleColorPic() {
        return getStyleColorPic();
    }

    public String getReferBulkStyleNo() {
        return getBulkStyleNo();
    }
}
