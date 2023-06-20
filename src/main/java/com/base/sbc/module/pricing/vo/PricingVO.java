/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 类描述：核价
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("核价")
public class PricingVO extends BaseDataEntity<String> {

    private static final long serialVersionUID = -1428745406044003803L;

    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String code;
    /**
     * 报价日期
     */
    @ApiModelProperty(value = "报价日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteDate;

    /**
     * 报价人
     */
    @ApiModelProperty(value = "报价人")
    private String quoteUserId;
    /**
     * 报价人名称
     */
    @ApiModelProperty(value = "报价人名称")
    private String quoteUserName;

    /**
     * 来源类型
     *
     * @see com.base.sbc.module.pricing.enums.PricingSourceTypeEnum
     */
    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    /**
     * 来源id
     */
    @ApiModelProperty(value = "来源id")
    private String sourceId;
    /**
     * 来源单号
     */
    @ApiModelProperty(value = "来源单号")
    private String sourceCode;

    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String styleCode;

    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designStyleCode;
    /**
     * 客款号
     */
    @ApiModelProperty(value = "客款号")
    private String customerStyleCode;
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String customId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customName;

    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private BigDecimal orderNum;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 合作方式
     */
    @ApiModelProperty(value = "合作方式")
    private String cooperationMethods;
    /**
     * 付款方式
     */
    @ApiModelProperty(value = "付款方式")
    private String payMethods;

    /**
     * 计划下单时间
     */
    @ApiModelProperty(value = "计划下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planBillDate;
    /**
     * 交货日期
     */
    @ApiModelProperty(value = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date giveDate;
    /**
     * 生产周期
     */
    @ApiModelProperty(value = "生产周期")
    private BigDecimal yieldDay;
    /**
     * 面料系数
     */
    @ApiModelProperty(value = "面料系数")
    private String fabricCoefficient;
    /**
     * 品质要求
     */
    @ApiModelProperty(value = "品质要求")
    private String qualityRequirements;
    /**
     * 查货要求
     */
    @ApiModelProperty(value = "查货要求")
    private String checkGoods;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String category;
    /**
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    private String quoteDesc;
    /**
     * 销售渠道id(多个逗号分隔)
     */
    @ApiModelProperty(value = "销售渠道id(多个逗号分隔)")
    private String salesChannelId;
    /**
     * 销售渠道名称(多个逗号分隔)
     */
    @ApiModelProperty(value = "销售渠道名称(多个逗号分隔)")
    private String salesChannelName;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 货币2
     */
    @ApiModelProperty(value = "货币2")
    private String otherCurrency;
    /**
     * 汇率
     */
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;
    /**
     * 物料费用
     */
    @ApiModelProperty(value = "物料费用")
    private BigDecimal materialCost;
    /**
     * 加工费
     */
    @ApiModelProperty(value = "加工费")
    private BigDecimal processCost;
    /**
     * 加工费倍率
     */
    @ApiModelProperty(value = "加工费倍率")
    private BigDecimal processCostRate;
    /**
     * 二次加工费用
     */
    @ApiModelProperty(value = "二次加工费用")
    private BigDecimal secondaryProcessingCost;
    /**
     * 其他费用
     */
    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherCost;
    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    private BigDecimal salesPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal costPrice;
    /**
     * 销售价倍率
     */
    @ApiModelProperty(value = "销售价倍率")
    private BigDecimal salesPriceRate;
    /**
     * 核价模板id
     */
    @ApiModelProperty(value = "核价模板id")
    private String pricingTemplateId;
    /**
     * 版本号(核价次数)
     */
    @ApiModelProperty(value = "版本号(核价次数)")
    private String version;
    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private String confirmId;
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String confirmName;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因")
    private String confirmReason;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;

    /**
     * 核价颜色
     */
    @ApiModelProperty(value = "颜色")
    private List<PricingColorVO> pricingColors;

    /**
     * 核价工艺费用
     */
    @ApiModelProperty(value = "核价工艺费用")
    private List<PricingCraftCostsVO> pricingCraftCosts;

    /**
     * 物料费用
     */
    @ApiModelProperty(value = "物料费用")
    private List<PricingMaterialCostsVO> pricingMaterialCosts;
    /**
     * 其他费用
     */
    @ApiModelProperty(value = "其他费用")
    private List<PricingOtherCostsVO> pricingOtherCosts;
    /**
     * 加工费用
     */
    @ApiModelProperty(value = "加工费用")
    private List<PricingProcessCostsVO> pricingProcessCosts;

}

