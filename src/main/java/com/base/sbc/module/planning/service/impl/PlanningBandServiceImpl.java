package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.mapper.PlanningBandMapper;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：波段企划 service
 * @address com.base.sbc.module.planning.service.impl.PlanningBandServiceImpl
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-14 15:00
 * @version 1.0
 */
@Service
public class PlanningBandServiceImpl extends ServicePlusImpl<PlanningBandMapper, PlanningBand> implements PlanningBandService {


    @Override
    public boolean del( String id) {

        this.removeById(id);

        // 删除 品类信息,坑位信息,关联素材库
//        planningCategoryService.delByPlanningBand(companyCode, id);
        return false;
    }

    @Override
    public List<PlanningSeasonBandVo> selectByQw(QueryWrapper<PlanningBand> qw) {
        return getBaseMapper().selectByQw(qw);
    }
}
