package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增修改尺码标签Dto类
 * */
@Data
public class AddRevampSizeLabelDto {
    private String id;
    @ApiModelProperty(value = "标签名称"  )
    private String labelName;
}
