package com.base.sbc.module.planningproject.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-22 14:02:55
 * @mail 247967116@qq.com
 */
@Data
public class CategoryPlanningDetailsQueryDto extends BaseDto {
    @ApiModelProperty(value = "品类企划Id")
    private String  categoryPlanningId;
    @ApiModelProperty(value = "季节企划Id")
    private String  seasonalPlanningId;
    private String prodCategoryNames;
}
