/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：款式管理-订货本 实体类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:29
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_order_book")
@ApiModel("款式管理-订货本 SampleStyleOrderBook")
public class StyleOrderBook extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/** 订货本编号 */
	@ApiModelProperty(value = "订货本编号")
	private String orderBookCode;
	/** 图片地址 */
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	/** 上会标记(0未上会，1已上会) */
	@ApiModelProperty(value = "上会标记(0未上会，1已上会)")
	private String meetFlag;
	/** 锁定标记(0未锁定，1已锁定) */
	@ApiModelProperty(value = "锁定标记(0未锁定，1已锁定)")
	private String lockFlag;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
}
