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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumRangeDifferenceDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumRangeDifferenceExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMeasurementMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumRangeDifferenceMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumRangeDifferenceService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.difference.entity.Difference;
import com.base.sbc.module.difference.service.DifferenceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-档差 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumRangeDifferenceService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-18 19:42:16
 */
@Service
@RequiredArgsConstructor
public class BasicsdatumRangeDifferenceServiceImpl extends BaseServiceImpl<BasicsdatumRangeDifferenceMapper, BasicsdatumRangeDifference> implements BasicsdatumRangeDifferenceService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private UploadFileService uploadFileService;

    private final DifferenceService differenceService;

    private final BasicsdatumMeasurementMapper basicsdatumMeasurementMapper;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-档差分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumRangeDifferenceVo> getList(QueryDto queryDto) {
        /*分页*/
        if (queryDto.getPageNum() != 0 & queryDto.getPageSize() != 0) {
            PageHelper.startPage(queryDto);
        }
        BaseQueryWrapper<BasicsdatumRangeDifference> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.notEmptyLike("model_type", queryDto.getModelType());
        queryWrapper.notEmptyLike("create_name", queryDto.getCreateName());
        queryWrapper.notEmptyLike("code", queryDto.getCode());
        queryWrapper.notEmptyLike("range_difference", queryDto.getRangeDifference());
        queryWrapper.notEmptyEq("status", queryDto.getStatus());
        queryWrapper.between("create_date", queryDto.getCreateDate());
        queryWrapper.orderByDesc("create_date");
        /*查询基础资料-档差数据*/
        List<BasicsdatumRangeDifference> basicsdatumRangeDifferenceList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumRangeDifference> pageInfo = new PageInfo<>(basicsdatumRangeDifferenceList);
        /*转换vo*/
        List<BasicsdatumRangeDifferenceVo> list = BeanUtil.copyToList(basicsdatumRangeDifferenceList, BasicsdatumRangeDifferenceVo.class);
        PageInfo<BasicsdatumRangeDifferenceVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        BeanUtil.copyProperties(pageInfo, pageInfo1, "list");
        return pageInfo1;
    }


    /**
     * 基础资料-档差导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumRangeDifferenceExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumRangeDifferenceExcelDto.class, params);
        list = list.stream().filter(d -> StringUtils.isNotBlank(d.getCode())).collect(Collectors.toList());
        for (BasicsdatumRangeDifferenceExcelDto basicsdatumRangeDifferenceExcelDto : list) {
            if (!StringUtils.isEmpty(basicsdatumRangeDifferenceExcelDto.getPicture())) {
                File file1 = new File(basicsdatumRangeDifferenceExcelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumRangeDifferenceExcelDto.setPicture(attachmentVo.getUrl());
            }
            /*去掉空格*/
            if(StringUtils.isNotBlank(basicsdatumRangeDifferenceExcelDto.getSize())){
                basicsdatumRangeDifferenceExcelDto.setSize(basicsdatumRangeDifferenceExcelDto.getSize().replaceAll(" ",""));
            }
            /*获取测量点id*/
            if(!StringUtils.isEmpty(basicsdatumRangeDifferenceExcelDto.getMeasurement())){
              basicsdatumRangeDifferenceExcelDto.setMeasurement(basicsdatumRangeDifferenceExcelDto.getMeasurement().replaceAll(" ",""));
              String[] measurement =  basicsdatumRangeDifferenceExcelDto.getMeasurement().split(",");
                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.in("measurement",measurement);
                List<BasicsdatumMeasurement> basicsdatumSizeList =basicsdatumMeasurementMapper.selectList(queryWrapper);
                if(!CollectionUtils.isEmpty(basicsdatumSizeList)){
                    List<String> stringList =  basicsdatumSizeList.stream().map(BasicsdatumMeasurement::getId).collect(Collectors.toList());
                    basicsdatumRangeDifferenceExcelDto.setMeasurementIds( StringUtils.join(stringList,","));
                }
            }
        }
        List<BasicsdatumRangeDifference> basicsdatumRangeDifferenceList = BeanUtil.copyToList(list, BasicsdatumRangeDifference.class);
        for (BasicsdatumRangeDifference basicsdatumRangeDifference : basicsdatumRangeDifferenceList) {
            QueryWrapper<BasicsdatumRangeDifference> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", basicsdatumRangeDifference.getCode());
            this.saveOrUpdate(basicsdatumRangeDifference, queryWrapper);
        }
        return true;
    }

    /**
     * 基础资料-档差导出
     *
     * @param
     * @return
     */
    @Override
    public void deriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumRangeDifference> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumRangeDifferenceExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumRangeDifferenceExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumRangeDifferenceExcelDto.class, "基础资料-档差.xlsx", new ExportParams(), response);
    }


    /**
     * 方法描述：新增修改基础资料-档差
     *
     * @param addRevampBasicsdatumRangeDifferenceDto 基础资料-档差Dto类
     * @return boolean
     */
    @Override
    @Transactional
    public Boolean addRevamp(AddRevampBasicsdatumRangeDifferenceDto addRevampBasicsdatumRangeDifferenceDto) {
        BasicsdatumRangeDifference basicsdatumRangeDifference = new BasicsdatumRangeDifference();
        QueryWrapper<BasicsdatumRangeDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", addRevampBasicsdatumRangeDifferenceDto.getCode());
        /*查询数据是否存在*/
        List<BasicsdatumRangeDifference> list = baseMapper.selectList(queryWrapper);
        if (StringUtils.isEmpty(addRevampBasicsdatumRangeDifferenceDto.getId())) {
            if (!CollectionUtils.isEmpty(list)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumRangeDifferenceDto, basicsdatumRangeDifference);
            basicsdatumRangeDifference.setCompanyCode(baseController.getUserCompany());
            basicsdatumRangeDifference.insertInit();
            baseMapper.insert(basicsdatumRangeDifference);
        } else {
            /*修改*/
            basicsdatumRangeDifference = baseMapper.selectById(addRevampBasicsdatumRangeDifferenceDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumRangeDifference)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            if (!addRevampBasicsdatumRangeDifferenceDto.getCode().equals(addRevampBasicsdatumRangeDifferenceDto.getCode()) && !CollectionUtils.isEmpty(list)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumRangeDifferenceDto, basicsdatumRangeDifference);
            basicsdatumRangeDifference.updateInit();
            baseMapper.updateById(basicsdatumRangeDifference);
        }
        for (Difference difference : addRevampBasicsdatumRangeDifferenceDto.getDifferenceList()) {
            difference.setPlateBillCode(addRevampBasicsdatumRangeDifferenceDto.getId());
        }

        differenceService.addAndUpdateAndDelList(addRevampBasicsdatumRangeDifferenceDto.getDifferenceList(),new BaseQueryWrapper<Difference>().eq("plate_bill_code",addRevampBasicsdatumRangeDifferenceDto.getId()));
        return true;
    }


    /**
     * 方法描述：删除基础资料-档差
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean del(String id) {
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
    public Boolean startStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumRangeDifference> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
