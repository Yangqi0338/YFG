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
 * 类描述：企划-渠道 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningChannel
 * @email your email
 * @date 创建时间：2023-7-21 17:59:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_channel")
@ApiModel("企划-渠道 PlanningChannel")
public class PlanningChannel extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 年份名称
     */
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
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
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;
    /**
     * 性别名称
     */
    @ApiModelProperty(value = "性别名称")
    private String sexName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

