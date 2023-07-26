package com.base.sbc.module.pricing.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 款式定价
 */
@Data
public class StylePricingVO {
    /**
     * 资料包id
     */
    private String id;
    private String packType;
    /**
     * 样衣图片
     */
    @ApiModelProperty(value = "样衣图片")
    private String sampleDesignPic;
    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    private String code;
    /**
     * 中分类
     */
    @ApiModelProperty(value = "中分类")
    private String middleClassif;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 色号
     */
    @ApiModelProperty(value = "色号")
    private String colourCode;
    /**
     * 是否上会
     */
    @ApiModelProperty(value = "是否上会")
    private String meetFlag;
    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    private String style;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;
    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    private String productName;
    /**
     * BOM阶段
     */
    @ApiModelProperty(value = "BOM阶段")
    private String bomStage;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 供应商简称
     */
    @ApiModelProperty(value = "供应商简称")
    private String supplierAbbreviation;
    /**
     * 目标成本
     */
    @ApiModelProperty(value = "目标成本")
    private String targetCost;
    /**
     * 材料成本
     */
    @ApiModelProperty(value = "材料成本")
    private BigDecimal materialCost;
    /**
     * 包装费
     */
    @ApiModelProperty(value = "包装费")
    private BigDecimal packagingFee;
    /**
     * 检测费
     */
    @ApiModelProperty(value = "检测费")
    private BigDecimal testingFee;
    /**
     * 车缝加工费
     */
    @ApiModelProperty(value = "车缝加工费")
    private BigDecimal sewingProcessingFee;
    /**
     * 毛纱加工费
     */
    @ApiModelProperty(value = "毛纱加工费")
    private BigDecimal woolenYarnProcessingFee;
    /**
     * 外协加工费
     */
    @ApiModelProperty(value = "外协加工费")
    private BigDecimal coordinationProcessingFee;
    /**
     * 总成本
     */
    @ApiModelProperty(value = "总成本")
    private BigDecimal totalCost;
    /**
     * 企划倍率
     */
    @ApiModelProperty(value = "企划倍率")
    private BigDecimal planningRatio;
    /**
     * 预计销售价
     */
    @ApiModelProperty(value = "预计销售价")
    private BigDecimal expectedSalesPrice;
    /**
     * 计控实际成本
     */
    @ApiModelProperty(value = "计控实际成本")
    private BigDecimal planCost;
    /**
     * 是否计控成本确认
     */
    @ApiModelProperty(value = "是否计控成本确认")
    private String planCostConfirm;
    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;
    /**
     * 吊牌是否打印
     */
    @ApiModelProperty(value = "吊牌是否打印")
    private String tagPrintFlag;
    /**
     * 商品吊牌价是否确认
     */
    @ApiModelProperty(value = "商品吊牌价是否确认")
    private String productTagPriceConfirm;
    /**
     * 计控吊牌价是否确认
     */
    @ApiModelProperty(value = "计控吊牌价是否确认")
    private String planTagPriceConfirm;
    /**
     * 计控实际倍率
     */
    @ApiModelProperty(value = "计控实际倍率")
    private BigDecimal planActualMagnification;
    /**
     * 实际倍率
     */
    @ApiModelProperty(value = "实际倍率")
    private String actualMagnification;
    /**
     * 产品风格
     */
    @ApiModelProperty(value = "产品风格")
    private String productStyle;
    /**
     * 设计师
     */
    @ApiModelProperty(value = "设计师")
    private String designer;
    /**
     * 款式类型
     */
    @ApiModelProperty(value = "款式类型")
    private String styleType;
    /**
     * 生产类型
     */
    @ApiModelProperty(value = "生产类型")
    private String productionType;
}
