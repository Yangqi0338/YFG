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
 * 类描述：样衣盘点明细 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleInventoryItem
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_inventory_item")
@ApiModel("样衣盘点明细 SampleInventoryItem")
public class SampleInventoryItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 企业编码 */
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    /** 样衣调拨主键ID */
    @ApiModelProperty(value = "样衣调拨主键ID")
    private String sampleAllocateId;

    /** 样衣主键ID */
    @ApiModelProperty(value = "样衣主键ID")
    private String sampleId;

    /** 样衣明细主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 库存数量（老）*/
    @ApiModelProperty(value = "库存数量（老）")
    private Integer oldCount;

    /** 盘点数量（新）*/
    @ApiModelProperty(value = "盘点数量（新）")
    private Integer newCount;

    /** 调拨数量 */
    @ApiModelProperty(value = "调拨数量")
    private Integer allocateCount;

    /** 差异类型：0-盘亏，1-正常，2-盘盈 */
    @ApiModelProperty(value = "差异类型：0-盘亏，1-正常，2-盘盈")
    private Integer differType;

    /** 差异数量（库存数量减盘点数量）- 绝对值 */
    @ApiModelProperty(value = "差异数量（库存数量减盘点数量）- 绝对值")
    private Integer differCount;

    /** 单价 */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    /** 库存总金额（单价*数量(老)） */
    @ApiModelProperty(value = "库存总金额（单价*数量(老)）")
    private BigDecimal oldTotalPrice;

    /** 盘点总金额（单价*数量(新)） */
    @ApiModelProperty(value = "盘点总金额（单价*数量(新)）")
    private BigDecimal newTotalPrice;

    /** 差异总金额（库存总金额减盘点总金额）- 绝对值 */
    @ApiModelProperty(value = "差异总金额（库存总金额减盘点总金额）- 绝对值")
    private BigDecimal differTotalPrice;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

