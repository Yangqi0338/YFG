/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import ${javapackage}.${project}.entity.${className};
import ${javapackage}.${project}.dao.${className}Dao;

/** 
 * 类描述：${title} service类
 * @address ${javapackage}.${project}.service.${className}Service
 * @author ${author}
 * @email ${email}
 * @date 创建时间：${.now}
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class ${className}Service extends BaseService<${className}> {
	
	@Autowired
	private ${className}Dao ${smallClassName}Dao;
	
	@Override
	protected BaseDao<${className}> getEntityDao() {
		return ${smallClassName}Dao;
	}
	
}
