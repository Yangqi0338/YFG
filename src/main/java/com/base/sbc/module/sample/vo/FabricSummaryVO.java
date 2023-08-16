package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 面料汇总视图类
 * @author lizan
 * @date 2023-07-13 17:47
 */
@Data
@ApiModel("面料汇总")
public class FabricSummaryVO {

    @ApiModelProperty(value = "订货本与配色中间id")
    private String id;

    @ApiModelProperty(value = "面料实物图")
    private String imageUrl;

    @ApiModelProperty(value = "材料")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "询价编号")
    private String inquiryNumber;

    @ApiModelProperty(value = "裁数")
    private String cuttingNumber;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "材料成分")
    private String ingredient;

    @ApiModelProperty(value = "面料克重")
    private String gramWeight;

    @ApiModelProperty(value = "纱织规格")
    private String specification;

    @ApiModelProperty(value = "密度")
    private String density;

    @ApiModelProperty(value = "起订量")
    private String minimumOrderQuantity;

    @ApiModelProperty(value = "货期")
    private String deliveryName;

    @ApiModelProperty(value = "有效门幅")
    private String widthList;

    @ApiModelProperty(value = "含税价格")
    private String supplierQuotationPrice;



}
