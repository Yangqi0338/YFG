package com.base.sbc.module.pricing.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * 款式定价
 */
@Data
public class StylePricingVO {
    private String stylePricingId;
    /**
     * 资料包id
     */
    private String id;
    private String packType;

    private String ingredient;

    private String downContent;

    private String styleId;

    /**
     * 计控确认成本时间
     */
    @ApiModelProperty(value = "计控确认成本时间")
    private Date controlConfirmTime;

    /**
     * 样衣图片
     */
    @ApiModelProperty(value = "样衣图片")
    private String styleColorPic;
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
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
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

    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }

    @ApiModelProperty(value = "款式名称")
    private String styleName;
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
     * 加工费
     */
    @ApiModelProperty(value = "加工费")
    private BigDecimal processingFee;
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
     * 二次加工费
     */
    @ApiModelProperty(value = "二次加工费")
    private BigDecimal secondaryProcessingFee;
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
    private BigDecimal actualMagnification;
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


    @ApiModelProperty(value = "系列编码")
    private String series;
    /**
     * 系列名称
     */
    @ApiModelProperty(value = "系列名称")
    private String seriesName;


    @ApiModelProperty(value = "产品风格编码产品风格编码")
    private String productStyleName;

    private String calcItemVal;

}

