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
 * 类描述：企划-维度表 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningDimensionality
 * @email your email
 * @date 创建时间：2023-10-18 20:55:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_dimensionality")
@ApiModel("企划-维度表 PlanningDimensionality")
public class PlanningDimensionality extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 字段管理id
     */
    @ApiModelProperty(value = "字段管理id")
    private String fieldId;
    /**
     * 渠道企划id
     */
    @ApiModelProperty(value = "渠道企划id")
    private String planningChannelId;
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
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String categoryId;
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;
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
     * 维度名称
     */
    @ApiModelProperty(value = "维度名称")
    private String dimensionalityName;
    /**
     * 是否检查
     */
    @ApiModelProperty(value = "是否检查")
    private String isExamine;

    /**
     * 围度系数标识(0微度数据1围度系数)
     */
    @ApiModelProperty(value = "围度系数标识(0微度数据1围度系数)")
    private String  coefficientFlag;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    @ApiModelProperty(value = "显示配置，为空是全部场景显示，不为空时根据传入条件取交集")
    private String showConfig;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

