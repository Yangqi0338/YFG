package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("颜色企划")
public class ColorPlanningSearchDTO extends Page {

    @ApiModelProperty(value = "年份编码")
    private String yearCode;
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    private String companyCode;


}
