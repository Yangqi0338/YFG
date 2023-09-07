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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampMeasurementDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMeasurementExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMeasurementMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMeasurementVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.Page;
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
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-测量点 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 9:35:14
 * @version 1.0
 */
@Service
public class BasicsdatumMeasurementServiceImpl extends BaseServiceImpl<BasicsdatumMeasurementMapper, BasicsdatumMeasurement> implements BasicsdatumMeasurementService {

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
    public PageInfo getMeasurement(QueryDto queryDto) {
        BaseQueryWrapper<BasicsdatumMeasurement> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.andLike(queryDto.getSearch(), "description", "code", "measurement");
        queryWrapper.like(StringUtils.isNotEmpty(queryDto.getPdmType()), "PDM_type", queryDto.getPdmType());
        queryWrapper.like(StringUtils.isNotEmpty(queryDto.getDescription()),"description",queryDto.getDescription());
        queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCreateName()),"create_name",queryDto.getCreateName());
        queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCode()),"code",queryDto.getCode());
        queryWrapper.in(StringUtils.isNotEmpty(queryDto.getCodes()),"code",StringUtils.convertList(queryDto.getCodes()));
        queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getStatus()),"status", queryDto.getStatus());
        if (queryDto.getCreateDate()!=null && queryDto.getCreateDate().length>0){
            queryWrapper.ge(StringUtils.isNotEmpty(queryDto.getCreateDate()[0]),"create_date",queryDto.getCreateDate()[0]);
            if (queryDto.getCreateDate().length>1){
                queryWrapper.and(i->i.le(StringUtils.isNotEmpty(queryDto.getCreateDate()[1]),"create_date",queryDto.getCreateDate()[1]));
            }
        }
        queryWrapper.orderByDesc("create_date");
        queryWrapper.like(StrUtil.isNotEmpty(queryDto.getMeasurement()), "measurement", queryDto.getMeasurement());
        /*查询基础资料-号型类型数据*/
        Page<BasicsdatumMeasurementVo> objects = PageHelper.startPage(queryDto);
        getBaseMapper().selectList(queryWrapper);
        return objects.toPageInfo();
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
            if (!StringUtils.isBlank(basicsdatumMeasurementExcelDto.getPdmTypeName())) {
                for(Map.Entry<String, String> entry : map.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumMeasurementExcelDto.getPdmType())){
                        basicsdatumMeasurementExcelDto.setPdmType(key);
                        basicsdatumMeasurementExcelDto.setPdmTypeName(value);
                        break;
                    }

                }

            }
        }
        list = list.stream().filter(m -> StringUtils.isNotBlank(m.getCode())).collect(Collectors.toList());
        List<BasicsdatumMeasurement> basicsdatumMeasurementList = BeanUtil.copyToList(list, BasicsdatumMeasurement.class);
        for (BasicsdatumMeasurement basicsdatumMeasurement : basicsdatumMeasurementList) {
            QueryWrapper<BasicsdatumMeasurement> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("code",basicsdatumMeasurement.getCode());
            this.saveOrUpdate(basicsdatumMeasurement,queryWrapper);
        }

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
        QueryWrapper<BasicsdatumMeasurement> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code",addRevampMeasurementDto.getCode());
        /*查询数据是否存在*/
        List<BasicsdatumMeasurement> list = baseMapper.selectList(queryWrapper);
        if (StringUtils.isEmpty(addRevampMeasurementDto.getId())) {
            /*新增*/
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
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
            if(!basicsdatumMeasurement.getCode().equals(addRevampMeasurementDto.getCode()) && !CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
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

    /**
     * 查询所有数据
     *
     * @param companyCode 企业编码
     * @return List<BasicsdatumMeasurementVo>
     */
    @Override
    public List<BasicsdatumMeasurement> getAllMeasurement(String companyCode) {
        BaseQueryWrapper<BasicsdatumMeasurement> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", companyCode);
        queryWrapper.orderByDesc("create_date");
        List<BasicsdatumMeasurement> list = getBaseMapper().selectList(queryWrapper);
        return list;
    }
}
