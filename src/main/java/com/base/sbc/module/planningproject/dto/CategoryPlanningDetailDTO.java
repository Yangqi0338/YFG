package com.base.sbc.module.planningproject.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    /**
     * "prodCategory2nd":""
     * "dimensionId":""
     * 中类和维度id
     */
    @ApiModelProperty(value = "中类和维度id")
    private List<Map<String, String>> queryList;
}
