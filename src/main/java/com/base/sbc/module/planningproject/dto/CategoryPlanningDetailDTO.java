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
     * 品类企划主表的 id
     */
    @ApiModelProperty(value = "品类企划 id")
    private String categoryPlanningId;

    /**
     * 品类 code
     */
    @ApiModelProperty(value = "品类 code")
    private String prodCategoryCode;

    /**
     * 维度数据 id，多选逗号分隔
     */
    @ApiModelProperty(value = "维度数据 id，多选逗号分隔")
    private String dimensionIds;
}
