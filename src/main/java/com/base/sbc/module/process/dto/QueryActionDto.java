package com.base.sbc.module.process.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryActionDto extends Page {

    @ApiModelProperty(value = "动作名称"  )
    private String actionName;
    /** 动作编码 */
    @ApiModelProperty(value = "动作编码"  )
    private String actionCode;
}
