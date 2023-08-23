/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBomTemplateMaterialDto;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-BOM模板与物料档案中间表 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService
 * @email XX.com
 * @date 创建时间：2023-8-22 17:27:44
 */
@Service
public class BasicsdatumBomTemplateMaterialServiceImpl extends BaseServiceImpl<BasicsdatumBomTemplateMaterialMapper, BasicsdatumBomTemplateMaterial> implements BasicsdatumBomTemplateMaterialService {


// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询分页查询模板物料信息
     *
     * @param queryBomTemplateDto
     * @return
     */
    @Override
    public PageInfo getBomTemplateMateriaList(QueryBomTemplateDto queryBomTemplateDto) {
        if (StringUtils.isBlank(queryBomTemplateDto.getBomTemplateId())) {
            throw new OtherException("缺少bom模板id");
        }
        /*查询*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bom_template_id", queryBomTemplateDto.getBomTemplateId());

        /*查询基础资料-档差数据*/
        Page<BasicsdatumMaterialPageVo> objects = PageHelper.startPage(queryBomTemplateDto);
        baseMapper.selectList(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * 添加一行物料
     *
     * @param bomTemplateId
     * @return
     */
    @Override
    public Boolean addMateria(String bomTemplateId) {
        BasicsdatumBomTemplateMaterial basicsdatumBomTemplateMaterial = new BasicsdatumBomTemplateMaterial();
        basicsdatumBomTemplateMaterial.setBomTemplateId(bomTemplateId);
        baseMapper.insert(basicsdatumBomTemplateMaterial);
        return true;
    }

    /**
     * 选择物料
     *
     * @param list
     * @return
     */
    @Override
    public Boolean selectMateria(List<AddRevampBomTemplateMaterialDto> list) {
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = BeanUtil.copyToList(list, BasicsdatumBomTemplateMaterial.class);
        saveOrUpdateBatch(templateMaterialList);
        return true;
    }

    /**
     * 查询bom模板下的物料id
     *
     * @param bomTemplateId bom模板id
     * @return
     */
    @Override
    public List<String> getTemplateMateriaId(String bomTemplateId) {
        return baseMapper.getTemplateMateriaId(bomTemplateId);
    }

    /**
     * 删除-BOM模板物料
     *
     * @param id bom模板物料id
     * @return
     */
    @Override
    public Boolean delBomTemplateMateria(String id) {
        baseMapper.deleteBatchIds(StringUtils.convertList(id));
        return true;
    }

    /**
     * 修改BOM模板物料
     *
     * @param addRevampBomTemplateMaterialDto
     * @return
     */
    @Override
    public Boolean revampBomTemplateMaterial(AddRevampBomTemplateMaterialDto addRevampBomTemplateMaterialDto) {
        if (StringUtils.isBlank(addRevampBomTemplateMaterialDto.getId())) {
            throw new OtherException("bom模板物料id不能为空");
        }
        BasicsdatumBomTemplateMaterial bomTemplateMaterial = baseMapper.selectById(addRevampBomTemplateMaterialDto.getId());
        if(!ObjectUtils.isEmpty(addRevampBomTemplateMaterialDto.getUnitUse()) && !ObjectUtils.isEmpty(addRevampBomTemplateMaterialDto.getPrice()) ){
            addRevampBomTemplateMaterialDto.setCost(addRevampBomTemplateMaterialDto.getUnitUse().multiply(addRevampBomTemplateMaterialDto.getPrice()));
        }
        BeanUtils.copyProperties(addRevampBomTemplateMaterialDto, bomTemplateMaterial);
        baseMapper.updateById(bomTemplateMaterial);
        return true;
    }


// 自定义方法区 不替换的区域【other_end】

}
