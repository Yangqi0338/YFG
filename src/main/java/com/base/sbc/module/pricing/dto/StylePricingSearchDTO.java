package com.base.sbc.module.pricing.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StylePricingSearchDTO extends Page {
    private String companyCode;
    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;

    @ApiModelProperty(value = "品类id")
    private String prodCategory;

    @ApiModelProperty(value = "中类id")
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;

    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "生产模式")
    private String devtType;
    @ApiModelProperty(value = "波段")
    private String bandCode;
    @ApiModelProperty(value = "品名")
    private String productName;
    @ApiModelProperty(value = "款式")
    private String style;

    private String packId;

    private String month;
    private String season;
    private String year;
    private String tagPrice;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "设计款号")
    private String designNo;

}
