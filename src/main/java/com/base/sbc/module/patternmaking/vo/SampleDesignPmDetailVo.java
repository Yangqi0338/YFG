package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.sample.vo.SampleDesignVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣设计+打板指令明细vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 15:25
 */
@Data
@ApiModel("样衣设计+打板指令明细 SampleDesignPmDetailVo ")
public class SampleDesignPmDetailVo extends SampleDesignVo {

    @ApiModelProperty(value = "打版指令")
    private PatternMakingVo patternMaking;


}
