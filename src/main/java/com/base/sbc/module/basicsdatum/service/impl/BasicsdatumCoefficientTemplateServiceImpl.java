/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumCoefficientTemplateMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCoefficientTemplateExcelVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCoefficientTemplateVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：基础资料-纬度系数模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0  
 */
@Service
public class BasicsdatumCoefficientTemplateServiceImpl extends BaseServiceImpl<BasicsdatumCoefficientTemplateMapper, BasicsdatumCoefficientTemplate> implements BasicsdatumCoefficientTemplateService {


    @Autowired
    private BasicsdatumDimensionalityService basicsdatumDimensionalityService;



    /**
     * 获取模板列表
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo getCoefficientTemplateList(BasicsdatumCoefficientTemplateDto dto) {
        QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()),"name",dto.getName());
        queryWrapper.in(StringUtils.isNotBlank(dto.getChannel()),"channel",StringUtils.convertList(dto.getChannel()));
        queryWrapper.in(StringUtils.isNotBlank(dto.getSeason()),"season",StringUtils.convertList(dto.getSeason()));
        queryWrapper.eq(StringUtils.isNotBlank(dto.getStatus()),"status",dto.getStatus());
        /*查询*/
        Page<BasicsdatumCoefficientTemplateVo> objects = PageHelper.startPage(dto);
        baseMapper.selectList(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * 新增保存
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public BasicsdatumCoefficientTemplate addUpdateCoefficientTemplate(AddUpdateCoefficientTemplateDto dto) {
        BasicsdatumCoefficientTemplate basicsdatumCoefficientTemplate = new BasicsdatumCoefficientTemplate();
        /*判断新增修改*/
        if (StringUtils.isNotBlank(dto.getId())) {
            /*修改*/
            basicsdatumCoefficientTemplate = baseMapper.selectById(dto.getId());
            if (!StrUtil.equals(dto.getName(), basicsdatumCoefficientTemplate.getName())) {
                //            校验名称重复
                QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
                queryWrapper.eq("name", dto.getName());
                if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                    throw new OtherException(dto.getName()+"数据重复");
                }
            }
            BeanUtils.copyProperties(dto, basicsdatumCoefficientTemplate);
            baseMapper.updateById(basicsdatumCoefficientTemplate);
        } else {
//            校验名称重复
            QueryWrapper<BasicsdatumCoefficientTemplate> queryWrapper = new QueryWrapper();
            queryWrapper.eq("name", dto.getName());
            if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                throw new OtherException(dto.getName()+"数据重复");
            }
            BeanUtils.copyProperties(dto, basicsdatumCoefficientTemplate);
            baseMapper.insert(basicsdatumCoefficientTemplate);
        }
        if(CollUtil.isNotEmpty(dto.getList())){
            /*新增修改维度标签*/
            for (BasicsdatumDimensionalityDto basicsdatumDimensionalityDto : dto.getList()) {
                basicsdatumDimensionalityDto.setCoefficientTemplateId(basicsdatumCoefficientTemplate.getId());
            }
            basicsdatumDimensionalityService.batchSaveDimensionality(dto.getList());
        }
        return basicsdatumCoefficientTemplate;
    }

    /**
     * 停用启用
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean startStopCoefficientTemplate(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumCoefficientTemplate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 复制模板
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean copyTemplate(BasicsdatumCoefficientTemplateDto dto) {
        if (StrUtil.isBlank(dto.getId()) || StrUtil.isBlank(dto.getName())) {
            throw new OtherException("模板id或名称不能为空");
        }
        /*校验数据是否重复*/
        BasicsdatumCoefficientTemplate name = getByOne("name", dto.getName());
        if (ObjectUtil.isNotEmpty(name)) {
            throw new OtherException(dto.getName() + "数据重复");
        }
        BasicsdatumCoefficientTemplate coefficientTemplate = baseMapper.selectById(dto.getId());
        /*模板里面的系数*/
        List<BasicsdatumDimensionality> list = basicsdatumDimensionalityService.getByList("coefficient_template_id", coefficientTemplate.getId());
        BeanUtils.copyProperties(dto, coefficientTemplate);
        coefficientTemplate.setId(null);
        coefficientTemplate.insertInit();
        coefficientTemplate.updateInit();
        baseMapper.insert(coefficientTemplate);
        /*新增系数*/
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(l -> {
                l.setCoefficientTemplateId(coefficientTemplate.getId());
                l.setId(null);
            });
            basicsdatumDimensionalityService.saveBatch(list);

        }
        return true;
    }

    /**
     * 获取模板详情
     *
     * @param idDto
     * @return
     */
    @Override
    public BasicsdatumCoefficientTemplateVo getTemplateDetails(IdDto idDto) {
        BasicsdatumCoefficientTemplate basicsdatumCoefficientTemplate = baseMapper.selectById(idDto.getId());
        BasicsdatumCoefficientTemplateVo basicsdatumCoefficientTemplateVo = new BasicsdatumCoefficientTemplateVo();
        BeanUtils.copyProperties(basicsdatumCoefficientTemplate, basicsdatumCoefficientTemplateVo);
        return basicsdatumCoefficientTemplateVo;
    }

    /**
     * 导出
     *
     * @param dto
     * @param response
     */
    @Override
    public void deriveExcel(BasicsdatumCoefficientTemplateDto dto, HttpServletResponse response) throws IOException {
        List<BasicsdatumCoefficientTemplateVo> list = getCoefficientTemplateList(dto).getList();
        List<BasicsdatumCoefficientTemplateExcelVo> list1 = BeanUtil.copyToList(list, BasicsdatumCoefficientTemplateExcelVo.class);
        ExcelUtils.exportExcel(list1, BasicsdatumCoefficientTemplateExcelVo.class, "维度系数模板.xlsx", new ExportParams(), response);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
