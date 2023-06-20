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
 * 类描述：核价颜色
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:20
 */
@Data
@ApiModel("核价颜色 ")
public class PricingColorVO {

    private static final long serialVersionUID = 4882876763193172914L;
    private String id;

    /**
     * 报价单单号
     */
    @ApiModelProperty(value = "报价单单号")
    private String pricingCode;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 物料费用
     */
    @ApiModelProperty(value = "物料费用")
    private BigDecimal materialCost;
}
