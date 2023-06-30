/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：样衣销售明细-移动端 Vo
 * @address com.base.sbc.module.sample.entity.SampleSaleSampleItemVo
 */
@Data
@ApiModel("样衣销售明细-移动端 SampleSaleSampleItemVo")
public class SampleSaleSampleItemVo {

    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 样衣编码 */
    @ApiModelProperty(value = "样衣编码")
    private String code;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    private String images;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 款式名称 */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /** 品类 */
    @ApiModelProperty(value = "品类")
    private String categoryName;

    /** 样衣类型：1-内部研发，2-外采，2-ODM提供 */
    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private String type;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 客户名称 */
    @ApiModelProperty(value = "客户名称")
    private String custmerName;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private String count;

    /** 价格（原价） */
    @ApiModelProperty(value = "价格（原价）")
    private BigDecimal price;

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

