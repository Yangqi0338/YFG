package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 设置任务等级 dto
 * 类描述：
 * @address com.base.sbc.module.planning.dto.SetTaskLevelDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-21 10:59
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-设置任务等级 SetTaskLevelDto")
public class SetTaskLevelDto {
    @ApiModelProperty(value = "坑位编号", example = "689467740238381056", required = true)
    @NotBlank(message = "坑位编号不能为空")
    private String id;
    @NotBlank(message = "任务等级不能为空")
    @ApiModelProperty(value = "任务等级", example = "0", required = true)
    private String taskLevel;
    @NotBlank(message = "任务等级不能为空")
    @ApiModelProperty(value = "任务等级名称", example = "0", required = true)
    private String taskLevelName;
}
