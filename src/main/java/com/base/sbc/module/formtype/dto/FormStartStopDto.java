package com.base.sbc.module.formtype.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("启用停止表单 FormStartStopDto")
public class FormStartStopDto {

    @ApiModelProperty(value = "类型",  required = true, example = "0")
    private Boolean type;

    @ApiModelProperty(name = "编号", value = "编号:多个数组",  required = true, dataType = "String[]", example = "[111,22]")
    @NotNull(message = "编号必填")
    private String[] ids;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;


}
