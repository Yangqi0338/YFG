/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.dao.PlanningCategoryDao;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** 
 * 类描述：企划-品类信息 service类
 * @address com.base.sbc.module.planning.service.PlanningCategoryService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-29 10:36:59
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class PlanningCategoryService extends BaseService<PlanningCategory> {
	private IdGen idGen = new IdGen();
	@Autowired
	private PlanningCategoryDao planningCategoryDao;
	@Resource
	private PlanningCategoryItemService planningCategoryItemService;
	@Resource
	private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
	@Override
	protected BaseDao<PlanningCategory> getEntityDao() {
		return planningCategoryDao;
	}

	/**
	 * 保存 波段企划-品类信息
	 * @param band
	 * @param categoryList
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean savePlanningCategory(PlanningBand band,List<PlanningCategory> categoryList){
		QueryCondition categoryQc=new QueryCondition(band.getCompanyCode());
		categoryQc.andEqualTo("planning_band_id",band.getId());
		if(CollUtil.isEmpty(categoryList)){
			deleteByConditionDelFlag(categoryQc);
			planningCategoryItemService.deleteByConditionDelFlag(categoryQc);
			planningCategoryItemMaterialService.deleteByConditionDelFlag(categoryQc);
			return true;
		}
		List<PlanningCategory> dbCategoryList=findByConditionAndDelNormal(categoryQc);
		List<PlanningCategoryItem> dbCategoryItemList = planningCategoryItemService.findByConditionAndDelNormal(categoryQc);
		Map<String, PlanningCategory> categoryMap = Optional.ofNullable(dbCategoryList).orElse(new ArrayList<>())
				.stream().collect(Collectors.toMap(PlanningCategory::getId, v -> v, (a, b) -> b));
		Map<String, List<PlanningCategoryItem>> categoryItemMap=Optional.ofNullable(dbCategoryItemList).orElse(new ArrayList<>())
				.stream().collect(Collectors.groupingBy(PlanningCategoryItem::getPlanningCategoryId));
		List<PlanningCategory> updateList=new ArrayList<>(12);
		List<PlanningCategory> saveList=new ArrayList<>(12);
		boolean addSub=false;
		for (PlanningCategory category : categoryList) {
			category.setPlanningBandId(band.getId());
			category.setPlanningSeasonId(band.getPlanningSeasonId());
			PlanningCategory dbCategory = categoryMap.get(category.getId());
			List<PlanningCategoryItem> dbCategoryItems = categoryItemMap.get(category.getId());
			List<PlanningCategoryItem> dbCategoryItemTemp = dbCategoryItems;
			if (dbCategory==null) {
				category.preInsert(idGen.nextIdStr());
				category.setCompanyCode(band.getCompanyCode());
				saveList.add(category);
				addSub=true;
			} else {
				category.preUpdate();
				updateList.add(category);
				// 品类不一样 重新生成坑位
				if(!StrUtil.equals(category.getManager(),dbCategory.getManager())){
					addSub=true;
					dbCategoryItemTemp=null;
				}
				// 负责人不一样 重新删除坑位
				else if(!StrUtil.equals(category.getCategoryIds(),dbCategory.getCategoryIds())){
					addSub=true;
					dbCategoryItemTemp=null;
				}
				// 数量不一样 重新生成坑位
				else if(!NumberUtil.equals(dbCategory.getPlanRequirementNum(),category.getPlanRequirementNum())){
					addSub=true;
				}
				// 数量和数据库数量不一样 重新生成坑位
				else if(!NumberUtil.equals(
						Optional.ofNullable(dbCategoryItems).map(List::size).orElse(0),
						Optional.ofNullable(dbCategory.getPlanRequirementNum()).map(BigDecimal::intValue).orElse(0)
				)){
					addSub=true;
				}
			}
			if(addSub){
				planningCategoryItemService.saveCategoryItem(band,category,dbCategoryItemTemp);
			}

		}
		if(CollUtil.isNotEmpty(updateList)){
			batchUpdate(updateList);
		}
		if(CollUtil.isNotEmpty(saveList)){
			batchInsert(saveList);
		}

		return true;
	}

	@Transactional(readOnly = false)
	public boolean delPlanningCategory(String userCompany, List<String> idList) {
		//删除品类信息
		deleteByConditionDelFlag(new QueryCondition(userCompany).andIn("id",idList));
		//删除坑位信息
		planningCategoryItemService.delByPlanningCategory(userCompany,idList);
		return true;
	}
}
