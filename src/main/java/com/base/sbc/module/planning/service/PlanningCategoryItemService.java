/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.module.planning.entity.PlanningCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.dao.PlanningCategoryItemDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** 
 * 类描述：企划-坑位信息 service类
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class PlanningCategoryItemService extends BaseService<PlanningCategoryItem> {

	@Autowired
	private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
	@Autowired
	private PlanningCategoryItemDao planningCategoryItemDao;
	
	@Override
	protected BaseDao<PlanningCategoryItem> getEntityDao() {
		return planningCategoryItemDao;
	}


	/**
	 * 保存坑位信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public int saveCategoryItem(List<PlanningCategory> categoryList,String companyCode){
		if(CollUtil.isEmpty(categoryList)){
			return 0;
		}
		//删除之前的数据 通过 planning_band_id
		List<String> planningBandIds = categoryList.stream().map(PlanningCategory::getPlanningBandId).collect(Collectors.toList());
		QueryCondition delQc=new QueryCondition(companyCode);
		delQc.andIn("planning_band_id",planningBandIds);
		planningCategoryItemMaterialService.deleteByCondition(delQc);
//		planningCategoryItemMaterialService.batchdeleteByIdDelFlag()
		deleteByCondition(delQc);
		List<PlanningCategoryItem> itemList=new ArrayList<>(12);
		IdGen idGen=new IdGen();
		//通过企划需求数生成 
		for (PlanningCategory planningCategory : categoryList) {
			BigDecimal planRequirementNum = planningCategory.getPlanRequirementNum();
			if(planRequirementNum==null){
				continue;
			}
			for (int i = 0; i < planRequirementNum.intValue(); i++) {
				PlanningCategoryItem item=new PlanningCategoryItem();
				item.setCompanyCode(companyCode);
				item.setId(idGen.nextIdStr());
				item.setPlanningSeasonId(planningCategory.getPlanningSeasonId());
				item.setPlanningBandId(planningCategory.getPlanningBandId());
				item.setPlanningCategoryId(planningCategory.getId());
				//TODO 设计款号
				itemList.add(item);
			}
		}
		return batchInsert(itemList);
	}
	
}
