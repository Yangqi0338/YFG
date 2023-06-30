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
 * 类描述：样衣仓库 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleWarehouse
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_warehouse")
@ApiModel("样衣仓库 SampleWarehouse")
public class SampleWarehouse extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 企业编码 */
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称")
    private String position;

    /** 仓库位置 */
    @ApiModelProperty(value = "仓库位置")
    private String address;

    /** 仓库编号 */
    @ApiModelProperty(value = "仓库编号")
    private String code;

    /** 状态：1-正常，2-作废，3-删除 */
    @ApiModelProperty(value = "状态：1-正常，2-作废，3-删除")
    private Integer status;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /** 联系人ID */
    @ApiModelProperty(value = "联系人ID")
    private String contactId;

    /** 联系人 */
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

