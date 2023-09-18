package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 基础资料-物料档案-供应商报价-详情Vo
 */
@Data
public class BasicsdatumMaterialPriceDetailVo {

    /** 报价id */
    @ApiModelProperty(value = "报价id"  )
    private String priceId;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String width;
    /** 采购报价 */
    @ApiModelProperty(value = "采购报价"  )
    private BigDecimal quotationPrice;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String colorName;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String widthName;
    /** 供应商料号 */
    @ApiModelProperty(value = "供应商料号"  )
    private String supplierMaterialCode;
}
