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
 * 类描述：样衣销售明细 Vo
 * @address com.base.sbc.module.sample.entity.SampleInventoryItemVo
 */
@Data
@ApiModel("样衣盘点明细 SampleInventoryItemVo")
public class SampleInventoryItemVo {
    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    private String images;

    /** 样衣编码 */
    @ApiModelProperty(value = "样衣编码")
    private String code;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 款式名称 */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 库存数量（老） */
    @ApiModelProperty(value = "库存数量（老）")
    private Integer oldCount;

    /** 数量 */
    @ApiModelProperty(value = "盘点数量（新）")
    private Integer newCount;

    /** 差异类型：0-盘亏，1-正常，2-盘盈 */
    @ApiModelProperty(value = "差异类型：0-盘亏，1-正常，2-盘盈")
    private Integer differType;

    /** 数量 */
    @ApiModelProperty(value = "差异数量（库存数量减盘点数量）- 绝对值")
    private Integer differCount;

    /** 价格（原价） */
    @ApiModelProperty(value = "价格（原价）")
    private BigDecimal price;

    /** 样衣盘点主键ID */
    @ApiModelProperty(value = "样衣盘点主键ID")
    private String sampleInventoryId;

    /** 样衣明细主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 库存总金额（单价*数量(老)）*/
    @ApiModelProperty(value = "库存总金额（单价*数量(老)）")
    private BigDecimal oldTotalPrice;

    /** 盘点总金额（单价*数量(新)）*/
    @ApiModelProperty(value = "盘点总金额（单价*数量(新)）")
    private BigDecimal newTotalPrice;

    /** 差异总金额（库存总金额减盘点总金额）- 绝对值 */
    @ApiModelProperty(value = "差异总金额（库存总金额减盘点总金额）- 绝对值")
    private BigDecimal differTotalPrice;

    /** 位置ID */
    @ApiModelProperty(value = "位置ID")
    private String positionId;

    /** 位置 */
    @ApiModelProperty(value = "位置")
    private String position;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

