package com.base.sbc.module.pack.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：资料包-物料清单-选择物料档案vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackBomVersionVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 16:45
 */
@Data
@ApiModel("资料包-物料清单-选择物料档案vo BomSelMaterialVo")
public class BomSelMaterialVo {
    /**
     * 物料档案id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 物料档案id
     */
    @ApiModelProperty(value = "物料档案id")
    private String materialId;
    /**
     * 大类编码
     */
    @ApiModelProperty(value = "大类编码")
    private String category1Code;
    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    private String category1Name;
    /**
     * 搭配
     */
    @ApiModelProperty(value = "搭配")
    private String categoryName;
    /**
     * 材料
     */
    @ApiModelProperty(value = "材料")
    private String materialCodeName;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /**
     * 厂家成分
     */
    @ApiModelProperty(value = "厂家成分")
    private String supplierFactoryIngredient;
    @ApiModelProperty(value = "辅料成分")
    private String auxiliaryMaterial;
    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredient;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 门幅/规格（通用）
     */
    @ApiModelProperty(value = "门幅/规格（通用）编码")
    private String translateCode;

    @ApiModelProperty(value = "门幅/规格（通用）名称")
    private String translate;

    @ApiModelProperty(value = "门幅")
    private String translateDs;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String color;
    /**
     * 颜色hex
     */
    @ApiModelProperty(value = "颜色hex")
    private String colorHex;
    /**
     * 颜色代码
     */
    @ApiModelProperty(value = "颜色代码")
    private String colorCode;
    /**
     * 颜色图片
     */
    @ApiModelProperty(value = "颜色图片")
    private String colorPic;
    /**
     * 物料图片
     */
    @ApiModelProperty(value = "物料图片")
    private String imageUrl;
    /**
     * 单件用量
     */
    @ApiModelProperty(value = "单件用量")
    private BigDecimal unitUse;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;
    @ApiModelProperty(value = "克重")
    private String gramWeight;
    /**持续环保（1是 0否 空白） */
    @ApiModelProperty(value = "持续环保（1是 0否 空白）")
    private String isProtection;
    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    /**
     * 供应商报价
     */
    @ApiModelProperty(value = "供应商报价")
    private BigDecimal supplierPrice;
    /**
     * 供应商物料号
     */
    @ApiModelProperty(value = "供应商物料号")
    private String supplierMaterialCode;
    /**
     * 状态(0停用,1启用)
     */
    @ApiModelProperty(value = "状态(0停用,1启用)")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 采购单位
     */
    @ApiModelProperty(value = "采购单位")
    private String purchaseUnitCode;
    /**
     * 采购单位名称
     */
    @ApiModelProperty(value = "采购单位名称")
    private String purchaseUnitName;
    /**
     * 库存单位
     */
    @ApiModelProperty(value = "库存单位")
    private String stockUnitCode;
    /**
     * 库存单位名称
     */
    @ApiModelProperty(value = "库存单位名称")
    private String stockUnitName;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "规格编码")
    private String widthCode;

    @ApiModelProperty(value = "规格名称")
    private String widthName;

    @ApiModelProperty(value = "货期名称")
    private String deliveryName;
    @ApiModelProperty(value = "货期编码")
    private String deliveryCode;
    /**
     * 数据来源：1.新增、2.面料企划、3.其他
     */
    @ApiModelProperty(value = "数据来源：1.新增、2.面料企划、3.其他")
    private String source;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 库存数量
     * */
    private BigDecimal stockQuantity;
}
