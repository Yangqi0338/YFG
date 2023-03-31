package com.base.sbc.pdm.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品企划-产品季查询 PlanningSeasonSaveDto")
public class PlanningSeasonSearchDto extends Page {

    @ApiModelProperty(value = "年份" ,required = false,example = "2023")
    private String year;
}
