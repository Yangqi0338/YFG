package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：产前样-任务
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样-任务 PreProductionSampleTaskDto")
public class PreProductionSampleTaskDto extends PreProductionSampleTask {


    @ApiModelProperty(value = "产前样id")
    @NotBlank(message = "产前样id不能为空")
    private String preProductionSampleId;

}
