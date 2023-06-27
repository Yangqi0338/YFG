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
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumBasicsTechnicsDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumBasicsTechnicsExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBasicsTechnics;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBasicsTechnicsMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBasicsTechnicsService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBasicsTechnicsVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
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
 * 类描述：基础资料-基础工艺 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBasicsTechnicsService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 */
@Service
public class BasicsdatumBasicsTechnicsServiceImpl extends BaseServiceImpl<BasicsdatumBasicsTechnicsMapper, BasicsdatumBasicsTechnics> implements BasicsdatumBasicsTechnicsService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private MinioUtils minioUtils;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-基础工艺分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumBasicsTechnicsVo> getBasicsdatumBasicsTechnicsList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumBasicsTechnics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询基础资料-基础工艺数据*/
        List<BasicsdatumBasicsTechnics> basicsdatumBasicsTechnicsList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumBasicsTechnics> pageInfo = new PageInfo<>(basicsdatumBasicsTechnicsList);
        /*转换vo*/
        List<BasicsdatumBasicsTechnicsVo> list = BeanUtil.copyToList(basicsdatumBasicsTechnicsList, BasicsdatumBasicsTechnicsVo.class);
        PageInfo<BasicsdatumBasicsTechnicsVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 基础资料-基础工艺导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean basicsdatumBasicsTechnicsImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumBasicsTechnicsExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumBasicsTechnicsExcelDto.class, params);
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_SewingType");
        Map<String, String> map = dictInfoToMap.get("C8_SewingType");
        for (BasicsdatumBasicsTechnicsExcelDto basicsdatumBasicsTechnicsExcelDto : list) {
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumBasicsTechnicsExcelDto.getPicture())) {
                File file1 = new File(basicsdatumBasicsTechnicsExcelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumBasicsTechnicsExcelDto.setPicture(attachmentVo.getUrl());
            }
            if (StringUtils.isNotEmpty(basicsdatumBasicsTechnicsExcelDto.getTechnicsType())) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumBasicsTechnicsExcelDto.getTechnicsType())) {
                        basicsdatumBasicsTechnicsExcelDto.setTechnicsType(key);
                        break;
                    }
                }
            }
        }
        List<BasicsdatumBasicsTechnics> basicsdatumBasicsTechnicsList = BeanUtil.copyToList(list, BasicsdatumBasicsTechnics.class);
        saveOrUpdateBatch(basicsdatumBasicsTechnicsList);
        return true;
    }

    /**
     * 基础资料-基础工艺导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumBasicsTechnicsDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumBasicsTechnics> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumBasicsTechnicsExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumBasicsTechnicsExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumBasicsTechnicsExcelDto.class, "基础工艺.xlsx",new ExportParams() ,response);
    }


    /**
     * 方法描述：新增修改基础资料-基础工艺
     *
     * @param addRevampBasicsdatumBasicsTechnicsDto 基础资料-基础工艺Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumBasicsTechnics(AddRevampBasicsdatumBasicsTechnicsDto addRevampBasicsdatumBasicsTechnicsDto) {
        BasicsdatumBasicsTechnics basicsdatumBasicsTechnics = new BasicsdatumBasicsTechnics();
        if (StringUtils.isEmpty(addRevampBasicsdatumBasicsTechnicsDto.getId())) {
            /*查询数据重复*/
            QueryWrapper<BasicsdatumBasicsTechnics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("coding",addRevampBasicsdatumBasicsTechnicsDto.getCoding());
            queryWrapper.eq("company_code",baseController.getUserCompany());
            if(!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumBasicsTechnicsDto, basicsdatumBasicsTechnics);
            basicsdatumBasicsTechnics.setCompanyCode(baseController.getUserCompany());
            basicsdatumBasicsTechnics.insertInit();
            baseMapper.insert(basicsdatumBasicsTechnics);
        } else {
            /*修改*/
            basicsdatumBasicsTechnics = baseMapper.selectById(addRevampBasicsdatumBasicsTechnicsDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumBasicsTechnics)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumBasicsTechnicsDto, basicsdatumBasicsTechnics);
            basicsdatumBasicsTechnics.updateInit();
            baseMapper.updateById(basicsdatumBasicsTechnics);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-基础工艺
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumBasicsTechnics(String id) {
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
    public Boolean startStopBasicsdatumBasicsTechnics(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumBasicsTechnics> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
