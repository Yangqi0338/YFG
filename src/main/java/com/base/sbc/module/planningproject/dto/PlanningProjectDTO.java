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
     * 类型（1-大类 2-品类 3-中类）
     */
    @ApiModelProperty(value = "类型（1-大类 2-品类 3-中类）")
    private Integer type;

    /**
     * 父类大类/品类 code
     */
    @ApiModelProperty(value = "品类类型（父类大类/品类 code")
    private String parentCode;

    /**
     * 品类 code
     */
    @ApiModelProperty(value = "品类 code")
    private String prodCategoryCode;

}
