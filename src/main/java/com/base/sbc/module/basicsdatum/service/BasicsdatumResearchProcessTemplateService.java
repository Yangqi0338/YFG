/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessTemplateDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumResearchProcessTemplateVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：款式研发进度模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-2 17:47:24
 * @version 1.0  
 */
public interface BasicsdatumResearchProcessTemplateService extends BaseService<BasicsdatumResearchProcessTemplate>{

// 自定义方法区 不替换的区域【other_start】


    PageInfo getTemplatePageInfo(BasicsdatumResearchProcessTemplateDto templateDto);

    /**
     * 保存研发总进度模板
     * @param templateDto
     */
    BasicsdatumResearchProcessTemplate saveTemplate(BasicsdatumResearchProcessTemplateDto templateDto);

    /**
     * 查看研发总进度模板详情
     * @param id
     * @return
     */
    BasicsdatumResearchProcessTemplateVo getTemplateById(String id);

    /**
     * 模板启用/停用
     * @param templateDto
     */
    Boolean updateEnableFlagById(BasicsdatumResearchProcessTemplateDto templateDto);
// 自定义方法区 不替换的区域【other_end】

	
}
