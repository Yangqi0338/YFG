package com.base.sbc.module.sample.vo;


import com.base.sbc.module.style.vo.StyleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：任务明细
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-19 10:18
 */

@Data
@ApiModel("产前样任务明细 PreProductionSampleTaskDetailVo ")
public class PreProductionSampleTaskDetailVo {

    @ApiModelProperty(value = "款式设计信息")
    private StyleVo style;

    @ApiModelProperty(value = "任务信息")
    private PreProductionSampleTaskVo task;

}
