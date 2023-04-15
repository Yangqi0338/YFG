package com.base.sbc.module.formType.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PagingFormTypeVo {
    @ApiModelProperty(value = "编号" ,example = "")
    private String id;

    @ApiModelProperty(value = "名称" ,example = "")
    private String groupName;

    @ApiModelProperty(value = "状态" ,example = "")
    private String status;

    private List<FormTypeVo> list;
}
