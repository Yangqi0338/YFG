package com.base.sbc.module.sample.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PreProductionSampleTaskPageSumVo {

    private BigDecimal sampleMakingScoreSum;

    private BigDecimal sampleQualityScoreSum;

}
