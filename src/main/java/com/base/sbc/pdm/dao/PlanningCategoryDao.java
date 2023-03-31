/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.dao;

import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.pdm.entity.PlanningCategory;
/** 
 * 类描述：企划-品类信息 dao类
 * @address com.base.sbc.pdm.dao.PlanningCategoryDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-3-29 10:36:59 
 * @version 1.0  
 */
 @MyBatisDao
public class PlanningCategoryDao extends BaseDao<PlanningCategory>{

   @Override
	protected String getMapperNamespace() {
		return "PlanningCategoryDao";
	}

}
