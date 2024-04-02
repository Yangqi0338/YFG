package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabricSummaryStyleDto {

    private String id;

    @ApiModelProperty(value = "排序"  )
    private Integer sort;

    @ApiModelProperty(value = "总投产"  )
    private String totalProduction;


}
