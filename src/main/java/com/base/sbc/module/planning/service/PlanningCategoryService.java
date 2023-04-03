/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.planning.entity.PlanningBand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.dao.PlanningCategoryDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
		List<PlanningCategory> updateList=new ArrayList<>(12);
		List<PlanningCategory> saveList=new ArrayList<>(12);
		for (PlanningCategory category : categoryList) {
			category.setPlanningBandId(band.getId());
			category.setPlanningSeasonId(band.getPlanningSeasonId());
			if (StrUtil.contains(category.getId(), "-")||StrUtil.isBlank(category.getCreateId())) {
				category.preInsert(idGen.nextIdStr());
				category.setCompanyCode(band.getCompanyCode());
				saveList.add(category);
			} else {
				category.preUpdate();
				updateList.add(category);
			}
		}
		if(CollUtil.isNotEmpty(updateList)){
			batchUpdate(updateList);
		}
		if(CollUtil.isNotEmpty(saveList)){
			batchInsert(saveList);
		}
		planningCategoryItemService.saveCategoryItem(categoryList,band.getCompanyCode());
		return true;
	}
	
}
