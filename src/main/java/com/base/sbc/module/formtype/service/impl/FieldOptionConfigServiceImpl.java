/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.FieldOptionConfigDto;
import com.base.sbc.module.formtype.dto.FieldOptionConfigExcelDto;
import com.base.sbc.module.formtype.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.mapper.FieldOptionConfigMapper;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.formtype.vo.FieldOptionConfigVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：字段选项配置表 service类
 * @address com.base.sbc.module.formType.service.FieldOptionConfigService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-4 13:00:48
 * @version 1.0  
 */
@Service
public class FieldOptionConfigServiceImpl extends BaseServiceImpl<FieldOptionConfigMapper, FieldOptionConfig> implements FieldOptionConfigService {


    @Override
    public Boolean addFieldOptionConfig(List<FieldOptionConfigDto> optionConfigDtoList) {
        List<FieldOptionConfig> list = BeanUtil.copyToList(optionConfigDtoList, FieldOptionConfig.class);
        saveOrUpdateBatch(list);
        return true;
    }

    /**
     * 查询配置选项
     *
     * @param queryFieldOptionConfigDto
     * @return
     */
    @Override
    public PageInfo getFieldOptionConfigList(QueryFieldOptionConfigDto queryFieldOptionConfigDto) {
        BaseQueryWrapper<FieldOptionConfig> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("field_management_id",queryFieldOptionConfigDto.getFieldManagementId());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getProdCategory2nd()),"prod_category2nd",queryFieldOptionConfigDto.getProdCategory2nd());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getCategoryCode()),"category_code",queryFieldOptionConfigDto.getCategoryCode());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getSeason()),"season",queryFieldOptionConfigDto.getSeason());
        queryWrapper.notEmptyLike("brand",queryFieldOptionConfigDto.getBrand());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getStatus()),"status",queryFieldOptionConfigDto.getStatus());
        Page<FieldOptionConfigVo> objects = PageHelper.startPage(queryFieldOptionConfigDto);
        baseMapper.selectList(queryWrapper);
        return objects.toPageInfo();
    }

    /**
     * 删除配置选项
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delFieldOptionConfig(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    /**
     * 批量启用/停用
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean startStopFieldOptionConfig(StartStopDto startStopDto) {
        UpdateWrapper<FieldOptionConfig> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 查询每个字段下的配置选项
     *
     * @param queryFieldOptionConfigDto
     * @return
     */
    @Override
    public Map<String, List<FieldOptionConfig>> getFieldConfig(QueryFieldOptionConfigDto queryFieldOptionConfigDto) {
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
        queryWrapper.in("field_management_id", queryFieldOptionConfigDto.getFieldManagementIdList());

        queryWrapper.eq("season", queryFieldOptionConfigDto.getSeason());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getProdCategory2nd()), "prod_category2nd", queryFieldOptionConfigDto.getProdCategory2nd());
        queryWrapper.eq(StringUtils.isNotBlank(queryFieldOptionConfigDto.getCategoryCode()), "category_code", queryFieldOptionConfigDto.getCategoryCode());

        queryWrapper.apply("find_in_set({0},brand)", queryFieldOptionConfigDto.getBrand());
        List<FieldOptionConfig> optionConfigList = baseMapper.selectList(queryWrapper);
        /*查询字段配置中的数据*/
        Map<String, List<FieldOptionConfig>> listMap = optionConfigList.stream().collect(Collectors.groupingBy(FieldOptionConfig::getFieldManagementId));
        return listMap;
    }

    /**
     * 配置选项导入
     *
     * @param file
     * @param fieldManagementId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importExcel(MultipartFile file, String fieldManagementId,String formTypeId) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        /*删除之前的数据*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("form_type_id", formTypeId);
        queryWrapper.eq("field_management_id", fieldManagementId);
        baseMapper.delete(queryWrapper);
        /*重新导入*/
        List<FieldOptionConfigExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), FieldOptionConfigExcelDto.class, params);
        list.forEach(f -> {
            f.setFieldManagementId(fieldManagementId);
            f.setFormTypeId(formTypeId);
        });
        List<FieldOptionConfig> optionConfigListl = BeanUtil.copyToList(list, FieldOptionConfig.class);
        saveOrUpdateBatch(optionConfigListl);
        return true;
    }

    /**
     * 导出配置列
     *
     * @param response
     * @param fieldManagementId
     */
    @Override
    public void deriveExcel(HttpServletResponse response, String fieldManagementId) throws IOException {
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("field_management_id",fieldManagementId);
        List<FieldOptionConfigExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), FieldOptionConfigExcelDto.class);
        ExcelUtils.exportExcel(list, FieldOptionConfigExcelDto.class, "字段配置选项.xlsx", new ExportParams(), response);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
