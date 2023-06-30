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

/**
 * 类描述：样衣调拨明细 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleAllocateItem
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_allocate_item")
@ApiModel("样衣调拨明细 SampleAllocateItem")
public class SampleAllocateItem extends BaseDataEntity<String> {

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

    /** 当前数量（库存） */
    @ApiModelProperty(value = "当前数量（库存）")
    private Integer afterCount;

    /** 调拨数量 */
    @ApiModelProperty(value = "调拨数量")
    private Integer count;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

