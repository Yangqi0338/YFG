/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：企划-需求维度表 实体类
 * @address com.base.sbc.module.planning.entity.PlanningDemandDimensionality
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_demand")
public class PlanningDemand extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;

    @ApiModelProperty(value = "产品季id"  )
    private String planningSeasonId;
    /** 需求名称 */
    @ApiModelProperty(value = "需求名称"  )
    private String demandName;
    /** 表单类型id */
    private String  formTypeId;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
