/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 类描述：工作小账 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.entity.WorkLog
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-11 15:45:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_work_log")
@ApiModel("工作小账 WorkLog")
public class WorkLog extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 登记编码
     */
    @ApiModelProperty(value = "登记编码")
    private String code;
    /**
     * 启用状态:(1启用,0停用)
     */
    @ApiModelProperty(value = "启用状态:(1启用,0停用)")
    private String enableFlag;
    /**
     * 工作日期
     */
    @ApiModelProperty(value = "工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date workDate;
    /**
     * 工作委派部门
     */
    @ApiModelProperty(value = "工作委派部门")
    private String delegatedDepartment;
    /**
     * 工作委派部门id
     */
    @ApiModelProperty(value = "工作委派部门id")
    private String delegatedDepartmentId;
    /**
     * 工作人员
     */
    @ApiModelProperty(value = "工作人员-后改为样衣工")
    private String worker;
    /**
     * 工作人员Id
     */
    @ApiModelProperty(value = "工作人员Id")
    private String workerId;
    /**
     * 登记类型
     */
    @ApiModelProperty(value = "登记类型")
    private String logType;
    /**
     * 登记类型名称
     */
    @ApiModelProperty(value = "登记类型名称")
    private String logTypeName;
    /**
     * 参考类型
     */
    @ApiModelProperty(value = "参考类型")
    private String numType;
    /**
     * 参考类型名称
     */
    @ApiModelProperty(value = "参考类型名称")
    private String numTypeName;
    /**
     * 参考款号
     */
    @ApiModelProperty(value = "参考款号")
    private String referenceNo;
    /**
     * 工作说明
     */
    @ApiModelProperty(value = "工作说明")
    private String workDescription;
    /**
     * 计件数量
     */
    @ApiModelProperty(value = "计件数量")
    private BigDecimal pieceCount;
    /**
     * 计时时间（分钟）
     */
    @ApiModelProperty(value = "计时时间（分钟）")
    private BigDecimal timeMinutes;
    /**
     * 工作分数
     */
    @ApiModelProperty(value = "工作分数")
    private BigDecimal score;

    /**
     * 列头筛选数量
     */
    @TableField(exist = false)
    private Integer groupCount;

    /** 创建部门 */
    @ApiModelProperty(value = "创建部门"  )
    private String createDeptId;
    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    private String cutterName;
    /**
     * 裁剪工id
     */
    @ApiModelProperty(value = "裁剪工id")
    private String cutterId;

    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

