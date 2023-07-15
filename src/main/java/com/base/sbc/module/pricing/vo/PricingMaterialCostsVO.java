/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：物料费用 实体类
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:25
 */
@Data
@ApiModel("物料费用 ")
public class PricingMaterialCostsVO {

    private static final long serialVersionUID = 1L;

    private String id;


    /**
     * 报价单编码
     */
    @ApiModelProperty(value = "报价单编码")
    private String pricingCode;
    /**
     * 类别（物料分类id）
     */
    @ApiModelProperty(value = "类别（物料分类id）")
    private String categoryId;
    /**
     * 类别名称（物料分类名称）
     */
    @ApiModelProperty(value = "类别名称（物料分类名称）")
    private String categoryName;
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
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialsCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialsName;
    /**
     * 幅宽(CM)
     */
    @ApiModelProperty(value = "幅宽(CM)")
    private String width;
    /**
     * 单件用量
     */
    @ApiModelProperty(value = "单件用量")
    private BigDecimal unitNum;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unit;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unitCode;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;
    /**
     * 额定单耗
     */
    @ApiModelProperty(value = "额定单耗")
    private String ratedUnitConsumption;
    /**
     * 合计用量
     */
    @ApiModelProperty(value = "合计用量")
    private BigDecimal totalUsage;
    /**
     * 购买货币编码
     */
    @ApiModelProperty(value = "购买货币编码")
    private String purchaseCurrencyCode;
    /**
     * 购买货币
     */
    @ApiModelProperty(value = "购买货币")
    private String purchaseCurrency;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 合计单价
     */
    @ApiModelProperty(value = "合计单价")
    private BigDecimal totalPrice;
    /**
     * 报价货币编码
     */
    @ApiModelProperty(value = "报价货币编码")
    private String quotationPriceCurrencyCode;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 上次单价
     */
    @ApiModelProperty(value = "上次单价")
    private BigDecimal lastTimePrice;
    /**
     * 上次报价币种
     */
    @ApiModelProperty(value = "上次报价币种")
    private String lastTimeCurrency;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;
    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String contactsPhone;
    /**
     * 联系人地址
     */
    @ApiModelProperty(value = "联系人地址")
    private String contactsAddress;
    /**
     * 工段分组编码
     */
    @ApiModelProperty(value = "工段分组编码")
    private String workshopGroupCode;
    /**
     * 工段分组
     */
    @ApiModelProperty(value = "工段分组")
    private String workshopGroup;
    /**
     * 核价颜色id
     */
    @ApiModelProperty(value = "核价颜色id")
    private String pricingColorId;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;

}
