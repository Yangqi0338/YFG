package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品企划-产品季新增 PlanningSeasonSaveDto")
public class PlanningSeasonSaveDto {
    @ApiModelProperty(value = "编号" ,required = false,example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "品牌" ,required = true,example = "0" )
    private String brand;
    @ApiModelProperty(value = "名称" ,required = true,example = "23年秋常规产品企划")
    private String name;
    @ApiModelProperty(value = "年份" ,required = true,example = "2023")
    private String year;
    @ApiModelProperty(value = "季节" ,required = true,example = "冬")
    private String season;
    @ApiModelProperty(value = "月份" ,required = true,example = "11月")
    private String month;
    @ApiModelProperty(value = "备注" )
    private String remarks;

}
