/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：基础资料-BOM模板与物料档案中间表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-22 17:27:44
 * @version 1.0  
 */
public interface BasicsdatumBomTemplateMaterialService extends BaseService<BasicsdatumBomTemplateMaterial>{

// 自定义方法区 不替换的区域【other_start】
    /**
     * 查询分页查询模板物料信息
     * @param queryBomTemplateDto
     * @return
     */
    PageInfo getBomTemplateMateriaList(QueryBomTemplateDto queryBomTemplateDto);

// 自定义方法区 不替换的区域【other_end】

	
}
