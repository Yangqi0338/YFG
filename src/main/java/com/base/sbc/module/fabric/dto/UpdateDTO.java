package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("更新DTO")
public class UpdateDTO {
    @ApiModelProperty("更新id")
    @NotEmpty(message = "更新id不可为空")
    private List<String> ids;
}
