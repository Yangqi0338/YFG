/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类描述：核价表
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@Data
@ApiModel("核价表 Pricing")
public class PricingDTO {

    private static final long serialVersionUID = -9146473387690735830L;

    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String code;
    /**
     * 报价日期
     */
    @ApiModelProperty(value = "报价日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 来源类型
     *
     * @see com.base.sbc.module.pricing.enums.PricingSourceTypeEnum
     */
    @ApiModelProperty(value = "来源类型")
    private String sourceType;
    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String styleCode;
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
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 季节编码
     */
    @ApiModelProperty(value = "季节编码")
    private String seasonCode;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 年份编码
     */
    @ApiModelProperty(value = "年份编码")
    private String yearCode;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 合作方式编码
     */
    @ApiModelProperty(value = "合作方式编码")
    private String cooperationMethodsCode;
    /**
     * 合作方式
     */
    @ApiModelProperty(value = "合作方式")
    private String cooperationMethods;
    /**
     * 付款方式编码
     */
    @ApiModelProperty(value = "付款方式编码")
    private String payMethodsCode;
    /**
     * 付款方式
     */
    @ApiModelProperty(value = "付款方式")
    private String payMethods;
    /**
     * 计划下单时间
     */
    @ApiModelProperty(value = "计划下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planBillDate;
    /**
     * 交货日期
     */
    @ApiModelProperty(value = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date giveDate;
    /**
     * 生产周期
     */
    @ApiModelProperty(value = "生产周期")
    private BigDecimal yieldDay;
    /**
     * 面料系数编码
     */
    @ApiModelProperty(value = "面料系数编码")
    private String fabricCoefficientCode;
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
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String categoryCode;
    /**
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    private String quoteDesc;
    /**
     * 销售渠道(多个逗号分隔)
     */
    @ApiModelProperty(value = "销售渠道(多个逗号分隔)")
    private String salesChannelName;
    /**
     * 销售渠道code(多个逗号分隔)
     */
    @ApiModelProperty(value = "销售渠道code(多个逗号分隔)")
    private String salesChannelCode;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 货币编码
     */
    @ApiModelProperty(value = "货币编码")
    private String currencyCode;
    /**
     * 货币2
     */
    @ApiModelProperty(value = "货币2")
    private String otherCurrency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationUnitPrice;
    /**
     * 货币2编码
     */
    @ApiModelProperty(value = "货币2编码")
    private String otherCurrencyCode;
    /**
     * 汇率
     */
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;
    /**
     * 其他报价单价
     */
    @ApiModelProperty(value = "其他报价单价")
    private BigDecimal otherQuotationUnitPrice;
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
     * 核价模板名称
     */
    @ApiModelProperty(value = "核价模板")
    private String pricingTemplateName;
    /**
     * 版本号(核价次数)
     */
    @ApiModelProperty(value = "版本号(核价次数)")
    private String version;
    /**
     * 核价颜色
     */
    @ApiModelProperty(value = "核价颜色")
    private List<PricingColorDTO> pricingColors;

    /**
     * 核价工艺费用
     */
    @ApiModelProperty(value = "核价工艺费用")
    private List<PricingCraftCostsDTO> pricingCraftCosts;
    /**
     * 核价物料费用
     */
    @ApiModelProperty(value = "核价物料费用")
    private Map<String, List<PricingMaterialCostsDTO>> pricingMaterialCosts;
    /**
     * 核价其他费用
     */
    @ApiModelProperty(value = "核价其他费用")
    private List<PricingOtherCostsDTO> pricingOtherCosts;
    /**
     * 核价加工费用
     */
    @ApiModelProperty(value = "核价加工费用")
    private List<PricingProcessCostsDTO> pricingProcessCosts;
    /**
     * 核价公式
     */
    @ApiModelProperty(value = "核价公式")
    private String pricingFormula;
}

