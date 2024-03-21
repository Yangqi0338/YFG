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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibraryExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.Page;
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
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;

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
    public PageInfo getBasicsdatumColourLibraryList(QueryBasicsdatumColourLibraryDto queryBasicsdatumColourLibraryDto) {
        BaseQueryWrapper<BasicsdatumColourLibrary> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.notEmptyEq("status",queryBasicsdatumColourLibraryDto.getStatus());
        queryWrapper.notEmptyLike("create_name",queryBasicsdatumColourLibraryDto.getCreateName());
        queryWrapper.between("create_date",queryBasicsdatumColourLibraryDto.getCreateDate());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourCode()), "colour_code", queryBasicsdatumColourLibraryDto.getColourCode());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourSpecification()), "colour_specification", queryBasicsdatumColourLibraryDto.getColourSpecification());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColourName()), "colour_name", queryBasicsdatumColourLibraryDto.getColourName());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getLibrary()), "library", queryBasicsdatumColourLibraryDto.getLibrary());
        queryWrapper.eq(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getIsStyle()), "is_style", queryBasicsdatumColourLibraryDto.getIsStyle());
        queryWrapper.eq(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getIsMaterials()), "is_materials", queryBasicsdatumColourLibraryDto.getIsMaterials());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getPantone()), "pantone", queryBasicsdatumColourLibraryDto.getPantone());
        queryWrapper.like(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getColorType()), "color_type", queryBasicsdatumColourLibraryDto.getColorType());
        queryWrapper.notEmptyLike("color_rgb", queryBasicsdatumColourLibraryDto.getColorRgb());
        queryWrapper.notEmptyLike("chroma_name", queryBasicsdatumColourLibraryDto.getChromaName());
        queryWrapper.notEmptyEq("chroma", queryBasicsdatumColourLibraryDto.getChroma());
        queryWrapper.in(!StringUtils.isEmpty(queryBasicsdatumColourLibraryDto.getScmSendFlag()), "scm_send_flag", StringUtils.convertList(queryBasicsdatumColourLibraryDto.getScmSendFlag()));
        if (StringUtils.isNotEmpty(queryBasicsdatumColourLibraryDto.getSearch())) {
            queryWrapper.andLike(queryBasicsdatumColourLibraryDto.getSearch(), "colour_code", "colour_name");
        }

        /*查询基础资料-颜色库数据*/
        queryWrapper.orderByDesc("create_date");
        Page<BasicsdatumColourLibraryVo> objects = PageHelper.startPage(queryBasicsdatumColourLibraryDto);
        List<BasicsdatumColourLibrary> basicsdatumColourLibraries = getBaseMapper().selectList(queryWrapper);
        minioUtils.setObjectUrlToList(objects.toPageInfo().getList(), "picture");
        return objects.toPageInfo();
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
//      过滤掉无编码数据
        list = list.stream().filter(c -> StringUtils.isNotBlank(c.getColourCode())).collect(Collectors.toList());
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
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getChromaName())) {
                for (Map.Entry<String, String> entry : mapColorChroma.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumColourLibraryExcelDto.getChromaName())) {
                        basicsdatumColourLibraryExcelDto.setChroma(key);
                        basicsdatumColourLibraryExcelDto.setChromaName(value);
                        break;
                    }
                }
            }
            /*色系*/
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getColorTypeName())) {
                for (Map.Entry<String, String> entry : mapColorType.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value.equals(basicsdatumColourLibraryExcelDto.getColorTypeName())) {
                        basicsdatumColourLibraryExcelDto.setColorType(key);
                        basicsdatumColourLibraryExcelDto.setColorTypeName(value);
                        break;
                    }
                }
            }
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumColourLibraryExcelDto.getPicture())) {
                File file1 = new File(basicsdatumColourLibraryExcelDto.getPicture());
                /*上传图*/
                AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1), "MaterialOther/ColourLibrary/" + basicsdatumColourLibraryExcelDto.getColourCode() + ".jpg");
                basicsdatumColourLibraryExcelDto.setPicture(CommonUtils.removeQuery(attachmentVo.getUrl()));
            }
            if (StringUtils.isNotBlank(basicsdatumColourLibraryExcelDto.getColorRgb()) && !basicsdatumColourLibraryExcelDto.getColorRgb().contains("rgb")) {
                basicsdatumColourLibraryExcelDto.setColor16(StringUtils.rgbToHex(basicsdatumColourLibraryExcelDto.getColorRgb()));
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
        queryWrapper.eq("company_code", baseController.getUserCompany());
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
        CommonUtils.removeQuery(addRevampBasicsdatumColourLibraryDto, "picture");
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
        /*控制是否下发外部SMP系统开关*/
        Boolean systemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());
        QueryWrapper<BasicsdatumColourLibrary> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(systemSwitch, BasicsdatumColourLibrary::getScmSendFlag, StringUtils.convertList("1,3"))
                .in(BasicsdatumColourLibrary::getId, ids);
        List<BasicsdatumColourLibrary> libraryList = baseMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(libraryList) && systemSwitch) {
            throw new OtherException("存在已下发数据无法删除");
        }
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
    public List<SelectOptionsVo> getAllColourSpecification(String status,String isStyle,String isMaterials) {
        QueryWrapper<BasicsdatumColourLibrary> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(StrUtil.isNotBlank(status), "status", status);
        qw.eq(!StringUtils.isEmpty(isStyle), "is_style", isStyle);
        qw.eq(!StringUtils.isEmpty(isMaterials), "is_materials", isMaterials);
        qw.ne("del_flag", BaseGlobal.YES);
        return this.getBaseMapper().getAllColourSpecification(qw);

    }

    @Override
    public List<BasicsdatumColourLibrary> listByCode(List<String> colourCodeList){
        LambdaQueryWrapper<BasicsdatumColourLibrary> qw = new LambdaQueryWrapper<>();
        qw.eq(BasicsdatumColourLibrary::getCompanyCode, getCompanyCode());
        qw.in(BasicsdatumColourLibrary::getColourCode, colourCodeList);
        return list(qw);
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
