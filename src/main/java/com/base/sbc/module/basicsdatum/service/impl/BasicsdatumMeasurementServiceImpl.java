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
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumComponent;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumComponentVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMeasurementVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMeasurementMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 类描述：基础资料-测量点 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 9:35:14
 * @version 1.0  
 */
@Service
public class BasicsdatumMeasurementServiceImpl extends ServicePlusImpl<BasicsdatumMeasurementMapper, BasicsdatumMeasurement> implements BasicsdatumMeasurementService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private CcmFeignService ccmFeignService;

    /**
     * 方法描述：分页查询测量点
     *
     * @param queryDto 查询条件
     * @return PageInfo<BasicsdatumTechnologyVo>
     */
    @Override
    public PageInfo<BasicsdatumMeasurementVo> getMeasurement(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumMeasurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.in(StrUtil.isNotEmpty(queryDto.getMeasurement()), "measurement", queryDto.getMeasurement());
        /*查询部件数据*/
        List<BasicsdatumMeasurement> basicsdatumComponentList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumComponent> pageInfo = new PageInfo(basicsdatumComponentList);
        /*转换vo*/
        List<BasicsdatumMeasurementVo> list = BeanUtil.copyToList(basicsdatumComponentList, BasicsdatumMeasurementVo.class);
        PageInfo<BasicsdatumMeasurementVo> vo = new PageInfo<>();
        vo.setList(list);
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(pageInfo.getPageNum());
        vo.setPageSize(pageInfo.getPageSize());
        return vo;
    }

    /**
     * 方法描述：传入Excel导入
     *
     * @param file 文件
     * @return boolean
     */
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumMeasurementExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumMeasurementExcelDto.class, params);
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_DimType");
        Map<String, String> map =   dictInfoToMap.get("C8_DimType");
        for (BasicsdatumMeasurementExcelDto basicsdatumMeasurementExcelDto : list) {
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumMeasurementExcelDto.getImage())) {
                File file1 = new File(basicsdatumMeasurementExcelDto.getImage());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumMeasurementExcelDto.setImage(attachmentVo.getUrl());
            }
            if (StringUtils.isBlank(basicsdatumMeasurementExcelDto.getDescription())) {
                basicsdatumMeasurementExcelDto.setDescription("");
            }
            if (StringUtils.isBlank(basicsdatumMeasurementExcelDto.getDescriptionAlt())) {
                basicsdatumMeasurementExcelDto.setDescriptionAlt("");
            }
            if (!StringUtils.isBlank(basicsdatumMeasurementExcelDto.getPdmType())) {
                for(Map.Entry<String, String> entry : map.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumMeasurementExcelDto.getPdmType())){
                        basicsdatumMeasurementExcelDto.setPdmType(key);
                        break;
                    }

                }

            }
        }
        List<BasicsdatumMeasurement> basicsdatumMeasurementList = BeanUtil.copyToList(list, BasicsdatumMeasurement.class);
        saveOrUpdateBatch(basicsdatumMeasurementList);
        return true;
    }

    /**
     * 基础资料-测量点导出
     *
     * @param response
     * @return
     */
    @Override
    public void deriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumMeasurement> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumMeasurementExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumMeasurementExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumMeasurementExcelDto.class, "基础资料-测量点.xlsx",new ExportParams() ,response);
    }

    /**
     * 方法描述：新增修改基础资料-部件
     *
     * @param addRevampMeasurementDto 部件Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampMeasurement(AddRevampMeasurementDto addRevampMeasurementDto) {
        BasicsdatumMeasurement basicsdatumMeasurement = new BasicsdatumMeasurement();
        if (StringUtils.isEmpty(addRevampMeasurementDto.getId())) {
            QueryWrapper<BasicsdatumMeasurement> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("measurement",addRevampMeasurementDto.getMeasurement());
            /*查询数据是否存在*/
            List<BasicsdatumMeasurement> list = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampMeasurementDto, basicsdatumMeasurement);
            basicsdatumMeasurement.setCompanyCode(baseController.getUserCompany());
            basicsdatumMeasurement.insertInit();
            baseMapper.insert(basicsdatumMeasurement);
        } else {
            /*修改*/
            basicsdatumMeasurement = baseMapper.selectById(addRevampMeasurementDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumMeasurement)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampMeasurementDto, basicsdatumMeasurement);
            basicsdatumMeasurement.updateInit();
            baseMapper.updateById(basicsdatumMeasurement);
        }
        return true;
    }

    /**
     * 方法描述：删除部件数据
     *
     * @param id 部件id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delMeasurement(String id) {
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
    public Boolean measurementStartStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumMeasurement> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }


}
