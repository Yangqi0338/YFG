/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumSupplierExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSupplierMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSupplierVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-供应商 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-22 10:51:07
 */
@Service
public class BasicsdatumSupplierServiceImpl extends BaseServiceImpl<BasicsdatumSupplierMapper, BasicsdatumSupplier> implements BasicsdatumSupplierService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private CcmFeignService ccmFeignService;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-供应商分页查询
     *
     * @param queryRevampBasicsdatumSupplierDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumSupplierVo> getBasicsdatumSupplierList(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
        /*分页*/
        PageHelper.startPage(queryRevampBasicsdatumSupplierDto);
        QueryWrapper<BasicsdatumSupplier> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getSupplier()), "supplier", queryRevampBasicsdatumSupplierDto.getSupplier());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getSupplierCode()), "supplier_code", queryRevampBasicsdatumSupplierDto.getSupplierCode());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getCreditCode()), "credit_code", queryRevampBasicsdatumSupplierDto.getCreditCode());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getSupplierType()), "supplier_type", queryRevampBasicsdatumSupplierDto.getSupplierType());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getFormerSupplierCode()), "former_supplier_code", queryRevampBasicsdatumSupplierDto.getFormerSupplierCode());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getCreateName()), "create_name", queryRevampBasicsdatumSupplierDto.getCreateName());
       if(StringUtils.isNotBlank(queryRevampBasicsdatumSupplierDto.getCreateDate())){
          String[] strings = queryRevampBasicsdatumSupplierDto.getCreateDate().split(",");
           queryWrapper.ge("create_date",strings[0]);
           queryWrapper.le("create_date",strings[1]);
       }

        /*查询基础资料-供应商数据*/
        List<BasicsdatumSupplier> basicsdatumSupplierList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumSupplier> pageInfo = new PageInfo<>(basicsdatumSupplierList);
        /*转换vo*/
        List<BasicsdatumSupplierVo> list = BeanUtil.copyToList(basicsdatumSupplierList, BasicsdatumSupplierVo.class);
        PageInfo<BasicsdatumSupplierVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 基础资料-供应商导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumSupplierImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumSupplierExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumSupplierExcelDto.class, params);
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("TradeTerm,C8_Sync_DataStatus");
        /*结算方式*/
        Map<String, String> mapTradeTerm = dictInfoToMap.get("TradeTerm");
        /*供应商类型*/
        Map<String, String> mapSync = dictInfoToMap.get("C8_Sync_DataStatus");
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getSupplierCode())).collect(Collectors.toList());
        for (BasicsdatumSupplierExcelDto basicsdatumSupplierExcelDto : list) {
            if (StringUtils.isNotEmpty(basicsdatumSupplierExcelDto.getPicture())) {
                File file1 = new File(basicsdatumSupplierExcelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumSupplierExcelDto.setPicture(attachmentVo.getUrl());
            }

            if (StringUtils.isNotEmpty(basicsdatumSupplierExcelDto.getAgentImages())) {
                File file1 = new File(basicsdatumSupplierExcelDto.getAgentImages());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumSupplierExcelDto.setAgentImages(attachmentVo.getUrl());
            }
            /*结算方式*/
            if (StringUtils.isNotBlank(basicsdatumSupplierExcelDto.getClearingForm())) {
                for (Map.Entry<String, String> entry : mapTradeTerm.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumSupplierExcelDto.getClearingForm())) {
                        basicsdatumSupplierExcelDto.setClearingForm(key);
                        break;
                    }
                }
            }
            /*供应商类型*/
            if (StringUtils.isNotBlank(basicsdatumSupplierExcelDto.getSupplierType())) {
                for (Map.Entry<String, String> entry : mapSync.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumSupplierExcelDto.getSupplierType())) {
                        basicsdatumSupplierExcelDto.setSupplierType(key);
                        break;
                    }
                }
            }

        }
        List<BasicsdatumSupplier> basicsdatumSupplierList = BeanUtil.copyToList(list, BasicsdatumSupplier.class);
        for (BasicsdatumSupplier basicsdatumSupplier : basicsdatumSupplierList) {
            QueryWrapper<BasicsdatumSupplier> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("supplier_code",basicsdatumSupplier.getSupplierCode());
            this.saveOrUpdate(basicsdatumSupplier,queryWrapper);
        }
