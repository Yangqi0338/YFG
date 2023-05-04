package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/*编辑维度标签*/
@Data
@ApiModel("企划维度-编辑维度标签 SaveUpdateDemandProportionDataDto")
public class UpdateDimensionalityDto {


    @ApiModelProperty(value = "id" ,required = true,example = "")
    @NotBlank(message = "id不能为空")
    private String  id;


    @ApiModelProperty(value = "fieldId" ,required = true,example = "122222")
    @NotBlank(message = "字段管理id不能为空")
    private String  fieldId;

    @ApiModelProperty(value = "维度名称" ,required = true,example = "长度")
    @NotBlank(message = "维度名称id不能为空")
    private String  dimensionalityName;

    @ApiModelProperty(value = "是否检查" ,required = true,example = "0")
    @NotBlank(message = "是否检查不能为空")
    private String  isExamine;

}
