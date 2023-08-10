package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("开发配置保存DTO")
public class FabricDevConfigInfoSaveDTO {
    @ApiModelProperty(value = "开发id")
    private String id;
    /**
     * 开发名称
     */
    @ApiModelProperty(value = "开发名称")
    @NotBlank(message = "开发名称不可为空")
    private String devName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
}
