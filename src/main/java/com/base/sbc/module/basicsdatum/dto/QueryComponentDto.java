package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询 QueryComponentDto")
public class QueryComponentDto   extends Page {
    @ApiModelProperty(value = "编码"  )
    private String coding;
}
