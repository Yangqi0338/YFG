package com.base.sbc.module.patternmaking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("样衣所需的制版单列表 SampleDesignForSampleVo")
public class PatternMakingForSampleVo {

    @ApiModelProperty(value = "制版单id")
    private String id;

    @ApiModelProperty(value = "打版编码")
    private String code;

    @ApiModelProperty(value = "打版类型（样衣版）")
    private String sampleType;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @ApiModelProperty(value = "设计款号")
    private String designNo;



    @ApiModelProperty(value = "季节ID")
    private String seasonId;

    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "设计师ID")
    private String designerId;

    @ApiModelProperty(value = "设计师名称")
    private String designer;

    @ApiModelProperty(value = "版师（纸样师）ID")
    private String patternDesignId;

    @ApiModelProperty(value = "版师（纸样师")
    private String patternDesignName;

    @ApiModelProperty(value = "样衣设计ID")
    private String sampleDesignId;
}
