package com.base.sbc.module.process.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryNodeStatusDto extends Page {

    private String nodeId;
}
