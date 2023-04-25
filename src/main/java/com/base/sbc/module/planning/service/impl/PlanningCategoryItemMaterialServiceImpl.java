package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMaterialMapper;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ZCYLGZ
 */
@Service
public class PlanningCategoryItemMaterialServiceImpl extends ServicePlusImpl<PlanningCategoryItemMaterialMapper, PlanningCategoryItemMaterial> implements PlanningCategoryItemMaterialService {

    /**
     * 根据传入的素材id列表查询对应收藏的数量
     *
     * @param materialIds
     */
    @Override
    public List<Map<String, Integer>> numList(List<String> materialIds) {
        return this.getBaseMapper().numList(materialIds);
    }
}
