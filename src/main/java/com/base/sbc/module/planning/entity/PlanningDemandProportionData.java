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
 * 类描述：企划-需求维度数据表 实体类
 * @address com.base.sbc.module.planning.entity.PlanningDemandDimensionalityData
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:22
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_demand_proportion_data")
public class PlanningDemandProportionData extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 需求维度id */
    @ApiModelProperty(value = "需求维度id"  )
    private String demandId;
    /** 分类,维度 */
    @ApiModelProperty(value = "分类"  )
    private String classify;
    /** 分类,维度 */
    @ApiModelProperty(value = "分类名称"  )
    private String classifyName;
    /** 占比，检查 */
    @ApiModelProperty(value = "占比"  )
    private String proportion;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
