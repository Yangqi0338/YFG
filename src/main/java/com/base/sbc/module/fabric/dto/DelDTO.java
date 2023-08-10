package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("删除DTO")
public class DelDTO {
    @ApiModelProperty("删除id")
    @NotNull(message = "删除id不可为空")
    private List<String> ids;
}
