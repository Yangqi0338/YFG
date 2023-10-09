package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class IdDto {
    @NotBlank(message = "id必填")
    @ApiModelProperty(value = "id")
    private String id;
}
