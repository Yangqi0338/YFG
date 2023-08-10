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
 * 类描述：样衣明细日志 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleItemLog
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_item_log")
@ApiModel("样衣明细日志 SampleItemLog")
public class SampleItemLog extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 样衣主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 操作类型：1.借出、2、归还、3.销售、4.盘点、5.调拨、6.新增、7.修改 */
    @ApiModelProperty(value = "操作类型：1.借出、2、归还、3.销售、4.盘点、5.调拨、6.新增、7.修改")
    private String type;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

