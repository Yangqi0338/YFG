package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class RelevanceBomDto {

    @NotBlank(message = "配色id不能为空")
    @ApiModelProperty(value = "配色id")
    private String styleColorId;

    @NotBlank(message = "资料包id不能为空")
    @ApiModelProperty(value = "资料包id")
    private String  packInfoId;
}
