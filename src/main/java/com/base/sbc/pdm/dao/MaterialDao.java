/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.dao;

import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.pdm.entity.Material;
/** 
 * 类描述：素材库 dao类
 * @address com.base.sbc.pdm.dao.MaterialDao
 * @author lile  
 * @email  lilemyemail@163.com
 * @date 创建时间：2023-3-23 11:24:30 
 * @version 1.0  
 */
 @MyBatisDao
public class MaterialDao extends BaseDao<Material>{

   @Override
	protected String getMapperNamespace() {
		return "MaterialDao";
	}

}
