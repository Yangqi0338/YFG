/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;

import com.base.sbc.module.planning.entity.PlanningBand;

import java.util.List;

/** 
 * 类描述：企划-波段表 service类
 * @address com.base.sbc.module.planning.service.PlanningBandService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:11
 * @version 1.0  
 */
public interface PlanningBandService extends IServicePlus<PlanningBand> {


	/**
	 * 删除
	 * @param id
	 * @return
	 */
	boolean del(String id);

	/**
	 * 通过构造器查询
	 * @param qw
	 * @return
	 */
	public List<PlanningSeasonBandVo> selectByQw(QueryWrapper<PlanningBand> qw);
	
}
