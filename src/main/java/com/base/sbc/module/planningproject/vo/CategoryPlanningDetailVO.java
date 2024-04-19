package com.base.sbc.module.planningproject.vo;

import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 品类企划详情查询返回值
 *
 * @author XHTE
 * @create 2024/4/17
 */
@Data
@ApiModel("品类企划详情查询返回值")
public class CategoryPlanningDetailVO {
    /**
     * 品类企划数据
     */
    @ApiModelProperty(value = "品类企划数据")
    private List<CategoryPlanningDetails> categoryPlanningDetailsList;

    /**
     * 品类企划根据 大/品/中/维度类型/维度值 分组后的数据
     */
    @ApiModelProperty(value = "品类企划根据 大/品/中/维度类型/维度值 分组后的数据")
    private List<CategoryPlanningDetails> groupByDimensionalityValueList;

    /**
     * 品类企划数据根据波段分组后的数据
     */
    @ApiModelProperty(value = "品类企划波段数据")
    private List<CategoryPlanningDetails> groupByBandList;

    /**
     * 品类企划数据根据维度分组后的数据
     */
    @ApiModelProperty(value = "品类企划维度数据")
    private List<CategoryPlanningDetails> groupByDimensionalityNameList;
}
