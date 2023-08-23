package com.base.sbc.module.fabric.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xhj
 * @Date 2023/6/26 17:53
 */
@Data
@ApiModel(value = "面料开发查询")
public class FabricDevSearchDTO extends Page {
    @ApiModelProperty("来源1.新增、2.其他")
    private String source;
    private String companyCode;

}
