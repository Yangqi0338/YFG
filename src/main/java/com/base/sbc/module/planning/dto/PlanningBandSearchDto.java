package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品企划-波段列表查询 PlanningBandSearchDto")
public class PlanningBandSearchDto extends Page {

    @ApiModelProperty(value = "年份" ,required = false,example = "2023")
    private String year;

    @ApiModelProperty(value = "产品季名称" ,required = true,example = "23年春常规产品企划")
    private String planningSeasonName;
}
