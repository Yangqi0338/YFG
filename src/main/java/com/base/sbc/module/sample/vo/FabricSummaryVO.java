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

    @ApiModelProperty(value = "订货本id")
    private String bookId;

    @ApiModelProperty(value = "订货本编号")
    private String orderBookCode;

    @ApiModelProperty(value = "款式(大货款号)")
    private String styleNo;

    @ApiModelProperty(value = "设计款号")
    private String designNo;


    @ApiModelProperty(value = "上会标记(0未上会，1已上会)")
    private String meetFlag;


    @ApiModelProperty(value = "锁定标记(0未锁定，1已锁定)")
    private String lockFlag;


    @ApiModelProperty(value = "物料id")
    private String materialId;


    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料图片")
    private String imageUrl;

    @ApiModelProperty(value = "类别名称（物料分类名称）")
    private String categoryName;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商料号")
    private String supplierMaterialCode;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "厂家成分")
    private String factoryComposition;

    @ApiModelProperty(value = "部位")
    private String part;

    @ApiModelProperty(value = "门幅")
    private String translate;


    @ApiModelProperty(value = "颜色")
    private String color;


    @ApiModelProperty(value = "备注")
    private String remarks;


    @ApiModelProperty(value = "单位名称")
    private String purchaseUnitName;

    @ApiModelProperty(value = "单价")
    private String price;

    @ApiModelProperty(value = "损耗率")
    private String lossRate;

    @ApiModelProperty(value = "成本")
    private String cost;

    @ApiModelProperty(value = "供应商报价")
    private String supplierPrice;

    @ApiModelProperty(value = "设计款号")
    private String designNoTwo;


}
