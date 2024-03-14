package com.base.sbc.module.planningproject.dto;

import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-19 16:08:00
 * @mail 247967116@qq.com
 */
@Data
public class SeasonalPlanningSaveDto extends SeasonalPlanning {
    private String ids;
}
