package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMaterialMapper;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZCYLGZ
 */
@Service
public class PlanningCategoryItemMaterialServiceImpl extends ServicePlusImpl<PlanningCategoryItemMaterialMapper, PlanningCategoryItemMaterial> implements PlanningCategoryItemMaterialService {
    @Override
    public List<PlanningCategoryItemMaterial> selectByQw(QueryWrapper<PlanningCategoryItemMaterial> qw) {
        return getBaseMapper().selectByQw(qw);
    }
}
