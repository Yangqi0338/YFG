/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBomTemplateDto;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplate;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBomTemplateVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 类描述：基础资料-BOM模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-22 17:27:44
 * @version 1.0  
 */
@Service
public class BasicsdatumBomTemplateServiceImpl extends BaseServiceImpl<BasicsdatumBomTemplateMapper, BasicsdatumBomTemplate> implements BasicsdatumBomTemplateService {


    @Autowired
    private BasicsdatumBomTemplateMaterialMapper basicsdatumBomTemplateMaterialMapper;


// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询Bom列表
     *
     * @param queryBomTemplateDto
     * @return
     */
    @Override
    public PageInfo getBomTemplateList(QueryBomTemplateDto queryBomTemplateDto) {

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.like(StringUtils.isNotBlank(queryBomTemplateDto.getCode()),"code",queryBomTemplateDto.getCode());
        queryWrapper.like(StringUtils.isNotBlank(queryBomTemplateDto.getName()),"name",queryBomTemplateDto.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryBomTemplateDto.getBrand()),"brand",queryBomTemplateDto.getBrand());
        queryWrapper.like(StringUtils.isNotBlank(queryBomTemplateDto.getBrandName()),"brand_name",queryBomTemplateDto.getBrandName());
        queryWrapper.like(StringUtils.isNotBlank(queryBomTemplateDto.getBomStatus()),"bom_status",queryBomTemplateDto.getBomStatus());
        /*查询基础资料-号型类型数据*/
        Page<BasicsdatumBomTemplateVo> objects = PageHelper.startPage(queryBomTemplateDto);
        baseMapper.selectList(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * bom模板新增修改
     *
     * @param addRevampBomTemplateDto
     * @return
     */
    @Override
    public Boolean addRevampBomTemplate(AddRevampBomTemplateDto addRevampBomTemplateDto) {
        BasicsdatumBomTemplate basicsdatumBomTemplate = new BasicsdatumBomTemplate();
        /*判断新增修改*/
        if (StringUtils.isNotBlank(addRevampBomTemplateDto.getId())) {
            /*修改*/
            basicsdatumBomTemplate = baseMapper.selectById(addRevampBomTemplateDto.getId());
            if(StringUtils.isBlank(addRevampBomTemplateDto.getApparelLabels()) ){
                addRevampBomTemplateDto.setApparelLabels(addRevampBomTemplateDto.getName());
            }
            BeanUtils.copyProperties(addRevampBomTemplateDto, basicsdatumBomTemplate);
            baseMapper.updateById(basicsdatumBomTemplate);
        } else {
//            校验编码重复
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("code", addRevampBomTemplateDto.getCode());
            if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            if(StringUtils.isBlank(addRevampBomTemplateDto.getApparelLabels()) ){
                addRevampBomTemplateDto.setApparelLabels(addRevampBomTemplateDto.getName());
            }
            BeanUtils.copyProperties(addRevampBomTemplateDto, basicsdatumBomTemplate);
            baseMapper.insert(basicsdatumBomTemplate);
        }
        return true;
    }

    /**
     * 删除BOM模板
     * 先删除关系表中是否有数据
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean delBomTemplate(String id) {
        List<String> stringList = StringUtils.convertList(id) ;
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.in("bom_template_id",stringList);
        basicsdatumBomTemplateMaterialMapper.delete(queryWrapper);
        baseMapper.deleteBatchIds(stringList);
        return true;
    }

// 自定义方法区 不替换的区域【other_end】
	
}
