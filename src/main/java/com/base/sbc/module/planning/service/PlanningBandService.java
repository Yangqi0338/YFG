/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.dao.PlanningBandDao;

/** 
 * 类描述：企划-波段表 service类
 * @address com.base.sbc.module.planning.service.PlanningBandService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:11
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class PlanningBandService extends BaseService<PlanningBand> {
	
	@Autowired
	private PlanningBandDao planningBandDao;

	@Autowired
	private PlanningCategoryService planningCategoryService;
	
	@Override
	protected BaseDao<PlanningBand> getEntityDao() {
		return planningBandDao;
	}
	@Transactional(readOnly = false)
	public  boolean del(String companyCode,String id){
		PlanningBand t=new PlanningBand();
		t.setDelFlag("1");
		t.setRemarks("del test");
		QueryCondition queryCondition = new QueryCondition(companyCode);
		queryCondition.andEqualTo("id",id).setT(t);
		int flg=updateByCondition("update",queryCondition);
		// 删除 品类信息,坑位信息,关联素材库
		planningCategoryService.delByPlanningBand(companyCode, id);

		return flg>0;
	}
	
}
