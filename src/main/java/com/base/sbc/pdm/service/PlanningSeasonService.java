/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.pdm.entity.PlanningSeason;
import com.base.sbc.pdm.dao.PlanningSeasonDao;

/** 
 * 类描述：企划-产品季 service类
 * @address com.base.sbc.pdm.service.PlanningSeasonService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class PlanningSeasonService extends BaseService<PlanningSeason> {
	
	@Autowired
	private PlanningSeasonDao planningSeasonDao;
	
	@Override
	protected BaseDao<PlanningSeason> getEntityDao() {
		return planningSeasonDao;
	}
	
}
