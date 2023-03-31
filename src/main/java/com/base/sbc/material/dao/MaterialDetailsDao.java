/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.material.dao;

import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.material.entity.MaterialDetails;
/**
 * 类描述：素材库详情表 dao类
 * @address com.base.sbc.pdm.dao.mcaterial.MaterialDetailsDao
 * @author lile
 * @email  lilemyemail@163.com
 * @date 创建时间：2023-3-24 18:27:18
 * @version 1.0
 */
 @MyBatisDao
public class MaterialDetailsDao extends BaseDao<MaterialDetails>{

   @Override
	protected String getMapperNamespace() {
		return "MaterialDetailsDao";
	}

}
