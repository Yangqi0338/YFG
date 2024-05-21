/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述： 实体类
 * @address com.base.sbc.module.column.entity.ColumnGroupDefineItem
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_column_group_define_item")
@ApiModel(" ColumnGroupDefineItem")
public class ColumnGroupDefineItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
	/**
	 * 表格编码
	 */
	@ApiModelProperty(value = "表格编码")
	private String tableCode;
	/**
	 * 系统列自定义ID
	 */
	@ApiModelProperty(value = "系统列自定义ID")
	private String sysId;
	/**
	 * 用户组ID
	 */
	@ApiModelProperty(value = "用户组ID")
	private String userGroupId;
	/**
	 * 版本ID
	 */
	@ApiModelProperty(value = "版本ID")
	private String versionId;
	/**
	 * 列名
	 */
	@ApiModelProperty(value = "列名")
	private String columnName;
	/**
	 * 列名国际化key
	 */
	@ApiModelProperty(value = "列名国际化key")
	private String columnNameI18nKey;
	/**
	 * 是否隐藏
	 */
	@ApiModelProperty(value = "是否隐藏")
	private String hidden;
	/**
	 * 对齐方式
	 */
	@ApiModelProperty(value = "对齐方式")
	private String alignType;
	/**
	 * 固定方式
	 */
	@ApiModelProperty(value = "固定方式")
	private String fixType;
	/**
	 * 是否可编辑
	 */
	@ApiModelProperty(value = "是否可编辑")
	private String isEdit;
	/**
	 * 列宽
	 */
	@ApiModelProperty(value = "列宽")
	private Integer columnWidth;
	/**
	 * 默认排序
	 */
	@ApiModelProperty(value = "默认排序")
	private Integer sortOrder;
	/**
	 * 字段颜色
	 */
	@ApiModelProperty(value = "字段颜色")
	private String columnColor;

	/**
	 * 是否可编辑
	 */
	@ApiModelProperty(value = "是否可编辑")
	private String isEdit;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
