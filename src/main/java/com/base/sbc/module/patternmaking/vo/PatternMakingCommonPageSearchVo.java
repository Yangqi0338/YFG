package com.base.sbc.module.patternmaking.vo;


import com.github.pagehelper.PageInfo;
import lombok.Data;

@Data
public class PatternMakingCommonPageSearchVo extends PageInfo {
    private PatternMakingScoreVo patternMakingScoreVo;
}
