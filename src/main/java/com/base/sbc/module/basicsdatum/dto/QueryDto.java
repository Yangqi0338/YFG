package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/*
* 用于基础资料查询dto类
*
* */
@Data
@ApiModel(" QueryDto")
public class QueryDto extends Page {
    @ApiModelProperty(value = "编码"  )
    private String coding;


    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** POM类型 */
    @ApiModelProperty(value = "POM类型"  )
    private String pdmType;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
}
