package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.planning.dto.PlanningSummaryQueryDto;

public interface PlanningSummaryService {
    ApiResult queryList(PlanningSummaryQueryDto planningSummaryQueryDto);
}
