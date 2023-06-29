/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：样衣销售明细 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleSale
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_sale_item")
@ApiModel("样衣销售明细 SampleSaleItem")
public class SampleSaleItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 企业编码 */
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    /** 样衣销售主键ID */
    @ApiModelProperty(value = "样衣销售主键ID")
    private String sampleSaleId;

    /** 样衣主键ID */
    @ApiModelProperty(value = "样衣主键ID")
    private String sampleId;

    /** 样衣明细主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private Integer count;

    /** 价格（原价） */
    @ApiModelProperty(value = "价格（原价）")
    private BigDecimal price;

    /** 单价（售价）*/
    @ApiModelProperty(value = "单价（售价）")
    private BigDecimal salePrice;

    /** 折扣 */
    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    /** 总价（数量*单价（售价）*折扣） */
    @ApiModelProperty(value = "总价（数量*单价（售价）*折扣）")
    private BigDecimal totalPrice;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

