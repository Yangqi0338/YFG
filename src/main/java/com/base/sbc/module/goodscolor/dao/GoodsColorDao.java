/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.goodscolor.dao;

import com.base.sbc.module.goodscolor.entity.GoodsColor;
import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;

/**
 * 类描述：物料颜色 dao类
 * @address com.base.sbc.baseData.dao.GoodsColorDao
 * @author gcc  
 * @email  gcc@bestgcc.cn
 * @date 创建时间：2021-4-25 9:05:01 
 * @version 1.0  
 */
 @MyBatisDao
public class GoodsColorDao extends BaseDao<GoodsColor> {

   @Override
	protected String getMapperNamespace() {
		return "GoodsColorDao";
	}

}
