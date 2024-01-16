package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddUpdateCoefficientTemplateDto extends BasicsdatumCoefficientTemplate {

    /** 纬度系数表新增 */
    @ApiModelProperty(value = "纬度系数表新增"  )
    private List<BasicsdatumDimensionalityDto> List;
}
