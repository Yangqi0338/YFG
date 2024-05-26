package com.base.sbc.module.planningproject.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-19 17:23:32
 * @mail 247967116@qq.com
 */
@Data
public class CategoryPlanningQueryDto extends Page {
    private String channelCode;
    private String seasonId;
    private String yearName;
}
