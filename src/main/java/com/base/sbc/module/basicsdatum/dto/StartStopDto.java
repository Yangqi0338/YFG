package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("启用停止 StartStopDto")
public class StartStopDto {

    @ApiModelProperty(name = "物料编码")
    private String  materialCode;

    @ApiModelProperty(name = "编号", value = "多个编号加,",  required = true, dataType = "String", example = "11,22")
    @NotNull(message = "编号必填")
    private String ids;

    @ApiModelProperty(name = "名称", value = "多个名称加," )
    private String  names;

    @ApiModelProperty(name = "编码", value = "多个编码加,")
    private String codes;

    @ApiModelProperty(name = "模块名称", value = "多个编码加,")
    private String  name;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;

    private String parentId;

}
