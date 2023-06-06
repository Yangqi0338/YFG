/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：打版管理-工作记录 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.entity.PatternMakingWorkLog
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 19:42:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_making_work_log")
@ApiModel("打版管理-工作记录 PatternMakingWorkLog")
public class PatternMakingWorkLog extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 打板id
     */
    @ApiModelProperty(value = "打板id")
    private String patternMakingId;
    /**
     * 用户类型:裁剪工、车缝工
     */
    @ApiModelProperty(value = "用户类型:裁剪工、车缝工")
    private String userType;
    /**
     * 登记类型:修改、小样、其他
     */
    @ApiModelProperty(value = "登记类型:修改、小样、其他")
    private String logType;
    /**
     * 记录
     */
    @ApiModelProperty(value = "记录")
    private String remarks;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
