package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceiveDesignDTO {

    @ApiModelProperty("工单号")
    private String productNumber;

    @ApiModelProperty("商品款号")
    private String itemNumber;

    @ApiModelProperty("版次")
    private String versionName;
    @ApiModelProperty("设计确认结果 11-通过 12-不通过")
    private String designResult;

    @ApiModelProperty("改版意见")
    private String designSuggestion;

    /**
     * [{"name":"图片1name","url":"图片1ulr"},{"name":"图片2name","url":"图片2ulr"},{"name":"图片3name","url":"图片3ulr"}]
     */
    @ApiModelProperty("图片json")
    private String designPic;

    /**
     * {"name":"视频name","url":"视频ulr"}
     */
    @ApiModelProperty("视频json")
    private String designAv;

}
