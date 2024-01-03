/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessTemplateDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumResearchProcessTemplateMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：款式研发进度模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-2 17:47:24
 * @version 1.0  
 */
@Service
public class BasicsdatumResearchProcessTemplateServiceImpl extends BaseServiceImpl<BasicsdatumResearchProcessTemplateMapper, BasicsdatumResearchProcessTemplate> implements BasicsdatumResearchProcessTemplateService {
    @Transactional
    @Override
    public BasicsdatumResearchProcessTemplate saveTemplate(BasicsdatumResearchProcessTemplateDto templateDto) {
        BasicsdatumResearchProcessTemplate basicsdatumResearchProcessTemplate = new BasicsdatumResearchProcessTemplate();
        BeanUtils.copyProperties(templateDto, basicsdatumResearchProcessTemplate);
        this.save(basicsdatumResearchProcessTemplate);
        return basicsdatumResearchProcessTemplate;
    }

    @Override
    public BasicsdatumResearchProcessTemplateDto getTemplateById(String id) {
        return null;
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
