package com.base.sbc.module.planningproject.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企划看板查询条件
 *
 * @author XHTE
 * @create 2024/4/23
 */
@Data
@ApiModel("企划看板查询条件")
public class PlanningProjectDTO {

    /**
     * 企划看板主表 id
     */
    @ApiModelProperty(value = "企划看板主表 id")
    private String planningProjectId;

    /**
     * 品类 code
     */
    @ApiModelProperty(value = "品类 code")
    private String prodCategoryCode;

}
