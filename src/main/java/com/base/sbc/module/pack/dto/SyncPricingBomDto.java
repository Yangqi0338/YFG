package com.base.sbc.module.pack.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("资料包-核价物料费用同步物料清单Dto SyncPricingBomDto")
public class SyncPricingBomDto {

    @ApiModelProperty(value = "主数据id")

    private String foreignId;

    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")

    private String packType;

    @ApiModelProperty(value = "物料id")
    private String bomId;
    @ApiModelProperty(value = "核价物料id")
    private String pricingBomId;




}
