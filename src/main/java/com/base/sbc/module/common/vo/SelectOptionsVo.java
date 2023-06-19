package com.base.sbc.module.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下拉选择Vo ")
public class SelectOptionsVo {
    @ApiModelProperty(value = "label")
    private String label;
    @ApiModelProperty(value = "value")
    private String value;
}
