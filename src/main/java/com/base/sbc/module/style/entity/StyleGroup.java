/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：款式管理-款式搭配 实体类
 * 
 * @author shenzhixiong
 * @date 创建时间：2023-7-3 14:37:23
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_group")
@ApiModel("款式管理-款式搭配 SampleStyleGroup")
public class StyleGroup extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
    /** 搭配编码 */
    @ApiModelProperty(value = "搭配编码"  )
    private String groupCode;
    /** 搭配名称 */
    @ApiModelProperty(value = "搭配名称"  )
    private String groupName;
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    private BigDecimal tagPrice;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
}
