/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessNodeDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessNode;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessNodeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBomTemplateMaterialVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumResearchProcessTemplateMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private BasicsdatumResearchProcessNodeService basicsdatumResearchProcessNodeService;

    @Transactional
    @Override
    public BasicsdatumResearchProcessTemplate saveTemplate(BasicsdatumResearchProcessTemplateDto templateDto) {
        BasicsdatumResearchProcessTemplate basicsdatumResearchProcessTemplate = new BasicsdatumResearchProcessTemplate();
        BeanUtils.copyProperties(templateDto, basicsdatumResearchProcessTemplate);
        this.save(basicsdatumResearchProcessTemplate);

        List<BasicsdatumResearchProcessNodeDto> nodeDtoList = templateDto.getNodeDtoList();
        List<BasicsdatumResearchProcessNode> basicsdatumResearchProcessNodes = BeanUtil.copyToList(nodeDtoList, BasicsdatumResearchProcessNode.class);
        basicsdatumResearchProcessNodeService.saveBatch(basicsdatumResearchProcessNodes);
        return basicsdatumResearchProcessTemplate;
    }

    @Override
    public BasicsdatumResearchProcessTemplateDto getTemplateById(String id) {
        return null;
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
