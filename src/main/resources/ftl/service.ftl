/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.service;
import com.base.sbc.module.common.service.BaseService;
import ${javapackage}.${project}.entity.${className};
import ${javapackage}.${project}.dto.${className}QueryDto;
import ${javapackage}.${project}.vo.${className}Vo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：${title} service类
 * @address ${javapackage}.${project}.service.${className}Service
 * @author ${author}
 * @email ${email}
 * @date 创建时间：${.now}
 * @version 1.0  
 */
public interface ${className}Service extends BaseService<${className}>{

// 自定义方法区 不替换的区域【other_start】

    PageInfo<${className}Vo> findPage(${className}QueryDto dto);

// 自定义方法区 不替换的区域【other_end】

	
}