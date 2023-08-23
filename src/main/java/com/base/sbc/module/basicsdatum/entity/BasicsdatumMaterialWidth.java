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
 * 类描述：基础资料-物料档案-物料规格 实体类
 *
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:19
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_material_width")
@ApiModel("基础资料-物料档案-物料规格 BasicsdatumMaterialWidth")
public class BasicsdatumMaterialWidth extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 规格代码 */
	@ApiModelProperty(value = "规格代码")
	private String widthCode;

	/** 规格名称 */
	@ApiModelProperty(value = "规格名称")
	private String name;


	/** 尺码号型 */
	private String sizeName;
	/** 排序码 */
	private String sortCode;
	/** 尺码排序码 */
	private String code;
}
