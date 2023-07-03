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

/**
 * 类描述：款式管理-款式搭配 与配色中间表 实体类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:26
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_style_group_color")
@ApiModel("款式管理-款式搭配 与配色中间表 SampleStyleGroupColor")
public class SampleStyleGroupColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
    /** 搭配编码 */
    @ApiModelProperty(value = "搭配编码"  )
    private String groupCode;
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
}
