/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：企划-坑位信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */
@Service
public class PlanningCategoryItemServiceImpl extends ServicePlusImpl<PlanningCategoryItemMapper, PlanningCategoryItem> implements PlanningCategoryItemService {

    @Autowired
    PlanningSeasonService planningSeasonService;
    @Autowired
    PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    @Autowired
    CcmService ccmService;

    /**
     * 保存坑位信息
     *
     * @return
     */
    @Transactional(readOnly = false)
    public int saveCategoryItem(PlanningBand band, PlanningCategory category,List<PlanningCategoryItem> dbCategoryItemList) {
        //通过企划需求数生成
        BigDecimal planRequirementNum = category.getPlanRequirementNum();
        String companyCode = band.getCompanyCode();
        //坑位信息条件
        QueryCondition categoryItemQc = new QueryCondition(companyCode);
        categoryItemQc.andEqualTo("planning_category_id", category.getId());
        //删除之前数据
       delByPlanningCategory(companyCode,CollUtil.newArrayList(category.getId()));
        if (planRequirementNum == null||planRequirementNum.intValue()==0) {
            return 0;
        }
        PlanningSeason planningSeason = planningSeasonService.getById(band.getPlanningSeasonId());
        IdGen idGen = new IdGen();
        int insertCount = 0;
        //编码规则条件
        Map<String, String> params = new HashMap<>(12);
        params.put("brand", planningSeason.getBrand());
        params.put("year", planningSeason.getYear());
        params.put("season", planningSeason.getSeason());
        params.put("category", getCategory(category.getCategoryName()));
        params.put("designCode", getDesignCode(category.getManager()));
        for (int i = 0; i < planRequirementNum.intValue(); i++) {
            PlanningCategoryItem item = new PlanningCategoryItem();
            item.setCompanyCode(companyCode);
            item.preInsert(idGen.nextIdStr());
            item.preUpdate();
            item.setPlanningSeasonId(category.getPlanningSeasonId());
            item.setPlanningBandId(category.getPlanningBandId());
            item.setPlanningCategoryId(category.getId());
            GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
            String designCode = Optional.ofNullable(CollUtil.get(dbCategoryItemList, i)).map(PlanningCategoryItem::getDesignNo).orElse(getMaxCode.genCode("PLANNING_DESIGN_NO", params));
            System.out.println("planningDesignNo:" + designCode);
            item.setDesignNo(designCode);
            insertCount += save(item)?1:0;

        }
        return insertCount;
    }
    @Transactional(readOnly = false)
    public boolean delByPlanningCategory(String companyCode,List<String> ids){
        //删除坑位信息
        remove(new QueryWrapper<PlanningCategoryItem>().in("planning_category_id",ids));

        // 删除坑位信息关联的素材库
        planningCategoryItemMaterialService.remove(new QueryWrapper<PlanningCategoryItemMaterial>().in("planning_category_id",ids));
        return true;
    }

    private String getDesignCode(String manager) {
        if (StrUtil.isBlank(manager)) {
            throw new OtherException("获取设计师编码失败");
        }
        try {
            return manager.split(StrUtil.COMMA)[1];
        } catch (Exception e) {
            throw new OtherException("获取设计师编码失败");
        }
    }

    private String getCategory(String categoryName) {
        if (StrUtil.isBlank(categoryName)) {
            throw new OtherException("未选择品类,不能生成设计款号");
        }
        String categoryCode = null;
        try {
            categoryCode = categoryName.split(StrUtil.SLASH)[1].split(StrUtil.COMMA)[1];
        } catch (Exception e) {
            throw new OtherException("品类编码获取失败");
        }
        return categoryCode;

    }

    @Transactional(readOnly = false)
    public boolean delByPlanningBand(String userCompany, String id) {
        //删除坑位信息
        remove(new QueryWrapper<PlanningCategoryItem>().eq("planning_band_id",id));
        // 删除坑位信息关联的素材库
        planningCategoryItemMaterialService.remove(new QueryWrapper<PlanningCategoryItemMaterial>().eq("planning_band_id",id));
        return true;
    }

    @Override
    public String selectMaxDesignNo(QueryWrapper qc) {
        return getBaseMapper().selectMaxDesignNo(qc);
    }

    @Override
    public List<String> selectCategoryIdsByBand(QueryWrapper qw) {
        return getBaseMapper().selectCategoryIdsByBand(qw);
    }
}
