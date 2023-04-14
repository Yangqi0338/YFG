/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.module.planning.entity.PlanningCategory;
import com.base.sbc.module.planning.dao.PlanningCategoryMapper;

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
public interface PlanningCategoryService extends IServicePlus<PlanningCategory> {


	/**
	 * 保存 波段企划-品类信息
	 * @param band
	 * @param categoryList
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean savePlanningCategory(PlanningBand band,List<PlanningCategory> categoryList);

	/**
	 * 删除品类
	 * @param userCompany
	 * @param idList
	 * @return
	 */
	public boolean delPlanningCategory(String userCompany, List<String> idList);

	/**
	 * 删除
	 * @param userCompany
	 * @param id
	 * @return
	 */
	public boolean delByPlanningBand(String userCompany,String id) ;
}
