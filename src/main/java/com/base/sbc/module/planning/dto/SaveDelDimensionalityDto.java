package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("企划需求-新增删除维度 SaveDelDimensionality")
public class SaveDelDimensionalityDto {

    @ApiModelProperty(value = "品类id" ,required = true,example = "122222")
    @NotNull(message = "品类id不能为空")
    private String categoryId;

    @ApiModelProperty(value = "产品季id" ,required = true,example = "122222")
    @NotNull(message = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "fieldId" ,required = true,example = "122222")
    @NotNull(message = "字段管理id不能为空")
    private String  fieldId;

    @ApiModelProperty(value = "维度名称" ,required = true,example = "长度")
    @NotNull(message = "维度名称id不能为空")
    private String  dimensionalityName;

    @ApiModelProperty(value = "是否检查" ,required = true,example = "0")
    @NotNull(message = "是否检查不能为空")
    private String  isExamine;

}
