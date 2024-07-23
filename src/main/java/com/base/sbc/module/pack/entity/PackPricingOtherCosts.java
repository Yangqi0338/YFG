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
 * 类描述：资料包-核价信息-其他费用 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackPricingOtherCosts
 * @email your email
 * @date 创建时间：2023-7-13 20:34:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_pricing_other_costs")
@ApiModel("资料包-核价信息-其他费用 PackPricingOtherCosts")
public class PackPricingOtherCosts extends BaseDataEntity<String> {

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
     * 类型:包装费/检测费/外协加工费/毛纱加工费/车缝加工费
     */
    @ApiModelProperty(value = "类型:包装费/检测费/外协加工费/毛纱加工费/车缝加工费/外辅工艺")
    private String costsItem;
    /**
     * 费用类型id
     */
    @ApiModelProperty(value = "费用类型id")
    private String costsTypeId;
    /**
     * 费用类型
     */
    @ApiModelProperty(value = "费用类型/角色")
    private String costsType;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
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
     * 外辅工厂
     */
    @ApiModelProperty(value = "外辅工厂")
    private String factoryName;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private String unit;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

