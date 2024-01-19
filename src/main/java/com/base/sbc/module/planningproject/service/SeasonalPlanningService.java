package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 卞康
 * @date 2024-01-18 17:07:38
 * @mail 247967116@qq.com
 */
public interface SeasonalPlanningService extends BaseService<SeasonalPlanning> {
    void importExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws IOException;
}
