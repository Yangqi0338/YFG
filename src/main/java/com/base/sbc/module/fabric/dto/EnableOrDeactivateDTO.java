package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 启动或停用
 */
@Data
@ApiModel("启动或停用")
public class EnableOrDeactivateDTO {
    @ApiModelProperty("ids")
    @NotNull(message = "入参id不可为空")
    private List<String> ids;

    @ApiModelProperty("状态：0正常,1停用")
    @NotNull(message = "状态不可为空")
    private String status;
}
