/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：资料包-核价信息 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackPricing
 * @email your email
 * @date 创建时间：2023-7-13 20:34:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_pricing")
@ApiModel("资料包-核价信息 PackPricing")
public class PackPricing extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 报价币种(1)
     */
    @ApiModelProperty(value = "报价币种(1)")
    private String currency;
    /**
     * 汇率(1)
     */
    @ApiModelProperty(value = "汇率(1)")
    private String exchangeRate;
    /**
     * 单价/件(1)
     */
    @ApiModelProperty(value = "单价/件(1)")
    private BigDecimal costPrice;
    /**
     * 报价币种(2)
     */
    @ApiModelProperty(value = "报价币种(2)")
    private String otherCurrency;
    /**
     * 汇率(2)
     */
    @ApiModelProperty(value = "汇率(2)")
    private String otherExchangeRate;
    /**
     * 单价/件(2)
     */
    @ApiModelProperty(value = "单价/件(2)")
    private BigDecimal otherCostPrice;
    /**
     * 计算项的值
     */
    @ApiModelProperty(value = "计算项的值")
    private String calcItemVal;
    /**
     * 核价模板id
     */
    @ApiModelProperty(value = "核价模板id")
    private String pricingTemplateId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;

    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colorName;

    /**
     * 成衣合同价
     */
    @ApiModelProperty(value = "成衣合同价")
    private String finishedProductPrice;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

