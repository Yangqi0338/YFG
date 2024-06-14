package com.base.sbc.module.patternmaking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author js
 * @Date: 2024-03-25 9:58
 */
@Data
@ApiModel("样板评分 PatternMakingScoreVo ")
public class PatternMakingScoreVo {

    /**
     * 样衣工工作量评分
     */
    @ApiModelProperty(value = "样衣工工作量评分")
    private BigDecimal sampleMakingScoreSum;
    /**
     * 样衣工质量评分
     */
    @ApiModelProperty(value = "样衣工质量评分")
    private BigDecimal sampleMakingQualityScoreSum;
    /**
     * 版师工作量评分
     */
    @ApiModelProperty(value = "版师工作量评分")
    private BigDecimal patternMakingScoreSum;

    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementNum;
}
