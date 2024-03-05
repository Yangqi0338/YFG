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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibraryAgentExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryAgentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibraryAgent;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryAgentMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryAgentService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryAgentVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-颜色库 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryAgentService
 * @email your email
 * @date 创建时间：2024-2-28 16:13:32
 */
@Service
public class BasicsdatumColourLibraryAgentServiceImpl extends BaseServiceImpl<BasicsdatumColourLibraryAgentMapper, BasicsdatumColourLibraryAgent> implements BasicsdatumColourLibraryAgentService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private BasicsdatumColourLibraryService colourLibraryService;


    @Override
    public PageInfo<BasicsdatumColourLibraryAgentVo> findPage(QueryBasicsdatumColourLibraryAgentDto dto) {
        BaseQueryWrapper<BasicsdatumColourLibraryAgent> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("t.company_code", baseController.getUserCompany());
        queryWrapper.notEmptyEq("t.status", dto.getStatus());
        queryWrapper.notEmptyLike("t.create_name", dto.getCreateName());
        queryWrapper.between("t.create_date", dto.getCreateDate());
        queryWrapper.notEmptyLike("t.colour_code", dto.getColourCode());
        queryWrapper.notEmptyLike("t.colour_specification", dto.getColourSpecification());
        queryWrapper.notEmptyLike("t.colour_name", dto.getColourName());
        queryWrapper.notEmptyLike("t.library", dto.getLibrary());
        queryWrapper.notEmptyEq("t.is_style", dto.getIsStyle());
        queryWrapper.notEmptyEq("t.is_materials", dto.getIsMaterials());
        queryWrapper.notEmptyLike("t.pantone", dto.getPantone());
        queryWrapper.notEmptyLike("t.color_type", dto.getColorType());
        queryWrapper.notEmptyIn("t.scm_send_flag", StringUtils.convertList(dto.getScmSendFlag()));
        queryWrapper.andLike(dto.getSearch(), "t.colour_code", "t.colour_name");
        queryWrapper.notEmptyEq("t.brand", dto.getBrand());

        queryWrapper.eq("t.del_flag","0");

        queryWrapper.orderByDesc("t.create_date");
        Page<BasicsdatumColourLibraryAgentVo> objects = PageHelper.startPage(dto);
        getBaseMapper().findList(queryWrapper);
        minioUtils.setObjectUrlToList(objects.toPageInfo().getList(), "picture");
        return objects.toPageInfo();
    }

    @Override
    public ApiResult importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumColourLibraryAgentExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumColourLibraryAgentExcelDto.class, params);
        //过滤掉无编码数据
        list = list.stream().filter(c -> StringUtils.isNotBlank(c.getColourCode())).collect(Collectors.toList());
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorChroma,C8_ColorType,C8_Brand");
        /*色度*/
        Map<String, String> mapColorChroma = dictInfoToMap.get("C8_ColorChroma");
        /*色系*/
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");
        /*品牌*/
        Map<String, String> brandMap = dictInfoToMap.get("C8_Brand");
        /*只保存有颜色编码数据*/
        List<BasicsdatumColourLibraryAgentExcelDto> libraryExcelDtoList = new ArrayList<>();

        //校验关联的集团颜色编码是否存在
        List<String> sysColorCodeList = new ArrayList<>();

        for (BasicsdatumColourLibraryAgentExcelDto excelDto : list) {
            if (StringUtils.isEmpty(excelDto.getBrandName())) {
                throw new OtherException("品牌名称不能为空");
            }
            for (Map.Entry<String, String> entry : brandMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(excelDto.getBrandName())) {
                    excelDto.setBrand(key);
                    break;
                }
            }
            if (StringUtils.isEmpty(excelDto.getBrand())) {
                throw new OtherException("品牌名称:"+excelDto.getBrandName()+"不存在");
            }

            /*色度*/
            if (StringUtils.isNotBlank(excelDto.getChromaName())) {
                for (Map.Entry<String, String> entry : mapColorChroma.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(excelDto.getChromaName())) {
                        excelDto.setChroma(key);
                        excelDto.setChromaName(value);
                        break;
                    }
                }
            }
            /*色系*/
            if (StringUtils.isNotBlank(excelDto.getColorTypeName())) {
                for (Map.Entry<String, String> entry : mapColorType.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(excelDto.getColorTypeName())) {
                        excelDto.setColorType(key);
                        excelDto.setColorTypeName(value);
                        break;
                    }
                }
            }
            //如果图片不为空
            if (StringUtils.isNotEmpty(excelDto.getPicture())) {
                File file1 = new File(excelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1), "MaterialOther/ColourLibrary/" + excelDto.getBrandName()+excelDto.getColourCode() + ".jpg");
                excelDto.setPicture(CommonUtils.removeQuery(attachmentVo.getUrl()));
            }
            if (StringUtils.isNotBlank(excelDto.getColorRgb()) && !excelDto.getColorRgb().contains("rgb")) {
                excelDto.setColor16(StringUtils.rgbToHex(excelDto.getColorRgb()));
            }

            if (StrUtil.isNotBlank(excelDto.getSysColorCode())) {
                sysColorCodeList.add(excelDto.getSysColorCode());
            }

            libraryExcelDtoList.add(excelDto);
        }

        if(CollUtil.isNotEmpty(sysColorCodeList)){
            List<BasicsdatumColourLibrary> colourLibraryList = colourLibraryService.listByCode(sysColorCodeList);
            Map<String, BasicsdatumColourLibrary> colorMap = colourLibraryList.stream().collect(Collectors.toMap(BasicsdatumColourLibrary::getColourCode, o -> o));
            for (BasicsdatumColourLibraryAgentExcelDto excelDto : libraryExcelDtoList) {
                if (StrUtil.isNotBlank(excelDto.getSysColorCode())) {
                    if (colorMap.containsKey(excelDto.getSysColorCode())) {
                        excelDto.setColorId(colorMap.get(excelDto.getSysColorCode()).getId());
                    } else {
                        throw new OtherException("关联的集团颜色编码不存在"+excelDto.getSysColorCode());
                    }
                }
            }
        }

        List<BasicsdatumColourLibraryAgent> finalList = BeanUtil.copyToList(libraryExcelDtoList, BasicsdatumColourLibraryAgent.class);
        /*按颜色编码修改*/
        for (BasicsdatumColourLibraryAgent basicsdatumColourLibraryAgent : finalList) {
            QueryWrapper<BasicsdatumColourLibraryAgent> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("colour_code",basicsdatumColourLibraryAgent.getColourCode());
            queryWrapper.eq("brand",basicsdatumColourLibraryAgent.getBrand());
            this.saveOrUpdate(basicsdatumColourLibraryAgent,queryWrapper);
        }

        return ApiResult.success();
    }

    @Override
    public void exportExcel(HttpServletResponse response, QueryBasicsdatumColourLibraryAgentDto dto) throws IOException {
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorChroma,C8_ColorType");
        /*色度*/
        Map<String, String> mapColorChroma = dictInfoToMap.get("C8_ColorChroma");
        /*色系*/
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");

        dto.setPageSize(Integer.MAX_VALUE);
        PageInfo<BasicsdatumColourLibraryAgentVo> page = findPage(dto);
        List<BasicsdatumColourLibraryAgentVo> list = page.getList();
        list.forEach(l -> {
            l.setChroma(mapColorChroma.get(l.getChroma()));
            l.setColorType(mapColorType.get(l.getColorType()));
        });
        List<BasicsdatumColourLibraryAgentExcelDto> excelDtoList = BeanUtil.copyToList(list, BasicsdatumColourLibraryAgentExcelDto.class);
        ExcelUtils.exportExcel(excelDtoList, BasicsdatumColourLibraryAgentExcelDto.class, "基础资料-代理品牌颜色库.xlsx", new ExportParams(), response);
    }

    @Override
    public void statusUpdate(StartStopDto startStopDto) {
        LambdaUpdateWrapper<BasicsdatumColourLibraryAgent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(BasicsdatumColourLibraryAgent::getStatus, startStopDto.getStatus());
        updateWrapper.eq(BasicsdatumColourLibraryAgent::getId, startStopDto.getIds());
        update(updateWrapper);
    }

    @Override
    public void del(String id) {
        //暂无校验
        removeById(id);
    }

    @Override
    public void saveOrUpdateMian(BasicsdatumColourLibraryAgent basicsdatumColourLibraryAgent) {
        CommonUtils.removeQuery(basicsdatumColourLibraryAgent, "picture");
        if (StrUtil.isEmpty(basicsdatumColourLibraryAgent.getId())) {
            //新增
            //校验颜色CODE是否重复
            LambdaQueryWrapper<BasicsdatumColourLibraryAgent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BasicsdatumColourLibraryAgent::getColourCode, basicsdatumColourLibraryAgent.getColourCode());
            queryWrapper.eq(BasicsdatumColourLibraryAgent::getBrand, basicsdatumColourLibraryAgent.getBrand());
            long count = count(queryWrapper);
            if (count > 0) {
                throw new OtherException("颜色代码重复:" + basicsdatumColourLibraryAgent.getColourCode() + basicsdatumColourLibraryAgent.getBrandName());
            }
            //校验集团颜色CODE是否存在
            if (StrUtil.isNotBlank(basicsdatumColourLibraryAgent.getSysColorCode())) {
                List<BasicsdatumColourLibrary> colourLibraryList = colourLibraryService.listByCode(Collections.singletonList(basicsdatumColourLibraryAgent.getColorId()));
                if (colourLibraryList.isEmpty()) {
                    throw new OtherException("关联的集团颜色编码不存在");
                }
                String id = colourLibraryList.get(0).getId();
                basicsdatumColourLibraryAgent.setColorId(id);
            }
            basicsdatumColourLibraryAgent.setCompanyCode(baseController.getUserCompany());
            basicsdatumColourLibraryAgent.insertInit();
            save(basicsdatumColourLibraryAgent);
        } else {
            //修改
            BasicsdatumColourLibraryAgent byId = getById(basicsdatumColourLibraryAgent.getId());
            if (ObjectUtils.isEmpty(byId)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            basicsdatumColourLibraryAgent.updateInit();
            updateById(basicsdatumColourLibraryAgent);
        }
    }
}
