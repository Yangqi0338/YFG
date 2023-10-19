package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("产品季总览-同步需求占比到坑位 SyncToSeatDto")
public class SyncToSeatDto {
    @ApiModelProperty(value = "id")
    private List<String> ids;

    @ApiModelProperty(value = "渠道企划id")
    private String planningChannelId;
}
