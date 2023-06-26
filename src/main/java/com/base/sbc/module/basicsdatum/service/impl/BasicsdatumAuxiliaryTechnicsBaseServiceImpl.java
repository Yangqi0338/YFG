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
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumAuxiliaryTechnicsMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumAuxiliaryTechnics;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumAuxiliaryTechnicsVo;
import com.base.sbc.module.basicsdatum.service.BasicsdatumAuxiliaryTechnicsService;
import com.base.sbc.module.common.vo.AttachmentVo;
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

import java.io.File;
import java.util.List;

/**
 * 类描述：基础资料-外辅工艺 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumAuxiliaryTechnicsService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:56
 */
@Service
public class BasicsdatumAuxiliaryTechnicsBaseServiceImpl extends BaseServiceImpl<BasicsdatumAuxiliaryTechnicsMapper, BasicsdatumAuxiliaryTechnics> implements BasicsdatumAuxiliaryTechnicsService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private MinioUtils minioUtils;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-外辅工艺分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumAuxiliaryTechnicsVo> getBasicsdatumAuxiliaryTechnicsList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumAuxiliaryTechnics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询基础资料-外辅工艺数据*/
        List<BasicsdatumAuxiliaryTechnics> basicsdatumAuxiliaryTechnicsList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumAuxiliaryTechnics> pageInfo = new PageInfo<>(basicsdatumAuxiliaryTechnicsList);
        /*转换vo*/
        List<BasicsdatumAuxiliaryTechnicsVo> list = BeanUtil.copyToList(basicsdatumAuxiliaryTechnicsList, BasicsdatumAuxiliaryTechnicsVo.class);
        PageInfo<BasicsdatumAuxiliaryTechnicsVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 基础资料-外辅工艺导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumAuxiliaryTechnicsImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumAuxiliaryTechnicsExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumAuxiliaryTechnicsExcelDto.class, params);
        for (BasicsdatumAuxiliaryTechnicsExcelDto basicsdatumAuxiliaryTechnicsExcelDto : list) {

            if (!StringUtils.isEmpty(basicsdatumAuxiliaryTechnicsExcelDto.getPicture())) {
                if (StringUtils.isNotEmpty(basicsdatumAuxiliaryTechnicsExcelDto.getPicture())) {
                    File file1 = new File(basicsdatumAuxiliaryTechnicsExcelDto.getPicture());
                    /*上传图*/
                    AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                    basicsdatumAuxiliaryTechnicsExcelDto.setPicture(attachmentVo.getUrl());
                }
            }
        }
        List<BasicsdatumAuxiliaryTechnics> basicsdatumAuxiliaryTechnicsList = BeanUtil.copyToList(list, BasicsdatumAuxiliaryTechnics.class);
        saveOrUpdateBatch(basicsdatumAuxiliaryTechnicsList);
        return true;
    }

    /**
     * 基础资料-外辅工艺导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumAuxiliaryTechnicsDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumAuxiliaryTechnics> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumAuxiliaryTechnicsExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumAuxiliaryTechnicsExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumAuxiliaryTechnicsExcelDto.class, "基础资料-外辅工艺.xlsx",new ExportParams() ,response);
    }


    /**
     * 方法描述：新增修改基础资料-外辅工艺
     *
     * @param addRevampBasicsdatumAuxiliaryTechnicsDto 基础资料-外辅工艺Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumAuxiliaryTechnics(AddRevampBasicsdatumAuxiliaryTechnicsDto addRevampBasicsdatumAuxiliaryTechnicsDto) {
        BasicsdatumAuxiliaryTechnics basicsdatumAuxiliaryTechnics = new BasicsdatumAuxiliaryTechnics();
        if (StringUtils.isEmpty(addRevampBasicsdatumAuxiliaryTechnicsDto.getId())) {
            QueryWrapper<BasicsdatumAuxiliaryTechnics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("technics_code",addRevampBasicsdatumAuxiliaryTechnicsDto.getTechnicsCode());
            queryWrapper.eq("company_code",baseController.getUserCompany());
            if(!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumAuxiliaryTechnicsDto, basicsdatumAuxiliaryTechnics);
            basicsdatumAuxiliaryTechnics.setCompanyCode(baseController.getUserCompany());
            basicsdatumAuxiliaryTechnics.insertInit();
            baseMapper.insert(basicsdatumAuxiliaryTechnics);
        } else {
            /*修改*/
            basicsdatumAuxiliaryTechnics = baseMapper.selectById(addRevampBasicsdatumAuxiliaryTechnicsDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumAuxiliaryTechnics)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumAuxiliaryTechnicsDto, basicsdatumAuxiliaryTechnics);
            basicsdatumAuxiliaryTechnics.updateInit();
            baseMapper.updateById(basicsdatumAuxiliaryTechnics);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-外辅工艺
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumAuxiliaryTechnics(String id) {
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
    public Boolean startStopBasicsdatumAuxiliaryTechnics(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumAuxiliaryTechnics> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}

