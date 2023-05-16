package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddRevampTechnologyDto {

    private String id;

    @ApiModelProperty(value = "编码"  )
    private String coding;

    @ApiModelProperty(value = "工艺项目"  )
    private String technologyProject;

    @ApiModelProperty(value = "描述"  )
    private String description;

    @ApiModelProperty(value = "图片"  )
    private String image;

    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
