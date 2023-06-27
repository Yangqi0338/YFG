package com.base.sbc.module.formType.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FormDeleteDto {
    @ApiModelProperty(value = "类型",  required = true, example = "0")
    private Boolean type;

    @ApiModelProperty(name = "编号", value = "编号:多个数组",  required = true, dataType = "String[]", example = "[111,22]")
    @NotNull(message = "编号必填")
    private String[] ids;
}
