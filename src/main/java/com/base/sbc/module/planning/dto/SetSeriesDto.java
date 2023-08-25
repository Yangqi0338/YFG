package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 设置任务等级 dto
 * 类描述：
 * @address com.base.sbc.module.planning.dto.SetTaskLevelDto
 * @author cxl
 * @date 创建时间：2023-08-25 17:39
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-设置系列 SetTaskLevelDto")
public class SetSeriesDto {
    @ApiModelProperty(value = "坑位编号", example = "689467740238381056", required = true)
    @NotBlank(message = "坑位编号不能为空")
    private String id;
    @NotBlank(message = "系列不能为空")
    @ApiModelProperty(value = "系列id", example = "0", required = true)
    private String seriesId;
    @NotBlank(message = "系列不能为空")
    @ApiModelProperty(value = "系列名称", example = "0", required = true)
    private String series;
}
