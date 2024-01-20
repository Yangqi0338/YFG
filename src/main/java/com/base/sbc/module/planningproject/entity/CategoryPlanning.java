package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 品类企划
 * @author 卞康
 * @date 2024-01-19 17:18:11
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_category_planning")
public class CategoryPlanning extends BaseDataEntity<String> {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "渠道编码")
    private String  channelCode;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "产品季id")
    private String  seasonId;
    @ApiModelProperty(value = "产品季名称")
    private String seasonName;

    @ApiModelProperty(value = "状态（0：启用，1：未启用）")
    private String status;

    private String seasonalPlanningId;
}
