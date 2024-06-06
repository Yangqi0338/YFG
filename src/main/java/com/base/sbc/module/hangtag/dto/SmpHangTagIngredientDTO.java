/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：吊牌成分信息详情
 *
 * @author huangqiang
 * @date 2024-06-03
 */
@Data
@ApiModel(value = "吊牌成分信息详情")
public class SmpHangTagIngredientDTO {
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 成分二级名称
     */
    @ApiModelProperty(value = "成分二级名称")
    private String ingredientSecondName;
    /**
     * 百分比
     */
    @ApiModelProperty(value = "百分比")
    private BigDecimal percentageStr;
    /**
     * 成分名称
     */
    @ApiModelProperty(value = "成分名称")
    private String ingredientName;
    /**
     * 成分说明
     */
    @ApiModelProperty(value = "成分说明")
    private String ingredientDescription;
}

