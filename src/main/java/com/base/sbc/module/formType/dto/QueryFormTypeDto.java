package com.base.sbc.module.formType.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryFormTypeDto extends Page {


    private String  companyCode;

    @ApiModelProperty(value = "名")
    private String name;
}
