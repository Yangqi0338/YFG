package com.base.sbc.module.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class MaterialEnableDto {

    private String id;

    @ApiModelProperty(value = "1启用，0停用")
    @Pattern(regexp = "[01]", message = "值不对,1启用,0未启用")
    private String enableFlag;

}
