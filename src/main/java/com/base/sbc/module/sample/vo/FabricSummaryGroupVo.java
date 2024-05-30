package com.base.sbc.module.sample.vo;

import com.base.sbc.module.fabricsummary.entity.FabricSummaryGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FabricSummaryGroupVo extends FabricSummaryGroup {

    @ApiModelProperty(value ="所属部门")
    private String department;

}
