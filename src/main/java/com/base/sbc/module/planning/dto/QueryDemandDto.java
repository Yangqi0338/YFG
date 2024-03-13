package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询需求dto QueryDemandDto")
public class QueryDemandDto {


    @ApiModelProperty(value = "产品季id", required = true, example = "111")
    private String planningSeasonId;

    @ApiModelProperty(value = "渠道", required = true)
    private String channel;

    @ApiModelProperty(value = "品类编码", required = true, example = "111")
    private String prodCategory;

    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    @ApiModelProperty(value = "表单编码", required = false, example = "122222")
    private String formCode;

    @ApiModelProperty(value = "维度字段id", required = false, example = "122222")
    private String fieldId;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "是否查询下单信息")
    private String orderInfo;

    @ApiModelProperty(value = "围度系数标识")
    private String coefficientFlag;
}
