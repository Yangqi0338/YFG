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

import java.math.BigDecimal;
/**
 * 类描述：企划-品类信息 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningCategory
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-27 15:54:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_category")
@ApiModel("企划-品类信息 PlanningCategory")
public class PlanningCategory extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 波段企划id
     */
    @ApiModelProperty(value = "波段企划id")
    private String planningBandId;
    /**
     * 品类名称路径(大类/品类)
     */
    @ApiModelProperty(value = "品类名称路径(大类/品类)")
    private String categoryName;
    /**
     * 品类id路径(大类/品类)
     */
    @ApiModelProperty(value = "品类id路径(大类/品类)")
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
     * 品类规划SKC数
     */
    @ApiModelProperty(value = "品类规划SKC数")
    private BigDecimal categoryPlanSkcNum;
    /**
     * 款色数SKC数
     */
    @ApiModelProperty(value = "款色数SKC数")
    private BigDecimal styleColorSkcNum;
    /**
     * 每款SKC数
     */
    @ApiModelProperty(value = "每款SKC数")
    private BigDecimal styleSkcNum;
    /** 企划需求数 */
    @ApiModelProperty(value = "企划需求数"  )
    private BigDecimal planRequirementNum;
    /** 设计放量 */
    @ApiModelProperty(value = "设计放量"  )
    private BigDecimal designReleaseNum;
    /** 计划开发数 */
    @ApiModelProperty(value = "计划开发数"  )
    private BigDecimal planDevNum;
    /** 负责人id */
    @ApiModelProperty(value = "负责人id"  )
    private String managerId;
    /** 负责人名称 */
    @ApiModelProperty(value = "负责人名称"  )
    private String manager;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

