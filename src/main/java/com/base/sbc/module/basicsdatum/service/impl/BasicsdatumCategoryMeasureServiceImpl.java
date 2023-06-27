/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumRangeDifferenceMapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumCategoryMeasureMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCategoryMeasure;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCategoryMeasureVo;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import com.base.sbc.config.common.base.BaseController;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import javax.servlet.http.HttpServletResponse;

import com.base.sbc.config.utils.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.utils.ExcelUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-品类测量组 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:55
 */
@Service
public class BasicsdatumCategoryMeasureServiceImpl extends BaseServiceImpl<BasicsdatumCategoryMeasureMapper, BasicsdatumCategoryMeasure> implements BasicsdatumCategoryMeasureService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumRangeDifferenceMapper basicsdatumRangeDifferenceMapper;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-品类测量组分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumCategoryMeasureVo> getBasicsdatumCategoryMeasureList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询基础资料-品类测量组数据*/
        List<BasicsdatumCategoryMeasure> basicsdatumCategoryMeasureList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumCategoryMeasure> pageInfo = new PageInfo<>(basicsdatumCategoryMeasureList);
        /*转换vo*/
        List<BasicsdatumCategoryMeasureVo> list = BeanUtil.copyToList(basicsdatumCategoryMeasureList, BasicsdatumCategoryMeasureVo.class);
        PageInfo<BasicsdatumCategoryMeasureVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 基础资料-品类测量组导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumCategoryMeasureImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumCategoryMeasureExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumCategoryMeasureExcelDto.class, params);
        QueryWrapper<BasicsdatumRangeDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        List<BasicsdatumRangeDifference> basicsdatumRangeDifferenceList = basicsdatumRangeDifferenceMapper.selectList(queryWrapper);
        for (BasicsdatumCategoryMeasureExcelDto basicsdatumCategoryMeasureExcelDto : list) {
            if (StringUtils.isNotBlank(basicsdatumCategoryMeasureExcelDto.getRangeDifferenceName())) {
                List<BasicsdatumRangeDifference> differenceList = basicsdatumRangeDifferenceList.stream().filter(r -> r.getRangeDifference().equals(basicsdatumCategoryMeasureExcelDto.getRangeDifferenceName())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(differenceList)) {
                    basicsdatumCategoryMeasureExcelDto.setRangeDifferenceId(differenceList.get(0).getId());
                }
            }
        }

        List<BasicsdatumCategoryMeasure> basicsdatumCategoryMeasureList = BeanUtil.copyToList(list, BasicsdatumCategoryMeasure.class);
        saveOrUpdateBatch(basicsdatumCategoryMeasureList);
        return true;
    }

    /**
     * 基础资料-品类测量组导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumCategoryMeasureDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumCategoryMeasureExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumCategoryMeasureExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumCategoryMeasureExcelDto.class, "基础资料-品类测量组.xlsx", new ExportParams(), response);
    }


    /**
     * 方法描述：新增修改基础资料-品类测量组
     *
     * @param addRevampBasicsdatumCategoryMeasureDto 基础资料-品类测量组Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumCategoryMeasure(AddRevampBasicsdatumCategoryMeasureDto addRevampBasicsdatumCategoryMeasureDto) {
        BasicsdatumCategoryMeasure basicsdatumCategoryMeasure = new BasicsdatumCategoryMeasure();
        if (StringUtils.isEmpty(addRevampBasicsdatumCategoryMeasureDto.getId())) {
            QueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumCategoryMeasureDto, basicsdatumCategoryMeasure);
            basicsdatumCategoryMeasure.setCompanyCode(baseController.getUserCompany());
            basicsdatumCategoryMeasure.insertInit();
            baseMapper.insert(basicsdatumCategoryMeasure);
        } else {
            /*修改*/
            basicsdatumCategoryMeasure = baseMapper.selectById(addRevampBasicsdatumCategoryMeasureDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumCategoryMeasure)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumCategoryMeasureDto, basicsdatumCategoryMeasure);
            basicsdatumCategoryMeasure.updateInit();
            baseMapper.updateById(basicsdatumCategoryMeasure);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-品类测量组
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumCategoryMeasure(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopBasicsdatumCategoryMeasure(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumCategoryMeasure> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}

