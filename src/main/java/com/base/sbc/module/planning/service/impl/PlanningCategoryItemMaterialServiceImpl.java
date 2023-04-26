package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.dto.PlanningCategoryItemMaterialSaveDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMaterialMapper;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ZCYLGZ
 */
@Service
public class PlanningCategoryItemMaterialServiceImpl extends ServicePlusImpl<PlanningCategoryItemMaterialMapper, PlanningCategoryItemMaterial> implements PlanningCategoryItemMaterialService {

    @Autowired
    private PlanningCategoryItemService planningCategoryItemService;
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     *
     * @param materialIds
     */
    @Override
    public List<Map<String, Integer>> numList(List<String> materialIds) {
        return this.getBaseMapper().numList(materialIds);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean savePlanningCategoryItemMaterial(PlanningCategoryItemMaterialSaveDto dto) {
        // 删除之前的
        QueryWrapper<PlanningCategoryItemMaterial> qw=new QueryWrapper<>();
        qw.eq("planning_category_item_id", dto.getId());
        remove(qw);
        //保存
        if(CollUtil.isNotEmpty(dto.getItem())){
            saveBatch(dto.getItem());
        }
        //修改关联数量
        UpdateWrapper<PlanningCategoryItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", dto.getId());
        updateWrapper.set("material_count", Optional.ofNullable(dto.getItem()).map(List::size).orElse(0));
        planningCategoryItemService.update(updateWrapper);
        return true;
    }
}
