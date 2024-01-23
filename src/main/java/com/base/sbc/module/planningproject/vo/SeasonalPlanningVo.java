package com.base.sbc.module.planningproject.vo;

import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import lombok.Data;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 16:43:50
 * @mail 247967116@qq.com
 */
@Data
public class SeasonalPlanningVo extends SeasonalPlanning {
    List<SeasonalPlanningDetails> seasonalPlanningDetailsList;
}
