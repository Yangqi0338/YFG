/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：企划-需求表 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningDemand
 * @email your email
 * @date 创建时间：2023-10-16 15:13:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_demand")
@ApiModel("企划-需求表 PlanningDemand")
public class PlanningDemand extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季id */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;
    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 需求名称
     */
    @ApiModelProperty(value = "需求名称")
    private String demandName;
    /**
     * 表单类型id
     */
    @ApiModelProperty(value = "表单类型id")
    private String formTypeId;
    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id")
    private String fieldId;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 重点维度(0:否,1是)
     */
    @ApiModelProperty(value = "重点维度(0:否,1是)")
    private String importantFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

