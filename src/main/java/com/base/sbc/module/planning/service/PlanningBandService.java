/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.PlanningBandDto;
import com.base.sbc.module.planning.dto.PlanningBandSearchDto;
import com.base.sbc.module.planning.dto.ProductSeasonExpandByBandSearchDto;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;

import com.base.sbc.module.planning.entity.PlanningBand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：企划-波段表 service类
 * @address com.base.sbc.module.planning.service.PlanningBandService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:11
 * @version 1.0  
 */
public interface PlanningBandService extends BaseService<PlanningBand> {


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
	 List<PlanningSeasonBandVo> selectByQw(QueryWrapper<PlanningBand> qw);

	/**
	 * 保存波段企划
	 * @param dto
	 * @return
	 */
    PlanningBand savePlanningBand(PlanningBandDto dto);

	/**
	 * 校验名称、波段是否重复
	 * @param dto
	 * @param userCompany
	 */
    void checkRepeat(PlanningBandDto dto, String userCompany);

	/**
	 * 通过产品季和波段企划名称
	 * @param planningSeasonName 产品季名称
	 * @param planningBandName 波段企划名称
	 * @param userCompany 企业编码
	 * @return
	 */
	PlanningSeasonBandVo getBandByName(String planningSeasonName, String planningBandName, String userCompany);

	/**
	 * 波段企划分页查询
	 * @param dto
	 * @param userCompany
	 * @return
	 */
	PageInfo<PlanningSeasonBandVo> queryPlanningSeasonBandPageInfo(PlanningBandSearchDto dto, String userCompany);



	/**
	 * 按波段展开
	 * @param dto
	 * @return
	 */
    PageInfo expandByBand(ProductSeasonExpandByBandSearchDto dto,String companyCode);
}
