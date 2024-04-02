package com.base.sbc.module.sample.vo;


import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("面料对应款式")
public class FabricStyleVo extends FabricSummaryStyle {

    @ApiModelProperty(value = "选择标识，0未选择，1选择")
    private String choiceFlag;
}
