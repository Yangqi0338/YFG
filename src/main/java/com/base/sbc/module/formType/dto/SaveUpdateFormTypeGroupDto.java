package com.base.sbc.module.formType.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;



@Data
@ApiModel("保存修改表单分组 FormTypeGroupDto")
public class SaveUpdateFormTypeGroupDto {

    @ApiModelProperty(name = "编号id", value = "编号:为空时新增、不为空时修改",  required = false, example = "")
    private String id;

    @ApiModelProperty(value = "分组名称",  required = true, example = "名称")
    @NotBlank(message = "分组名称必填")
    private String groupName;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;
}
