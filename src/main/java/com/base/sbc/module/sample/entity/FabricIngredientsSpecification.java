/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述： 实体类
 * @address com.base.sbc.module.sample.entity.FabricIngredientsSpecification
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-11-17 15:11:41
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_ingredients_specification")
@ApiModel(" FabricIngredientsSpecification")
public class FabricIngredientsSpecification extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

	/** 辅料信息id */
	@ApiModelProperty(value = "辅料信息id"  )
	private String ingredientsInfoId;
	/** 规格 */
	@ApiModelProperty(value = "规格"  )
	private String specification;
	/** 数量 */
	@ApiModelProperty(value = "数量"  )
	private Integer quantity;
	/** 大货含税价 */
	@ApiModelProperty(value = "大货含税价"  )
	private BigDecimal containPrice;
	/** 颜色编码 */
	@ApiModelProperty(value = "颜色编码"  )
	private String colorCode;
	/** 颜色名称 */
	@ApiModelProperty(value = "颜色名称"  )
	private String	colorName;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
