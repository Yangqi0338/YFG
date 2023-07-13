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
 * 类描述：资料包-二次加工费(工艺费用) 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackPricingCraftCosts
 * @email your email
 * @date 创建时间：2023-7-13 20:34:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_pricing_craft_costs")
@ApiModel("资料包-二次加工费(工艺费用) PackPricingCraftCosts")
public class PackPricingCraftCosts extends BaseDataEntity<String> {

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
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id")
    private String craftId;
    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "工艺名称")
    private String craftName;
    /**
     * 是否委外0.否, 1.是
     */
    @ApiModelProperty(value = "是否委外0.否, 1.是")
    private String outsource;
    /**
     * 加工商id
     */
    @ApiModelProperty(value = "加工商id")
    private String processorId;
    /**
     * 加工商名称
     */
    @ApiModelProperty(value = "加工商名称")
    private String processorName;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 汇率
     */
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal num;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal sumprice;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

