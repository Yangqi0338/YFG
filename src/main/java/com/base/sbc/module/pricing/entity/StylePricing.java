/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：款式定价 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.entity.StylePricing
 * @email your email
 * @date 创建时间：2023-10-17 20:44:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_pricing")
@ApiModel("款式定价 StylePricing")
public class StylePricing extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 状态启用(0),停用(1) */
    @ApiModelProperty(value = "状态启用(0),停用(1)"  )
    private String status;
    /** 企划倍率 */
    @ApiModelProperty(value = "企划倍率"  )
    private BigDecimal planningRate;
    /** 工时部确认工价 0.否、1.是 */
    @ApiModelProperty(value = "工时部确认工价 0.否、1.是"  )
    private String wagesConfirm;
    /**
     * 工时部确认工价时间
     */
    @ApiModelProperty(value = "工时部确认工价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date wagesConfirmTime;
    /** 是否计控确认 0.否、1.是 */
    @ApiModelProperty(value = "是否计控确认 0.否、1.是"  )
    private String controlConfirm;
    /** 是否商品吊牌确认 0.否、1.是 */
    @ApiModelProperty(value = "是否商品吊牌确认 0.否、1.是")
    private String productHangtagConfirm;
    /**
     * 是否商品吊牌确认时间
     */
    @ApiModelProperty(value = "是否商品吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productHangtagConfirmTime;
    /**
     * 是否计控吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控吊牌确认 0.否、1.是")
    private String controlHangtagConfirm;
    /**
     * 是否计控吊牌确认时间
     */
    @ApiModelProperty(value = "是否计控吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlHangtagConfirmTime;
    /**
     * 资料包id
     */
    @ApiModelProperty(value = "资料包id")
    private String packId;
    /**
     * 系列名称
     */
    @ApiModelProperty(value = "系列名称")
    private String seriesName;
    /**
     * 系列编码
     */
    @ApiModelProperty(value = "系列编码")
    private String series;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 计控实际成本
     */
    @ApiModelProperty(value = "计控实际成本")
    private BigDecimal controlPlanCost;
    /**
     * 计控确认成本时间
     */
    @ApiModelProperty(value = "计控确认成本时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlConfirmTime;
    /**
     * 产品风格编码
     */
    @ApiModelProperty(value = "产品风格编码")
    private String productStyle;
    /**
     * 产品风格编码
     */
    @ApiModelProperty(value = "产品风格编码")
    private String productStyleName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

