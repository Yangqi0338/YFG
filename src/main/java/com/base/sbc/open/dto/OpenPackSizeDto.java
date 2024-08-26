package com.base.sbc.open.dto;

import com.base.sbc.module.pack.dto.PackSizeDto;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("资料包-尺寸表dto OpenPackSizeDto")
public class OpenPackSizeDto {

    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    @ApiModelProperty(value = "尺码表数据集合")
    private List<PackSizeDto> sizeDtos;
    @ApiModelProperty(value = "尺码配置对象")
    private PackSizeConfig config;

}
