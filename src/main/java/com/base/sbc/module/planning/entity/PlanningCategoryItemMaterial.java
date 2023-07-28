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
 * 类描述：企划-坑位关联的素材库表 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-28 14:39:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_category_item_material")
@ApiModel("企划-坑位关联的素材库表 PlanningCategoryItemMaterial")
public class PlanningCategoryItemMaterial extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 品类信息id */
    @ApiModelProperty(value = "品类信息id"  )
    private String planningCategoryItemId;
    /** 素材库id */
    @ApiModelProperty(value = "素材库id"  )
    private String materialCategoryId;
    /** 素材id */
    @ApiModelProperty(value = "素材id"  )
    private String materialId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

