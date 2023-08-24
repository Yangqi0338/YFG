package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*修改排序下表*/
@Data
public class RevampSortDto {
    @ApiModelProperty(value = "bom模板id")
    private String bomTemplateId;

    @ApiModelProperty(value = "当前位置数据id")
    private String currentId;

    @ApiModelProperty(value = "目标位置数据id")
    private String targetId;
}
