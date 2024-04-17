package com.base.sbc.module.planningproject.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 品类企划详情查询条件
 *
 * @author XHTE
 * @create 2024/4/17
 */
@Data
@ApiModel("品类企划详情查询条件")
public class CategoryPlanningDetailDTO {

    /**
     * 品类企划主表的id
     */
    @ApiModelProperty(value = "品类企划id")
    private String categoryPlanningId;

    /**
     * 品类，多选逗号分隔
     */
    @ApiModelProperty(value = "品类，多选逗号分隔")
    private String prodCategoryCodes;

    /**
     * 字段管理的id，多选逗号分隔
     */
    @ApiModelProperty(value = "字段管理的id，多选逗号分隔")
    private String fieldIds;
}
