/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：加工费用 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.entity.PricingProcessCosts
 * @email your email
 * @date 创建时间：2023-7-15 10:22:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_process_costs")
@ApiModel("加工费用 PricingProcessCosts")
public class PricingProcessCosts extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 报价单单号
     */
    @ApiModelProperty(value = "报价单单号")
    private String pricingCode;
    /**
     * 工序编号
     */
    @ApiModelProperty(value = "工序编号")
    private String processCode;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 工时
     */
    @ApiModelProperty(value = "工时")
    private String workingHours;
    /**
     * 标准单价
     */
    @ApiModelProperty(value = "标准单价")
    private BigDecimal standardUnitPrices;
    /**
     * 倍数
     */
    @ApiModelProperty(value = "倍数")
    private BigDecimal multiple;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
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
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

