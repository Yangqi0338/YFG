/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.dao;

import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;
import ${javapackage}.${project}.entity.${className};
/** 
 * 类描述：${title} dao类
 * @address ${javapackage}.${project}.dao.${className}Dao
 * @author ${author}  
 * @email  ${email}
 * @date 创建时间：${.now} 
 * @version 1.0  
 */
 @MyBatisDao
public class ${className}Dao extends BaseDao<${className}>{

   @Override
	protected String getMapperNamespace() {
		return "${className}Dao";
	}

}
