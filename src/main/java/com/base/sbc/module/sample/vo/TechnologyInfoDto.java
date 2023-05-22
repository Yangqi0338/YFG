package com.base.sbc.module.sample.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("工艺说明保存 TechnologyFVDto")
public class TechnologyInfoDto {

    @ApiModelProperty(value = "表单类型主键"  )
    private String fieldName;

    @ApiModelProperty(value = "值"  )
    private String val;
}
