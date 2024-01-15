/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessNodeDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumResearchProcessTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessNode;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumResearchProcessTemplateMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessNodeService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumResearchProcessTemplateService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumResearchProcessNodeVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumResearchProcessTemplateVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private BaseController baseController;

    @Override
    public PageInfo getTemplatePageInfo(BasicsdatumResearchProcessTemplateDto templateDto) {
        BaseQueryWrapper<BasicsdatumResearchProcessTemplate> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(templateDto.getBrandCode()),"brand_code", templateDto.getBrandCode());
        queryWrapper.eq(StringUtils.isNotBlank(templateDto.getProductType()),"product_type", templateDto.getProductType());
        queryWrapper.notEmptyLike("template_name", templateDto.getTemplateName());
        Page<BasicsdatumModelTypeVo> objects = PageHelper.startPage(templateDto);
        this.getBaseMapper().selectList(queryWrapper);
        return objects.toPageInfo();
    }

    @Transactional
    @Override
    public BasicsdatumResearchProcessTemplate saveTemplate(BasicsdatumResearchProcessTemplateDto templateDto) {
        if (templateDto != null) {
            QueryWrapper<BasicsdatumResearchProcessTemplate> validQueryWrapper = new QueryWrapper();
            validQueryWrapper.eq("brand_code", templateDto.getBrandCode());
            validQueryWrapper.eq("product_type", templateDto.getProductType());
            validQueryWrapper.eq("enable_flag", 0);
            List<BasicsdatumResearchProcessTemplate> validExsitList = this.list(validQueryWrapper);
            if (CollUtil.isNotEmpty(validExsitList)) {
                throw new OtherException("该品牌:{" + templateDto.getBrandName() + "}，以及生产类型:{" + templateDto.getProductType() + "} 已存在模板数据");
            }

            List<BasicsdatumResearchProcessNodeDto> nodeDtoList = templateDto.getNodeDtoList();

            List<String> repetitionCount = nodeDtoList.stream().map(BasicsdatumResearchProcessNodeDto::getCode).distinct().collect(Collectors.toList());
            if (repetitionCount.size() != nodeDtoList.size()) {
                throw new OtherException("存在重复节点，请检查数据！");
            }
        }
        
        BasicsdatumResearchProcessTemplate basicsdatumResearchProcessTemplate = new BasicsdatumResearchProcessTemplate();
        BeanUtils.copyProperties(templateDto, basicsdatumResearchProcessTemplate);
        this.save(basicsdatumResearchProcessTemplate);

        List<BasicsdatumResearchProcessNodeDto> nodeDtoList = templateDto.getNodeDtoList();
        List<BasicsdatumResearchProcessNode> basicsdatumResearchProcessNodes = BeanUtil.copyToList(nodeDtoList, BasicsdatumResearchProcessNode.class);
        for (int i = 0; i < basicsdatumResearchProcessNodes.size(); i++) {
            basicsdatumResearchProcessNodes.get(i).setSort(i+1);
            basicsdatumResearchProcessNodes.get(i).setTemplateId(basicsdatumResearchProcessTemplate.getId());
        }
        basicsdatumResearchProcessNodeService.saveOrUpdateBatch(basicsdatumResearchProcessNodes);
        return basicsdatumResearchProcessTemplate;
    }

    @Override
    public BasicsdatumResearchProcessTemplateVo getTemplateById(String id) {
        BasicsdatumResearchProcessTemplate template = this.getById(id);
        BasicsdatumResearchProcessTemplateVo templateVo = new BasicsdatumResearchProcessTemplateVo();
        BeanUtils.copyProperties(template, templateVo);
        if (template != null) {
            QueryWrapper<BasicsdatumResearchProcessNode> queryWrapper = new QueryWrapper();
            queryWrapper.eq("template_id", id);
            queryWrapper.orderByAsc("sort");
            List<BasicsdatumResearchProcessNode> templateList = basicsdatumResearchProcessNodeService.list(queryWrapper);
            List<BasicsdatumResearchProcessNodeVo> basicsdatumResearchProcessNodeVos = BeanUtil.copyToList(templateList, BasicsdatumResearchProcessNodeVo.class);
            templateVo.setTemplateNodeList(basicsdatumResearchProcessNodeVos);
        }
        return templateVo;
    }

    @Override
    public Boolean updateEnableFlagById(BasicsdatumResearchProcessTemplateDto templateDto) {
        if (templateDto.getEnableFlag() == 0) {
            QueryWrapper<BasicsdatumResearchProcessTemplate> validQueryWrapper = new QueryWrapper();
            validQueryWrapper.eq("brand_code", templateDto.getBrandCode());
            validQueryWrapper.eq("product_type", templateDto.getProductType());
            validQueryWrapper.eq("enable_flag", templateDto.getEnableFlag());
            List<BasicsdatumResearchProcessTemplate> validExsitList = this.list(validQueryWrapper);
            if (CollUtil.isNotEmpty(validExsitList)) {
                throw new OtherException("该品牌:{" + validExsitList.get(0).getBrandName() + "}，以及生产类型:{" + templateDto.getProductType() + "} 已存在启用数据！");
            }
        }

        BasicsdatumResearchProcessTemplate template = new BasicsdatumResearchProcessTemplate();
        BeanUtils.copyProperties(templateDto, template);
        this.updateById(template);
        return true;
    }
}
