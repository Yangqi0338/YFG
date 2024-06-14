package com.base.sbc.module.planningproject.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-19 16:41:39
 * @mail 247967116@qq.com
 */
@Data
public class SeasonalPlanningQueryDto extends Page {
    private String seasonId;
    private String channelCode;
    private String yearName;
    @ApiModelProperty(value = "产品季名称")
    private String seasonName;
}
