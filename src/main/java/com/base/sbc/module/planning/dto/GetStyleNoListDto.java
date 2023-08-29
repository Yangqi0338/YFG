package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("企划看板-获取大货款号 查询条件")
public class GetStyleNoListDto {

    @ApiModelProperty(value = "品类code"  )
    private String prodCategory;

    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;

    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    @ApiModelProperty(value = "渠道id"  )
    private String planningChannelId;

    @ApiModelProperty(value = "渠道"  )
    private String channel;
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;

    @ApiModelProperty(value = "维度信息")
    private Map<String,String> dimension;
}
