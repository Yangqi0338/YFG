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
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumPressingPackagingMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumPressingPackaging;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumPressingPackagingVo;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumPressingPackaging;
import com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import com.base.sbc.config.common.base.BaseController;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：基础资料-整烫包装 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackagingService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:55
 */
@Service
public class BasicsdatumPressingPackagingServiceImpl extends ServicePlusImpl<BasicsdatumPressingPackagingMapper, BasicsdatumPressingPackaging> implements BasicsdatumPressingPackagingService {

    @Autowired
    private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-整烫包装分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumPressingPackagingVo> getBasicsdatumPressingPackagingList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumPressingPackaging> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询基础资料-整烫包装数据*/
        List<BasicsdatumPressingPackaging> basicsdatumPressingPackagingList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumPressingPackaging> pageInfo = new PageInfo<>(basicsdatumPressingPackagingList);
        /*转换vo*/
        List<BasicsdatumPressingPackagingVo> list = BeanUtil.copyToList(basicsdatumPressingPackagingList, BasicsdatumPressingPackagingVo.class);
        PageInfo<BasicsdatumPressingPackagingVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 基础资料-整烫包装导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumPressingPackagingImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumPressingPackagingExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumPressingPackagingExcelDto.class, params);
        List<BasicsdatumPressingPackaging> basicsdatumPressingPackagingList = BeanUtil.copyToList(list, BasicsdatumPressingPackaging.class);
        saveOrUpdateBatch(basicsdatumPressingPackagingList);
        return true;
    }

    /**
     * 基础资料-整烫包装导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumPressingPackagingDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumPressingPackaging> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumPressingPackagingExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumPressingPackagingExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumPressingPackagingExcelDto.class, "基础资料-整烫包装.xlsx", new ExportParams(), response);

    }


    /**
     * 方法描述：新增修改基础资料-整烫包装
     *
     * @param addRevampBasicsdatumPressingPackagingDto 基础资料-整烫包装Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumPressingPackaging(AddRevampBasicsdatumPressingPackagingDto addRevampBasicsdatumPressingPackagingDto) {
        BasicsdatumPressingPackaging basicsdatumPressingPackaging = new BasicsdatumPressingPackaging();
        if (StringUtils.isEmpty(addRevampBasicsdatumPressingPackagingDto.getId())) {
            QueryWrapper<BasicsdatumPressingPackaging> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumPressingPackagingDto, basicsdatumPressingPackaging);
            basicsdatumPressingPackaging.setCompanyCode(baseController.getUserCompany());
            basicsdatumPressingPackaging.insertInit();
            baseMapper.insert(basicsdatumPressingPackaging);
        } else {
            /*修改*/
            basicsdatumPressingPackaging = baseMapper.selectById(addRevampBasicsdatumPressingPackagingDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumPressingPackaging)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumPressingPackagingDto, basicsdatumPressingPackaging);
            basicsdatumPressingPackaging.updateInit();
            baseMapper.updateById(basicsdatumPressingPackaging);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-整烫包装
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumPressingPackaging(String id) {
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
    public Boolean startStopBasicsdatumPressingPackaging(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumPressingPackaging> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}

