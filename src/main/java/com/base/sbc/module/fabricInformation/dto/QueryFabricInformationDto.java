package com.base.sbc.module.fabricInformation.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("查询面料信息 QueryFabricInformationDto")
public class QueryFabricInformationDto extends Page {

    private String companyCode;

    private String manufacturerNumber;

    private String manufacturer;
}
