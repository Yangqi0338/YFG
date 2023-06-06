package com.base.sbc.module.process.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryStatusDto  extends Page {

    @ApiModelProperty(value = "状态名称"  )
    private String statusName;
}
