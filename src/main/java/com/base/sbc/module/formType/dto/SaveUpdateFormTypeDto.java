package com.base.sbc.module.formType.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("保存修改表单类型 SaveUpdateFormTypeDto")
public class SaveUpdateFormTypeDto {

    @ApiModelProperty(name = "编号id", value = "编号:为空时新增、不为空时修改",  required = false, example = "")
    private String id;

    @ApiModelProperty(value = "数据库表名",  required = true, example = "名称")
    @NotBlank(message = "数据库表名必填")
    private String name;

    @ApiModelProperty(value = "分组id",  required = true, example = "分组id")
    @NotBlank(message = "分组必填")
    private String groupId;

    @ApiModelProperty(value = "编码",  required = true, example = "编码")
//    @NotBlank(message = "编码必填")
    private String coding;

    @ApiModelProperty(value = "表说明",  required = true, example = "表说明")
    private String tableExplain;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;
}
