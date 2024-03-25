/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：基础资料-物料档案-物料成分 实体类
 * 
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialIngredient
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-7 14:34:56
 * @version 1.0
 */
@Data
@TableName("t_basicsdatum_material_ingredient")
@ApiModel("基础资料-物料档案-物料成分 BasicsdatumMaterialIngredient")
public class BasicsdatumMaterialIngredient extends BaseEntity {

	/**********************************
	 * 实体存放的其他字段区 不替换的区域 【other_start】
	 ******************************************/

	/**********************************
	 * 实体存放的其他字段区 【other_end】
	 ******************************************/

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【start】
	 ***********************************/
	/** 成分类型(0:成分,1:厂家成分) */
	@ApiModelProperty(value = "成分类型(0:成分,1:厂家成分)")
	private String type;
	/** 成分编码 */
	@ApiModelProperty(value = "成分编码")
	private String code;
	/** 成分名称 */
	@ApiModelProperty(value = "成分名称")
	private String name;
	/** 比例(%) */
	@ApiModelProperty(value = "比例(%)")
	private BigDecimal ratio;
	/** 说明 */
	@ApiModelProperty(value = "说明编码")
	private String sayCode;
	/** 说明 */
	@ApiModelProperty(value = "说明")
	private String say;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 物料编号 */
	@ApiModelProperty(value = "物料成分编号")
	private String materialKindCode;
	/** 物料编号 */
	@ApiModelProperty(value = "物料成分名称")
	private String materialKindName;
	/** 创建日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/** 创建者名称 */
	@TableField(fill = FieldFill.INSERT)
	private String createName;
	/** 创建者id */
	@TableField(fill = FieldFill.INSERT)
	private String createId;
	/** 公司编码 */
	@TableField(fill = FieldFill.INSERT)
	protected String companyCode;

	@Override
	public void preInsert() {
	}

	@Override
	public void preUpdate() {

	}

	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【end】
	 ***********************************/
}
