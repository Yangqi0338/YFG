package com.base.sbc.module.planningproject.vo;

import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-22 14:10:53
 * @mail 247967116@qq.com
 */
@Data
public class CategoryPlanningDetailsVo extends CategoryPlanningDetails {
    private String seasonId;
    private String channelCode;
    private String channelName;
    /**
     * 产品季名称
     */
    @ApiModelProperty(value = "产品季名称")
    private String seasonName;

    private String categoryPlanningStatus;
}
