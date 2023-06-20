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
 * 类描述：核价颜色 实体类
 * @address com.base.sbc.module.pricing.entity.PricingColor
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:20
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_color")
@ApiModel("核价颜色 PricingColor")
public class PricingColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 报价单单号 */
    @ApiModelProperty(value = "报价单单号"  )
    private String pricingCode;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 物料费用 */
    @ApiModelProperty(value = "物料费用"  )
    private BigDecimal materialCost;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
