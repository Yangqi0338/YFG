/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import com.base.sbc.pdm.mapper.PlanningMapper;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.pdm.entity.Planning;
import com.base.sbc.pdm.dao.PlanningDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述： service类
 * @address com.base.sbc.pdm.service.PlanningService
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-17 18:09:02
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class PlanningService extends BaseService<Planning> {

	@Autowired
	private PlanningDao planningDao;

	@Resource
	private PlanningMapper planningMapper;

	@Override
	protected BaseDao<Planning> getEntityDao() {
		return planningDao;
	}

	public Integer delByIds(String[] ids) {
		return planningMapper.delByIds(ids);
	}

	public Integer update(Planning planning) {
		return planningMapper.update(planning);
	}

	public List<Planning> listQuery(Planning planning) {
		return planningMapper.listQuery(planning);
	}
}
