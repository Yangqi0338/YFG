/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：款式管理-面料汇总 实体类
 * @address com.base.sbc.module.fabricsummary.entity.FabricSummary
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-28 15:25:40
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_summary")
@ApiModel("款式管理-面料汇总 FabricSummary")
public class FabricSummary extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 汇总编码 */
    @ApiModelProperty(value = "汇总编码"  )
    private String fabricSummaryCode;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 材料 */
    @ApiModelProperty(value = "材料"  )
    private String materialCodeName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址"  )
    private String imageUrl;
    /** 材料类型 */
    @ApiModelProperty(value = "材料类型"  )
    private String materialCategory;
    /** 材料类型名称 */
    @ApiModelProperty(value = "材料类型名称"  )
    private String materialCategoryName;
    /** 规格名称 */
    @ApiModelProperty(value = "规格名称"  )
    private String widthName;
    /** 面料成分 */
    @ApiModelProperty(value = "面料成分"  )
    private String ingredient;
    /** 默认供应商名称 */
    @ApiModelProperty(value = "默认供应商名称"  )
    private String supplierName;
    @ApiModelProperty(value = "默认供应商简称"  )
    private String supplierAbbreviation;
    /** 默认供应商id */
    @ApiModelProperty(value = "默认供应商id"  )
    private String supplierId;
    /** 供应商物料编号 */
    @ApiModelProperty(value = "供应商物料编号"  )
    private String supplierFabricCode;
    /** 供应商报价 */
    @ApiModelProperty(value = "供应商报价"  )
    private BigDecimal supplierQuotationPrice;
    /** 生产周期 */
    @ApiModelProperty(value = "生产周期-期货"  )
    private BigDecimal productionDay;
    /** 供应商色号 */
//    @ApiModelProperty(value = "供应商色号"  )
//    private String supplierColorNo;
    /** 供应商颜色描述 */
    @ApiModelProperty(value = "供应商颜色描述"  )
    private String supplierColorSay;
    /** 供应商面料成分 */
    @ApiModelProperty(value = "供应商面料成分"  )
    private String supplierIngredient;
    /** 供应商厂家成分 */
    @ApiModelProperty(value = "供应商厂家成分"  )
    private String supplierFactoryIngredient;
    /** 面料成分说明 */
    @ApiModelProperty(value = "面料成分说明"  )
    private String ingredientSay;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer minimumOrderQuantity;
    /** 理化检测结果（1是0否) */
    @ApiModelProperty(value = "理化检测结果（1是0否)"  )
    private String physicochemistryDetectionResult;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private String gramWeight;
    /** 物料属性 */
    @ApiModelProperty(value = "物料属性"  )
    private String materialType;
    /** 试穿结果 */
    @ApiModelProperty(value = "试穿结果 0不合适，1合适"  )
    private String fittingResult;
    /** 询价编号 */
    @ApiModelProperty(value = "询价编号"  )
    private String enquiryCode;
    /** 年份尾缀 */
    @ApiModelProperty(value = "年份尾缀"  )
    private String yearSuffix;
    @ApiModelProperty(value = "持续环保（1是 0否 空白）")
    private String isProtection;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String specification;

    @ApiModelProperty(value = "版本号"  )
    private Integer fabricSummaryVersion;

    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;

    @ApiModelProperty(value = "组id"  )
    private String groupId;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
