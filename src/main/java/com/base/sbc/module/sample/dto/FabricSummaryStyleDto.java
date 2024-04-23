package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabricSummaryStyleDto {

    private String id;

    @ApiModelProperty(value = "排序"  )
    private Integer sort;

    @ApiModelProperty(value = "总投产-投产件数"  )
    private String totalProduction;

    @ApiModelProperty(value = "是否撞色,0否 1是"  )
    private String colorCrash;

    @ApiModelProperty(value = "款式版本号"  )
    private Integer fabricSummaryStyleVersion;

    @ApiModelProperty(value = "单件用量"  )
    private BigDecimal unitUse;

    @ApiModelProperty(value = "备注"  )
    private String remarks;


}
