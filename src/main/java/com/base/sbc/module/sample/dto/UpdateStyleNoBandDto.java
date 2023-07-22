package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UpdateStyleNoBandDto {
    @ApiModelProperty(value = "配色id", example = "")
    @NotBlank(message = "编号不能为空")
    private String id;
    @ApiModelProperty(value = "大货款号", example = "10")
//    @NotBlank(message = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "波段", example = "10")
    private String bandCode;
    @ApiModelProperty(value = "波段", example = "10")
    private String bandName;
}
