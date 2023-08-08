package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryFabricIngredientsInfoDto extends Page {

    @ApiModelProperty(value = "发起")
    private String originate;

    @ApiModelProperty(value = "品类")
    private String categoryId;

    /** 开发类型名称 */
    @ApiModelProperty(value = "开发类型名称"  )
    private String devTypeName;
}
