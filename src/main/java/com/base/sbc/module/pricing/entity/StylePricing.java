/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式定价 实体类
 * @address com.base.sbc.module.pricing.entity.StylePricing
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 * @version 1.0
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
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 状态启用(0),停用(1) */
    @ApiModelProperty(value = "状态启用(0),停用(1)"  )
    private String status;
    /** 企划倍率 */
    @ApiModelProperty(value = "企划倍率"  )
    private BigDecimal planningRate;
    /** 是否计控确认 0.否、1.是 */
    @ApiModelProperty(value = "是否计控确认 0.否、1.是"  )
    private String controlConfirm;
    /** 是否商品吊牌确认 0.否、1.是 */
    @ApiModelProperty(value = "是否商品吊牌确认 0.否、1.是"  )
    private String productHangtagConfirm;
    /** 是否计控吊牌确认 0.否、1.是 */
    @ApiModelProperty(value = "是否计控吊牌确认 0.否、1.是"  )
    private String controlHangtagConfirm;
    /** 资料包id */
    @ApiModelProperty(value = "资料包id"  )
    private String packId;
    /** 系列编码 */
    @ApiModelProperty(value = "系列编码"  )
    private String series;
    /** 系列名称 */
    @ApiModelProperty(value = "系列名称"  )
    private String seriesName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
