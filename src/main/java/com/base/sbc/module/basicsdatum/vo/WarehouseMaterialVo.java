package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarehouseMaterialVo {
    private String id;
    /** 物料图片 */
    @ApiModelProperty(value = "物料图片"  )
    private String imageUrl;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 材料类型 */
    @ApiModelProperty(value = "材料类型"  )
    private String materialCategoryName;
    /** 损耗 */
    @ApiModelProperty(value = "损耗"  )
    private String lossRate;
    /** 供应商报价 */
    @ApiModelProperty(value = "供应商报价"  )
    private String supplierQuotationPrice;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String width;
    /** 规格编码 */
    @ApiModelProperty(value = "规格编码"  )
    private String widthCode;
    /** 采购单位编码 */
    @ApiModelProperty(value = "采购单位编码"  )
    private String purchaseUnitCode;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位名称"  )
    private String purchaseUnitName;
    /** 库存单位编码 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnitCode;
    /** 库存单位名称 */
    @ApiModelProperty(value = "库存单位名称"  )
    private String stockUnitName;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String ingredient;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColorCode;
}
