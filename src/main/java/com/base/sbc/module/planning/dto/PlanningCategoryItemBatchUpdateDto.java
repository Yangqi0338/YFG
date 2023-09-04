package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：商品企划-坑位信息批量编辑dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningCategoryItemBatchUpdateDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-19 09:29
 */
@Data
@ApiModel("商品企划-坑位信息批量编辑dto PlanningCategoryItemBatchUpdateDto")
public class PlanningCategoryItemBatchUpdateDto {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private List<String> ids;
    @ApiModelProperty(value = "设计师名称", required = false)
    private String designer;

    @ApiModelProperty(value = "设计师id", required = false)
    private String designerId;
    @ApiModelProperty(value = "任务等级", required = false)
    private String taskLevel;
    @ApiModelProperty(value = "计划完成时间", required = false)
    private String planningFinishDate;
    @ApiModelProperty(value = "样衣需求完成时间")
    private String demandFinishDate;

    @NotBlank(message = "系列不能为空")
    @ApiModelProperty(value = "系列id", example = "0", required = true)
    private String seriesId;
    @NotBlank(message = "系列不能为空")
    @ApiModelProperty(value = "系列名称", example = "0", required = true)
    private String series;
}
