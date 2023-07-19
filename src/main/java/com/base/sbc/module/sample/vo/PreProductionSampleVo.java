package com.base.sbc.module.sample.vo;

import com.base.sbc.module.sample.entity.PreProductionSample;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样 PreProductionSample")
public class PreProductionSampleVo extends PreProductionSample {

    @ApiModelProperty(value = "任务列表")
    private List<PreProductionSampleTaskVo> taskList;

}
