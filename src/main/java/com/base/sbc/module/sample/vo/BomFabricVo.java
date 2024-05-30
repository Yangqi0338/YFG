package com.base.sbc.module.sample.vo;

import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("BOM使用物料")
public class BomFabricVo extends FabricSummary {

    @ApiModelProperty(value = "选择标识，0未选择，1选择")
    private String choiceFlag;

    @ApiModelProperty(value = "款式列表")
    private List<FabricSummaryStyle> fabricSummaryStyles;

    @ApiModelProperty(value = "打印次数")
    private Long printCount;

}
