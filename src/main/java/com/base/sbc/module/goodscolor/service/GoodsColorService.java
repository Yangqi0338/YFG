/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.goodscolor.service;


import com.base.sbc.module.goodscolor.dao.GoodsColorDao;
import com.base.sbc.module.goodscolor.entity.GoodsColor;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 类描述：物料颜色 service类
 * @address com.base.sbc.baseData.service.GoodsColorService
 * @author gcc
 * @email gcc@bestgcc.cn
 * @date 创建时间：2021-4-25 9:05:01
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class GoodsColorService extends BaseService<GoodsColor> {
	
	@Autowired
	private GoodsColorDao goodsColorDao;
	
	@Override
	protected BaseDao<GoodsColor> getEntityDao() {
		return goodsColorDao;
	}
	
}
