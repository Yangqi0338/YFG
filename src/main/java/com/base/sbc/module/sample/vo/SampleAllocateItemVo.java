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

/**
 * 类描述：样衣调拨明细 Vo
 * @address com.base.sbc.module.sample.entity.SampleAllocateItemVo
 */
@Data
@ApiModel("样衣调拨明细 SampleAllocateItemVo")
public class SampleAllocateItemVo {
    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 样衣编码 */
    @ApiModelProperty(value = "样衣编码")
    private String code;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    private String images;

    /** 设计编号 */
    @ApiModelProperty(value = "设计编号")
    private String designNo;

    /** 款式名称 */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;

    /** 样衣销售主键ID */
    @ApiModelProperty(value = "样衣销售主键ID")
    private String sampleSaleId;

    /** 样衣明细主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 位置 */
    @ApiModelProperty(value = "位置")
    private String position;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private String count;

    /** 调出数量 */
    @ApiModelProperty(value = "调出数量")
    private String allocateCount;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}