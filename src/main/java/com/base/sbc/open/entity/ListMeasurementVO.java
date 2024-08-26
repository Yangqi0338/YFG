/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListMeasurementVO {

	private static final long serialVersionUID = 1L;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 测量点 */
    @ApiModelProperty(value = "测量点"  )
    private String measurement;
}
