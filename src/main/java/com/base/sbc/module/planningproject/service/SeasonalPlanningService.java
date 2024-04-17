package com.base.sbc.module.planningproject.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-18 17:07:38
 * @mail 247967116@qq.com
 */
public interface SeasonalPlanningService extends BaseService<SeasonalPlanning> {

    ApiResult importSeasonalPlanningExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto);

    ApiResult getSeasonalPlanningDetails(SeasonalPlanningDetails seasonalPlanningDetails);

    @Deprecated
    void importExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws IOException;

    List<SeasonalPlanningVo> queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto);

    List<SeasonalPlanningVo> queryPage(SeasonalPlanningQueryDto seasonalPlanningQueryDto);

    SeasonalPlanningVo getDetailById(String id);
}
