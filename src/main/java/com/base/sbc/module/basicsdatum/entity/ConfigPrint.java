/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：打印配置 实体类
 * 
 * @address com.base.sbc.module.basicsdatum.entity.ConfigPrint
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-21 10:29:11
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_config_print")
@ApiModel("打印配置 ConfigPrint")
public class ConfigPrint extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************
	 * 实体存放的其他字段区 不替换的区域 【other_start】
	 ******************************************/

	/**********************************
	 * 实体存放的其他字段区 【other_end】
	 ******************************************/

	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【start】
	 ***********************************/
	/** 顺序 */
	@ApiModelProperty(value = "顺序")
	private Integer sort;
	/** 单据类别 */
	@ApiModelProperty(value = "单据类别")
	private String billType;
	/** 编码 */
	@ApiModelProperty(value = "编码")
	private String code;
	/** 名称 */
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "文件名称")
	private String fileName;
	/** 类型(ureport) */
	@ApiModelProperty(value = "类型(ureport)")
	private String printType;
	/** 示例数据 */
	@ApiModelProperty(value = "示例数据")
	private String dataJson;
	/** 模板数据 */
	@ApiModelProperty(value = "模板数据")
	private String fmtJson;
	/** 是否默认选中(0否,1是) */
	@ApiModelProperty(value = "是否默认选中(0否,1是)")
	private String selectFlag;
	/** 是否停用(0否,1是) */
	@ApiModelProperty(value = "是否停用(0否,1是)")
	private String stopFlag;
	/** 备注 */
	@ApiModelProperty(value = "备注")
	private String remarks;
	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【end】
	 ***********************************/
}
