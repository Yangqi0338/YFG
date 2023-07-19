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
 * 类描述：企划-波段表 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningBand
 * @email your email
 * @date 创建时间：2023-7-19 19:19:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_band")
@ApiModel("企划-波段表 PlanningBand")
public class PlanningBand extends BaseDataEntity<String> {

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
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String name;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 月份名称
     */
    @ApiModelProperty(value = "月份名称")
    private String monthName;
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
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String bandName;
    /**
     * 生产模式
     */
    @ApiModelProperty(value = "生产模式")
    private String devtType;
    /**
     * 生产模式名称
     */
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
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
     * 0草稿、2已提交(有坑位信息下发)
     */
    @ApiModelProperty(value = "0草稿、2已提交(有坑位信息下发)")
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

