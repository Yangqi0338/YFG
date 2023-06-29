/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：样衣调拨 实体类
 * @address com.base.sbc.module.sample.entity.SampleAllocate
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_allocate")
@ApiModel("样衣调拨 SampleAllocate")
public class SampleAllocate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 企业编码 */
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    /** 编号 */
    @ApiModelProperty(value = "单号")
    private String code;

    /** 状态：1-正常，2-作废，3-删除 */
    @ApiModelProperty(value = "状态：1-正常，2-作废，3-删除")
    private Integer tatus;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /** 调出位置ID */
    @ApiModelProperty(value = "调出位置ID")
    private String fromPositionId;

    /** 调出位置 */
    @ApiModelProperty(value = "调出位置")
    private String fromPosition;

    /** 调入位置ID */
    @ApiModelProperty(value = "调入位置ID")
    private String toPositionId;

    /** 调入位置 */
    @ApiModelProperty(value = "调入位置")
    private String toPosition;

    /** 调拨时间 */
    @ApiModelProperty(value = "调拨时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allocateDate;

    /** 总数量 */
    @ApiModelProperty(value = "总数量")
    private String totalCount;

    /** 经手人ID */
    @ApiModelProperty(value = "经手人ID")
    private String operateId;

    /** 经手人 */
    @ApiModelProperty(value = "经手人")
    private String operateName;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

