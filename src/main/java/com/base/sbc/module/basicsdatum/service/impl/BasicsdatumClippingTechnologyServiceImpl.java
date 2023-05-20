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
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampTechnologyDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumComponentExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumClippingTechnology;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumClippingTechnologyMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumClippingTechnologyService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumTechnologyVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
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

/**
 * 类描述：基础资料-裁剪工艺 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumClippingTechnologyService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-15 17:31:31
 * @version 1.0  
 */
@Service
public class BasicsdatumClippingTechnologyServiceImpl extends ServicePlusImpl<BasicsdatumClippingTechnologyMapper, BasicsdatumClippingTechnology> implements BasicsdatumClippingTechnologyService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private UploadFileService uploadFileService;

    @Override
    public PageInfo<BasicsdatumTechnologyVo> getTechnologyList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumClippingTechnology> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.in(StrUtil.isNotEmpty(queryDto.getCoding()), "coding", queryDto.getCoding());
        /*查询部件数据*/
        List<BasicsdatumClippingTechnology> clippingTechnologyList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumClippingTechnology> pageInfo = new PageInfo<>(clippingTechnologyList);
        /*转换vo*/
        List<BasicsdatumTechnologyVo> list = BeanUtil.copyToList(clippingTechnologyList, BasicsdatumTechnologyVo.class);
        PageInfo<BasicsdatumTechnologyVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumComponentExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumComponentExcelDto.class, params);
        for (BasicsdatumComponentExcelDto basicsdatumComponentExcelDto : list) {
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumComponentExcelDto.getImage())) {
                File file1 = new File(basicsdatumComponentExcelDto.getImage());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumComponentExcelDto.setImage(attachmentVo.getUrl());
            }
            if (StringUtils.isBlank(basicsdatumComponentExcelDto.getDescription())) {
                basicsdatumComponentExcelDto.setDescription("");
            }
        }
        List<BasicsdatumClippingTechnology> basicsdatumComponentList = BeanUtil.copyToList(list, BasicsdatumClippingTechnology.class);
        saveOrUpdateBatch(basicsdatumComponentList);
        return true;
    }

    /**
     * 基础资料-裁剪工艺导出
     *
     * @param response
     * @return
     */
    @Override
    public void deriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumClippingTechnology> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumComponentExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumComponentExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumComponentExcelDto.class, "裁剪工艺.xlsx",new ExportParams() ,response);
    }

    @Override
    public Boolean addRevampTechnology(AddRevampTechnologyDto addRevampTechnologyDto) {
        BasicsdatumClippingTechnology basicsdatumClippingTechnology = new BasicsdatumClippingTechnology();
        if (StringUtils.isEmpty(basicsdatumClippingTechnology.getId())) {
            QueryWrapper<BasicsdatumClippingTechnology> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("coding",addRevampTechnologyDto.getCoding());
            /*查询数据是否存在*/
            List<BasicsdatumClippingTechnology> list = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampTechnologyDto, basicsdatumClippingTechnology);
            basicsdatumClippingTechnology.setCompanyCode(baseController.getUserCompany());
            basicsdatumClippingTechnology.insertInit();
            baseMapper.insert(basicsdatumClippingTechnology);
        } else {
            /*修改*/
            basicsdatumClippingTechnology = baseMapper.selectById(addRevampTechnologyDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumClippingTechnology)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampTechnologyDto, basicsdatumClippingTechnology);
            basicsdatumClippingTechnology.updateInit();
            baseMapper.updateById(basicsdatumClippingTechnology);
        }
        return true;
    }

    @Override
    public Boolean delTechnology(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public Boolean technologyStartStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumClippingTechnology> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
