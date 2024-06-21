/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBomTemplateMaterialDto;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.basicsdatum.dto.RevampSortDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBomTemplateMaterialVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

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


    @Autowired
    private BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;
    @Autowired
    private MinioUtils minioUtils;

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
        queryWrapper.eq(StringUtils.isNotBlank(queryBomTemplateDto.getSupplierFactoryIngredient()), "supplier_factory_ingredient", queryBomTemplateDto.getSupplierFactoryIngredient());
        queryWrapper.eq(StringUtils.isNotBlank(queryBomTemplateDto.getMaterialCodeName()), "material_code_name", queryBomTemplateDto.getMaterialCodeName());
        queryWrapper.eq(StringUtils.isNotBlank(queryBomTemplateDto.getIngredient()), "ingredient", queryBomTemplateDto.getIngredient());
        queryWrapper.eq("bom_template_id", queryBomTemplateDto.getBomTemplateId());
        queryWrapper.orderByAsc("sort");
        /*查询基础资料-物料数据*/
        Page<BasicsdatumBomTemplateMaterialVo> objects = PageHelper.startPage(queryBomTemplateDto);
        List list = baseMapper.selectList(queryWrapper);
        minioUtils.setObjectUrlToList(list, "imageUrl");
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
        if (CollectionUtils.isEmpty(list)) {
            throw new OtherException("数据为空");
        }
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = BeanUtil.copyToList(list, BasicsdatumBomTemplateMaterial.class);
        /*获取模板下最大顺序*/
        Integer sort = baseMapper.getMaxSort(list.get(0).getBomTemplateId());
        for (int i = 0; i < templateMaterialList.size(); i++) {
            templateMaterialList.get(i).setSort(sort==null?0:sort + i + 1);
        }
        CommonUtils.removeQueryList(templateMaterialList,"imageUrl");
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
        if (!ObjectUtils.isEmpty(addRevampBomTemplateMaterialDto.getUnitUse()) && !ObjectUtils.isEmpty(addRevampBomTemplateMaterialDto.getPrice())) {
            addRevampBomTemplateMaterialDto.setCost(addRevampBomTemplateMaterialDto.getUnitUse().multiply(addRevampBomTemplateMaterialDto.getPrice()));
        }
        BeanUtils.copyProperties(addRevampBomTemplateMaterialDto, bomTemplateMaterial);
        CommonUtils.removeQuery(bomTemplateMaterial,"imageUrl");
        baseMapper.updateById(bomTemplateMaterial);
        return true;
    }

    /**
     * 批量启用/停用-BOM模板物料
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean startStopBomTemplateMateria(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumBomTemplateMaterial> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        baseMapper.update(null, updateWrapper) ;
        return true;
    }

    /**
     * 复制BOM模板物料
     *
     * @param id
     * @return
     */
    @Override
    public Boolean copyBomTemplateMateria(String id) {
        if(StringUtils.isBlank(id)){
            throw new OtherException("bom模板物料id不能为空");
        }
        BasicsdatumBomTemplateMaterial basicsdatumBomTemplateMaterial =  baseMapper.selectById(id);
        /*获取模板下最大顺序*/
        Integer sort = baseMapper.getMaxSort(basicsdatumBomTemplateMaterial.getBomTemplateId());
        basicsdatumBomTemplateMaterial.setId(null);
        basicsdatumBomTemplateMaterial.setSort(sort+1);
        basicsdatumBomTemplateMaterial.insertInit();
        basicsdatumBomTemplateMaterial.updateInit();
        baseMapper.insert(basicsdatumBomTemplateMaterial);
        return true;
    }

    /**
     * 修改顺序
     *
     * @param revampSortDto
     * @return
     */
    @Override
    public Boolean revampSort(RevampSortDto revampSortDto) {

        BasicsdatumBomTemplateMaterial basicsdatumBomTemplateMaterial=new BasicsdatumBomTemplateMaterial();
        Integer  currentId =    baseMapper.selectById( revampSortDto.getCurrentId()).getSort();
        Integer targetId =  baseMapper.selectById( revampSortDto.getTargetId()) .getSort();
        basicsdatumBomTemplateMaterial.setId(revampSortDto.getCurrentId());
        basicsdatumBomTemplateMaterial.setSort(targetId);
        basicsdatumBomTemplateMaterial.updateInit();
        baseMapper.updateById(basicsdatumBomTemplateMaterial);
        BasicsdatumBomTemplateMaterial basicsdatumBomTemplateMaterial1=new BasicsdatumBomTemplateMaterial();
        basicsdatumBomTemplateMaterial1.setId(revampSortDto.getTargetId());
        basicsdatumBomTemplateMaterial1.setSort(currentId);
        basicsdatumBomTemplateMaterial1.updateInit();
        baseMapper.updateById(basicsdatumBomTemplateMaterial1);
        return true;
    }

    /**
     * 查询模板下的物料
     *
     * @param bomTemplateId
     * @return
     */
    @Override
    public List<BasicsdatumBomTemplateMaterialVo> getTemplateMateria(String bomTemplateId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bom_template_id", bomTemplateId);
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = baseMapper.selectList(queryWrapper);
        List<BasicsdatumBomTemplateMaterialVo> list = BeanUtil.copyToList(templateMaterialList, BasicsdatumBomTemplateMaterialVo.class);
        return list;
    }

    /**
     * 批量新增修改物料
     *
     * @param list
     * @return
     */
    @Override
    public Boolean batchSaveBomTemplate(List<AddRevampBomTemplateMaterialDto> list) {
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = BeanUtil.copyToList(list, BasicsdatumBomTemplateMaterial.class);
        templateMaterialList.forEach(it-> {
            CommonUtils.removeQuery(it, "imageUrl");
            if (!ObjectUtils.isEmpty(it.getUnitUse()) && !ObjectUtils.isEmpty(it.getPrice())) {
                it.setCost(it.getUnitUse().multiply(it.getPrice()));
            }
        });
        saveOrUpdateBatch(templateMaterialList);
        return true;
    }


// 自定义方法区 不替换的区域【other_end】

}
