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
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibraryExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourGroup;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumLavationReminder;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourGroupMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类描述：基础资料-颜色库 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02
 */
@Service
public class BasicsdatumColourLibraryServiceImpl extends BaseServiceImpl<BasicsdatumColourLibraryMapper, BasicsdatumColourLibrary> implements BasicsdatumColourLibraryService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumColourGroupMapper basicsdatumColourGroupMapper;

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    @Lazy
    private SmpService smpService;
/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-颜色库分页查询
     *
     * @param queryBasicsdatumColourLibraryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumColourLibraryVo> getBasicsdatumColourLibraryList(QueryBasicsdatumColourLibraryDto queryBasicsdatumColourLibraryDto) {
        /*分页*/
        PageHelper.startPage(queryBasicsdatumColourLibraryDto);
        BaseQueryWrapper<BasicsdatumColourLibrary> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());


         queryWrapper.notEmptyLike("create_name",queryBasicsdatumColourLibraryDto.getCreateName());
        queryWrapper.between("create_date",queryBasicsdatumColourLibraryDto.getCreateDate());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourCode()), "colour_code", queryBasicsdatumColourLibraryDto.getColourCode());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourSpecification()), "colour_specification", queryBasicsdatumColourLibraryDto.getColourSpecification());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourName()), "colour_name", queryBasicsdatumColourLibraryDto.getColourName());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getLibrary()), "library", queryBasicsdatumColourLibraryDto.getLibrary());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getIsStyle()), "is_style", queryBasicsdatumColourLibraryDto.getIsStyle());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getIsMaterials()), "is_materials", queryBasicsdatumColourLibraryDto.getIsMaterials());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getPantone()), "pantone", queryBasicsdatumColourLibraryDto.getPantone());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColorType()),"color_type", queryBasicsdatumColourLibraryDto.getColorType());

        /*查询基础资料-颜色库数据*/
        queryWrapper.orderByDesc("create_date");
        List<BasicsdatumColourLibrary> basicsdatumColourLibraryList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumColourLibrary> pageInfo = new PageInfo<>(basicsdatumColourLibraryList);
        /*转换vo*/
        List<BasicsdatumColourLibraryVo> list = BeanUtil.copyToList(basicsdatumColourLibraryList, BasicsdatumColourLibraryVo.class);
        for (BasicsdatumColourLibraryVo basicsdatumColourLibraryVo : list) {
            if (StringUtils.isEmpty(basicsdatumColourLibraryVo.getColor16()) && StringUtils.isNotEmpty(basicsdatumColourLibraryVo.getColorRgb())) {
                basicsdatumColourLibraryVo.setColor16(this.rgbToHex(basicsdatumColourLibraryVo.getColorRgb()));
            }
        }
        PageInfo<BasicsdatumColourLibraryVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }

    private String rgbToHex(String rgbValue) {
        int[] rgbComponents = extractRGB(rgbValue);

        String redHex = Integer.toHexString(rgbComponents[0]);
        String greenHex = Integer.toHexString(rgbComponents[1]);
        String blueHex = Integer.toHexString(rgbComponents[2]);
        // 确保十六进制字符串长度为两位
        redHex = padLeft(redHex);
        greenHex = padLeft(greenHex);
        blueHex = padLeft(blueHex);
        return "#" + redHex + greenHex + blueHex;
    }

    private String padLeft(String str) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() + str.length() < 2) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    public static int[] extractRGB(String rgbValue) {
        String[] components = rgbValue.replaceAll("[^\\d,]", "").split(",");
        int red = Integer.parseInt(components[0].trim());
        int green = Integer.parseInt(components[1].trim());
        int blue = Integer.parseInt(components[2].trim());
        return new int[]{red, green, blue};
    }

    /**
     * 基础资料-颜色库导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumColourLibraryImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumColourLibraryExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumColourLibraryExcelDto.class, params);
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorChroma,C8_ColorType");
        /*色度*/
        Map<String, String> mapColorChroma = dictInfoToMap.get("C8_ColorChroma");
        /*色系*/
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");
        /*只保存有颜色编码数据*/
        List<BasicsdatumColourLibraryExcelDto> libraryExcelDtoList = new ArrayList<>();
        for (BasicsdatumColourLibraryExcelDto basicsdatumColourLibraryExcelDto : list) {
        if(StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getColourCode())){

            /*色度*/
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getChroma())) {
                for (Map.Entry<String, String> entry : mapColorChroma.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumColourLibraryExcelDto.getChroma())) {
                        basicsdatumColourLibraryExcelDto.setChroma(key);
                        break;
                    }
                }
            }
            /*色系*/
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getColorType())) {
                for (Map.Entry<String, String> entry : mapColorType.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumColourLibraryExcelDto.getColorType())) {
                        basicsdatumColourLibraryExcelDto.setColorType(key);
                        break;
                    }
                }
            }
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumColourLibraryExcelDto.getPicture())) {
                File file1 = new File(basicsdatumColourLibraryExcelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                basicsdatumColourLibraryExcelDto.setPicture(attachmentVo.getUrl());
            }
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getColorRgb()) && basicsdatumColourLibraryExcelDto.getColorRgb().indexOf("rgb") == -1) {
                basicsdatumColourLibraryExcelDto.setColorRgb("rgb" + basicsdatumColourLibraryExcelDto.getColorRgb());
            }
         }
            libraryExcelDtoList.add(basicsdatumColourLibraryExcelDto);
        }

        List<BasicsdatumColourLibrary> basicsdatumColourLibraryList = BeanUtil.copyToList(libraryExcelDtoList, BasicsdatumColourLibrary.class);