//        saveOrUpdateBatch(basicsdatumSupplierList);
        return true;
    }

    /**
     * 基础资料-供应商导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumSupplierDeriveExcel(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto, HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumSupplier> queryWrapper = new QueryWrapper<>();
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("TradeTerm,C8_Sync_DataStatus");
        /*结算方式*/
        Map<String, String> mapTradeTerm = dictInfoToMap.get("TradeTerm");
        /*供应商类型*/
        Map<String, String> mapSync = dictInfoToMap.get("C8_Sync_DataStatus");

        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getSupplierCode()), "supplier_code", queryRevampBasicsdatumSupplierDto.getSupplierCode());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getCreditCode()), "credit_code", queryRevampBasicsdatumSupplierDto.getCreditCode());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getSupplierType()), "supplier_type", queryRevampBasicsdatumSupplierDto.getSupplierType());
        queryWrapper.like(!StringUtils.isEmpty(queryRevampBasicsdatumSupplierDto.getFormerSupplierCode()), "former_supplier_code", queryRevampBasicsdatumSupplierDto.getFormerSupplierCode());
        List<BasicsdatumSupplier> basicsdatumSuppliers = baseMapper.selectList(queryWrapper);
        basicsdatumSuppliers.forEach(b -> {

            if (StringUtils.isNotBlank(b.getSupplierType())) {
                b.setSupplierType(mapSync.get(b.getSupplierType()));
            }
            if (StringUtils.isNotBlank(b.getClearingForm())) {
                b.setClearingForm(mapTradeTerm.get(b.getClearingForm()));
            }

        });
        List<BasicsdatumSupplierExcelDto> list = BeanUtil.copyToList(basicsdatumSuppliers, BasicsdatumSupplierExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumSupplierExcelDto.class, "基础资料-供应商.xlsx", new ExportParams(), response);

    }


    /**
     * 方法描述：新增修改基础资料-供应商
     *
     * @param addRevampBasicsdatumSupplierDto 基础资料-供应商Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumSupplier(AddRevampBasicsdatumSupplierDto addRevampBasicsdatumSupplierDto) {
        BasicsdatumSupplier basicsdatumSupplier = new BasicsdatumSupplier();
        if (StringUtils.isEmpty(addRevampBasicsdatumSupplierDto.getId())) {
            QueryWrapper<BasicsdatumSupplier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("supplier_code", addRevampBasicsdatumSupplierDto.getSupplierCode());
            if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumSupplierDto, basicsdatumSupplier);
            basicsdatumSupplier.setCompanyCode(baseController.getUserCompany());
            basicsdatumSupplier.setDataState("0");
            basicsdatumSupplier.insertInit();
            baseMapper.insert(basicsdatumSupplier);
        } else {
            /*修改*/
            basicsdatumSupplier = baseMapper.selectById(addRevampBasicsdatumSupplierDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumSupplier)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumSupplierDto, basicsdatumSupplier);
            basicsdatumSupplier.setDataState("1");
            basicsdatumSupplier.updateInit();
            baseMapper.updateById(basicsdatumSupplier);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-供应商
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumSupplier(String id) {
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
    public Boolean startStopBasicsdatumSupplier(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumSupplier> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public PageInfo<BasicsdatumSupplierVo> getSupplierListPopup(QueryRevampBasicsdatumSupplierDto queryDto) {
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumSupplier> qw = new QueryWrapper<>();
        if(StringUtils.isNotBlank(queryDto.getSearch())){
            qw.and(wrapper -> wrapper.like("supplier_code", queryDto.getSearch())
                    .or()
                    .like("supplier", queryDto.getSearch())
                    .or()
                    .like("supplier_abbreviation", queryDto.getSearch()));
        }
        if(StringUtils.isNotBlank(queryDto.getSql())) {
            qw.apply(queryDto.getSql());
        }
        if (!StringUtils.isEmpty(queryDto.getOrderBy())){
            qw.orderByAsc(queryDto.getOrderBy());
        }else {
            qw.orderByDesc("create_date");
        }

        /*查询基础资料-供应商数据*/
        List<BasicsdatumSupplier> basicsdatumSupplierList = baseMapper.selectList(qw);
        PageInfo<BasicsdatumSupplier> pageInfo = new PageInfo<>(basicsdatumSupplierList);
        /*转换vo*/
        List<BasicsdatumSupplierVo> list = BeanUtil.copyToList(basicsdatumSupplierList, BasicsdatumSupplierVo.class);
        PageInfo<BasicsdatumSupplierVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /** 自定义方法区 不替换的区域【other_end】 **/

}
