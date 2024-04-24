package com.base.sbc.module.planning.service.impl;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.planning.service.PlanningSummaryService;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanningSummaryServiceImpl implements PlanningSummaryService {
    @Autowired
    private OrderBookDetailService orderBookDetailService;
    @Autowired
    private PlanningProjectPlankService planningProjectPlankService;
    @Autowired
    private PlanningProjectService planningProjectService;
    @Autowired
    private PlanningProjectDimensionService planningProjectDimensionService;

    @Override
    public ApiResult queryList() {


        return null;
    }
}
