package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/*用于bom模板新增物料*/
@Data
public class AddRevampBomTemplateMaterialDto {


    @ApiModelProperty(value = "bom模板物料id"  )
    private String id;
    /** BOM模板id */
    @ApiModelProperty(value = "BOM模板id"  )
    private String bomTemplateId;
    /** 物料档案id */
    @ApiModelProperty(value = "物料档案id"  )
    private String materialId;
    /** 搭配名称 */
    @ApiModelProperty(value = "搭配名称"  )
    private String collocationCode;
    /** 搭配编码 */
    @ApiModelProperty(value = "搭配编码"  )
    private String collocationName;
    /** 主材料标识(0否,1是) */
    @ApiModelProperty(value = "主材料标识(0否,1是)"  )
    private String mainFlag;
    /** 物料图片 */
    @ApiModelProperty(value = "物料图片"  )
    private String imageUrl;
    /** 材料 */
    @ApiModelProperty(value = "材料"  )
    private String materialCodeName;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商厂家成分 */
    @ApiModelProperty(value = "供应商厂家成分"  )
    private String supplierFactoryIngredient;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String ingredient;
    /** 供应商报价 */
    @ApiModelProperty(value = "供应商报价"  )
    private BigDecimal supplierPrice;
    /** 供应商物料号 */
    @ApiModelProperty(value = "供应商物料号"  )
    private String supplierMaterialCode;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 部位编码 */
    @ApiModelProperty(value = "部位编码"  )
    private String partCode;
    /** 部位名称 */
    @ApiModelProperty(value = "部位名称"  )
    private String partName;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unitCode;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String purchaseUnitCode;
    /** 采购单位名称 */
    @ApiModelProperty(value = "采购单位名称"  )
    private String purchaseUnitName;
    /** 库存单位 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnitCode;
    /** 库存单位名称 */
    @ApiModelProperty(value = "库存单位名称"  )
    private String stockUnitName;
    /** 辅料材质 */
    @ApiModelProperty(value = "辅料材质"  )
    private String auxiliaryMaterial;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String color;
    /** 颜色hex */
    @ApiModelProperty(value = "颜色hex"  )
    private String colorHex;
    /** 颜色代码 */
    @ApiModelProperty(value = "颜色代码"  )
    private String colorCode;
    /** 颜色图片 */
    @ApiModelProperty(value = "颜色图片"  )
    private String colorPic;
    /** 厂家有效门幅/规格 */
    @ApiModelProperty(value = "厂家有效门幅/规格"  )
    private String translate;
    /** 厂家有效门幅/规格编码 */
    @ApiModelProperty(value = "厂家有效门幅/规格编码"  )
    private String translateCode;
    /** 单件用量 */
    @ApiModelProperty(value = "单件用量"  )
    private BigDecimal unitUse;
    /** 损耗% */
    @ApiModelProperty(value = "损耗%"  )
    private BigDecimal lossRate;
    /** 成本 */
    @ApiModelProperty(value = "成本"  )
    private BigDecimal cost;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String scmSendFlag;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