//        saveOrUpdateBatch(basicsdatumColourLibraryList);
        /*按颜色编码修改*/
        for (BasicsdatumColourLibrary basicsdatumColourLibrary : basicsdatumColourLibraryList) {
            QueryWrapper<BasicsdatumColourLibrary> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("colour_code",basicsdatumColourLibrary.getColourCode());
            this.saveOrUpdate(basicsdatumColourLibrary,queryWrapper);
        }
        return true;
    }

    /**
     * 基础资料-颜色库导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumColourLibraryDeriveExcel(HttpServletResponse response) throws Exception {
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorChroma,C8_ColorType");
        /*色度*/
        Map<String, String> mapColorChroma = dictInfoToMap.get("C8_ColorChroma");
        /*色系*/
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");
        QueryWrapper<BasicsdatumColourLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tbcl.company_code", baseController.getUserCompany());
        List<BasicsdatumColourLibrary> libraryList = baseMapper.selectList(queryWrapper);
        libraryList.forEach(l ->{
            l.setChroma(mapColorChroma.get(l.getChroma()));
            l.setColorType(mapColorType.get(l.getColorType()));
        });
        List<BasicsdatumColourLibraryExcelDto> list = BeanUtil.copyToList( libraryList, BasicsdatumColourLibraryExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumColourLibraryExcelDto.class, "基础资料-颜色库.xlsx", new ExportParams(), response);

    }


    /**
     * 方法描述：新增修改基础资料-颜色库
     *
     * @param addRevampBasicsdatumColourLibraryDto 基础资料-颜色库Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumColourLibrary(AddRevampBasicsdatumColourLibraryDto addRevampBasicsdatumColourLibraryDto) {
        BasicsdatumColourLibrary basicsdatumColourLibrary = new BasicsdatumColourLibrary();
        if (StringUtils.isEmpty(addRevampBasicsdatumColourLibraryDto.getId())) {
            QueryWrapper<BasicsdatumColourLibrary> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("colour_code", addRevampBasicsdatumColourLibraryDto.getColourCode());
            List<BasicsdatumColourLibrary> libraryList = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(libraryList)) {
                throw new OtherException("代码重复");
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumColourLibraryDto, basicsdatumColourLibrary);
            basicsdatumColourLibrary.setCompanyCode(baseController.getUserCompany());
            basicsdatumColourLibrary.insertInit();
            baseMapper.insert(basicsdatumColourLibrary);
        } else {
            /*修改*/
            basicsdatumColourLibrary = baseMapper.selectById(addRevampBasicsdatumColourLibraryDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumColourLibrary)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumColourLibraryDto, basicsdatumColourLibrary);
            basicsdatumColourLibrary.updateInit();
            if ("1".equals(basicsdatumColourLibrary.getScmSendFlag())){
                smpService.color(basicsdatumColourLibrary.getId().split(","));
            }
            baseMapper.updateById(basicsdatumColourLibrary);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-颜色库
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumColourLibrary(String id) {
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
    public Boolean startStopBasicsdatumColourLibrary(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumColourLibrary> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public List<String> getAllColourSpecification() {
        QueryWrapper<BasicsdatumColourLibrary> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.ne("del_flag", BaseGlobal.YES);
        return this.getBaseMapper().getAllColourSpecification(qw);

    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
