package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoefficientTemplateDetailsVo extends BasicsdatumCoefficientTemplate {


    @ApiModelProperty(value = "纬度系数列表"  )
    private List<BasicsdatumDimensionalityVo> list;
}
