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
 * 类描述：吊牌成分
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 */
@Data
@ApiModel(value = "吊牌成分")
public class HangTagIngredientDTO extends BaseDto {

    private static final long serialVersionUID = 1L;

    private String id;
    private String hangTagId;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 百分比
     */
    @ApiModelProperty(value = "百分比")
    private BigDecimal percentage;
    /**
     * 成分说明
     */
    @ApiModelProperty(value = "成分说明")
    private String ingredientDescription;
    /**
     * 成分说明编码
     */
    @ApiModelProperty(value = "成分说明编码")
    private String ingredientDescriptionCode;
    /**
     * 成分备注
     */
    @ApiModelProperty(value = "成分备注")
    private String descriptionRemarks;
    /**
     * 成分备注编码
     */
    @ApiModelProperty(value = "成分备注编码")
    private String descriptionRemarksCode;
}

