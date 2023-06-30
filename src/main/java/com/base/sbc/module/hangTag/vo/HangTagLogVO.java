/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：吊牌日志 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTagLog
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:57
 */
@Data
public class HangTagLogVO {

    private static final long serialVersionUID = 1L;
    private String id;
    private String hangTagId;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 创建者名称
     */
    private String createName;

    /**
     * 创建者id
     */
    private String createId;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String operationDescription;
}

