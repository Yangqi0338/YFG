package com.base.sbc.module.formtype.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选项 OptionDto")
public class OptionDto {

    /** 字段管理id */
    @ApiModelProperty(value = "字段管理id"  )
    private String fieldId;
    /** 选项编码*/
    @ApiModelProperty(value = "选项编码"  )
    private String optionCode;
    /** 选项名 */
    @ApiModelProperty(value = "选项名"  )
    private String optionName;
}
