package com.base.sbc.module.fabricInformation.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("查询面料信息 QueryFabricInformationDto")
public class QueryFabricInformationDto extends Page {
    private String companyCode;

    /*厂家编号*/
    private String manufacturerNumber;

    /*厂家*/
    private String manufacturer;

    /*0查询我发起的 1我接受的*/
    private String originate;

    /*用户id*/
    private  String userId;

    /*岗位id*/
    private List<String> jobIdList;

}
