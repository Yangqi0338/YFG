package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/*新增删除需求维度*/
@Data
@ApiModel("企划需求-新增删除需求 SaveDelDemandDto")
public class SaveDelDemandDto {

    /*产品季id*/
    @ApiModelProperty(value = "产品季id" ,required = true,example = "111")
    @NotBlank(message = "产品季id不能为空")
    private String planningSeasonId;

    @ApiModelProperty(value = "品类id" ,required = true,example = "122222")
    @NotBlank(message = "品类id不能为空")
    private String categoryId;

    @ApiModelProperty(value = "需求占比" ,  required = true ,example = "111")
    @NotBlank(message = "需求名称")
    private String demandName;

    @ApiModelProperty(value = "formTypeId" ,required = true,example = "122222")
    @NotNull(message = "表单id不能为空")
    private String  formTypeId;

}
