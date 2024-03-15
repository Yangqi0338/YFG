package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-03-11 9:32:35
 * @mail 247967116@qq.com
 * 企划看板维度维度
 */
@Data
@TableName("t_planning_project_plank_dimension")
public class PlanningProjectPlankDimension extends BaseDataEntity<String> {
    /**
     * 企划看板维度id
     */
    @ApiModelProperty(value = "企划看板维度id")
    private String planningProjectPlankId;


    /** 维度名称 */
    @ApiModelProperty(value = "维度名称"  )
    private String dimensionName;

    /** 维度编码 */
    @ApiModelProperty(value = "维度编码"  )
    private String dimensionCode;

    /** 维度值 */
    @ApiModelProperty(value = "维度值"  )
    private String dimensionValue;

}
