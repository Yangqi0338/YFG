/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 类描述：企划-坑位信息 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningCategoryItem
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-27 15:54:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_category_item")
@ApiModel("企划-坑位信息 PlanningCategoryItem")
public class PlanningCategoryItem extends BaseDataEntity<String> {

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
     * 波段企划id
     */
    @ApiModelProperty(value = "波段企划id")
    private String planningBandId;
    /**
     * 品类信息id
     */
    @ApiModelProperty(value = "品类信息id")
    private String planningCategoryId;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 品类名称路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;
    /**
     * 品类id路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)")
    private String categoryIds;
    /**
     * 大类id
     */
    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String prodCategory;
    /**
     * 中类id
     */
    @ApiModelProperty(value = "中类id")
    private String prodCategory2nd;
    /**
     * 小类
     */
    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;
    /**
     * 价格带
     */
    @ApiModelProperty(value = "价格带")
    private String price;
    /**
     * 关联的素材库数量
     */
    @ApiModelProperty(value = "关联的素材库数量")
    private BigDecimal materialCount;
    /**
     * 关联历史款
     */
    @ApiModelProperty(value = "关联历史款")
    private String hisDesignNo;
    /**
     * 状态:0未下发,1已下发到产品季总览,2已下发到样衣设计
     */
    @ApiModelProperty(value = "状态:0未下发,1已下发到产品季总览,2已下发到样衣设计")
    private String status;
    /**
     * 下发时间
     */
    @ApiModelProperty(value = "下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendDate;
    /**
     * 计划完成时间
     */
    @ApiModelProperty(value = "计划完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningFinishDate;
    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;
    /**
     * 任务等级:普通,紧急,非常紧急
     */
    @ApiModelProperty(value = "任务等级:普通,紧急,非常紧急")
    private String taskLevel;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

