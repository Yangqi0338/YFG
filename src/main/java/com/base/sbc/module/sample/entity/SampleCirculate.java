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
 * 类描述：样衣借还 实体类
 * @address com.base.sbc.module.sample.entity.SampleCirulate
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_circulate")
@ApiModel("样衣借还 SampleCirculate")
public class SampleCirculate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;


    /** 借出单号 */
    @ApiModelProperty(value = "借出单号")
    private String borrowCode;

    /** 借出类型：1-内部借出，2-外部借出 */
    @ApiModelProperty(value = "借出类型：1-内部借出，2-外部借出")
    private Integer borrowType;

    /** 预计归还时间 */
    @ApiModelProperty(value = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectReturnDate;

    /** 借出原因 */
    @ApiModelProperty(value = "借出原因")
    private String borrowReason;

    /** 借出时间 */
    @ApiModelProperty(value = "借出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date borrowDate;

    /** 借出人ID */
    @ApiModelProperty(value = "借出人ID")
    private String borrowId;

    /** 借出人 */
    @ApiModelProperty(value = "借出人")
    private String borrowName;

    /** 借出数量 */
    @ApiModelProperty(value = "借出数量")
    private Integer borrowCount;
    /** 归还数量 */
    @ApiModelProperty(value = "归还数量")
    private Integer returnCount;


    /** 经手人ID */
    @ApiModelProperty(value = "经手人ID")
    private String operateId;

    /** 经手人 */
    @ApiModelProperty(value = "经手人")
    private String operateName;
}

