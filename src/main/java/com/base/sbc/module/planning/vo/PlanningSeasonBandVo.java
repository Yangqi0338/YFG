package com.base.sbc.module.planning.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品企划-产品季波段信息 PlanningSeasonBandVo")
public class PlanningSeasonBandVo {
    @ApiModelProperty(value = "波段企划id" ,example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "波段企划信息")
    private PlanningBandVo band;
    @ApiModelProperty(value = "产品季信息")
    private PlanningSeasonVo season;
}
