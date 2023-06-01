package com.base.sbc.module.patternmaking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternDesignSampleTypeCountVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-01 17:09
 */
@Data
@ApiModel("版师打版类型数量统计 PatternDesignSampleTypeCountVo ")
public class PatternDesignSampleTypeQtyVo {


    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    @ApiModelProperty(value = "打样类型")
    private String sampleType;
    @ApiModelProperty(value = "数量")
    private Long quantity;

}
