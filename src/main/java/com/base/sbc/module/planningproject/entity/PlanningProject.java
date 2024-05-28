package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_project")
@ApiModel("企划规划表 PlanningProject")
public class PlanningProject extends BaseDataEntity<String> {
    /**
     * 品类企划id
     */
    private String categoryPlanningId;

    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String seasonId;

    /**
     * 产品季名称
     */
    @ApiModelProperty(value = "产品季名称")
    private String seasonName;

    /**
     * 企划规划名称
     */
    @ApiModelProperty(value = "企划规划名称")
    private String planningProjectName;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String planningChannelName;

    /**
     * 渠道编码
     */
    @ApiModelProperty(value = "渠道编码")
    private String planningChannelCode;

    /**
     * 是否已匹配
     */
    @ApiModelProperty(value = "是否已匹配:0否,1是")
    private String isMatch;

    /**
     * 1停用、0启用
     */
    @ApiModelProperty(value = "1停用、0启用")
    private String status;
}
