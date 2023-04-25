package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：企划-产品季 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningSeasonService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08
 */
@Service
public class PlanningSeasonServiceImpl extends ServicePlusImpl<PlanningSeasonMapper, PlanningSeason> implements PlanningSeasonService {
    @Override
    public boolean del(String companyCode, String id) {
        return removeById(id);
    }

    @Override
    public List<PlanningSeason> selectProductSeason(QueryWrapper qw) {
        return getBaseMapper().selectProductSeason(qw);
    }

    @Override
    public List<PlanningSeason> queryYs(String companyCode) {
        return getBaseMapper().queryYs(companyCode);
    }
}
