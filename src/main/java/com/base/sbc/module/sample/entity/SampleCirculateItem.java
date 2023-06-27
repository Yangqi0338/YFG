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
 * 类描述：样衣明细 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleCirulateItem
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_cirulate_item")
@ApiModel("样衣明细 SampleCirulateItem")
public class SampleCirculateItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 样衣借还主键ID */
    @ApiModelProperty(value = "样衣借还主键ID")
    private String sampleCirculateId;

    /** 样衣主键ID */
    @ApiModelProperty(value = "样衣主键ID")
    private String sampleId;

    /** 样衣明细主键ID */
    @ApiModelProperty(value = "样衣明细主键ID")
    private String sampleItemId;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

