/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackSizeConfigReferencesDto;
import com.base.sbc.module.pack.dto.PackSizeDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import com.base.sbc.module.pack.entity.PackSizeDetail;
import com.base.sbc.module.pack.mapper.PackSizeMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.dto.OpenPackSizeDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-尺寸表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 10:14:51
 */
@Service
public class PackSizeServiceImpl extends AbstractPackBaseServiceImpl<PackSizeMapper, PackSize> implements PackSizeService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private PackSizeDetailService packSizeDetailService;

    @Autowired
    private PackSizeConfigService packSizeConfigService;
    @Lazy
    @Autowired
    private StyleColorService styleColorService;
    @Autowired
    private BasicsdatumModelTypeService basicsdatumModelTypeService;

    @Override
    public PageInfo<PackSizeVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackSize> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        if (StrUtil.isBlank(dto.getOrderBy())) {
            qw.orderByAsc("sort");
        }
        Page<PackSize> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackSize> pageInfo = page.toPageInfo();
        PageInfo<PackSizeVo> voPageInfo = CopyUtil.copy(pageInfo, PackSizeVo.class);
        return voPageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackSizeVo saveByDto(PackSizeDto dto) {
        //新增
        if (StrUtil.isBlank(dto.getId()) || StrUtil.contains(dto.getId(), StrUtil.DASHED)) {
            PackSize pageData = BeanUtil.copyProperties(dto, PackSize.class);
            pageData.setId(null);
            save(pageData);
            sizeToHtml(new PackCommonSearchDto(dto.getForeignId(), dto.getPackType()));
            packSizeDetailService.saveSizeDetail(PackUtils.parseSizeDetail(pageData));
            return BeanUtil.copyProperties(pageData, PackSizeVo.class);
        }
        //修改
        else {
            PackSize dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            saveOrUpdateOperaLog(dto, dbData, genOperaLogEntity(dbData, "修改"));
            BeanUtil.copyProperties(dto, dbData);
            boolean b = updateById(dbData);
            sizeToHtml(new PackCommonSearchDto(dto.getForeignId(), dto.getPackType()));
            packSizeDetailService.saveSizeDetail(PackUtils.parseSizeDetail(dbData));
            return BeanUtil.copyProperties(dbData, PackSizeVo.class);
        }
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatchByDto(PackCommonSearchDto commonDto, List<PackSizeDto> dtoList) {
        List<PackSize> packSizes = BeanUtil.copyToList(dtoList, PackSize.class);
        List<PackSizeDetail> allSizeDetail = new ArrayList<>(16);
        if (CollUtil.isNotEmpty(packSizes)) {
            for (PackSize packSize : packSizes) {
                packSize.setForeignId(commonDto.getForeignId());
                packSize.setPackType(commonDto.getPackType());
                allSizeDetail.addAll(PackUtils.parseSizeDetail(packSize));
            }
        }
        QueryWrapper<PackSize> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, commonDto);
        addAndUpdateAndDelList(packSizes, qw, false);
        if (CollUtil.isNotEmpty(allSizeDetail)) {
            packSizeDetailService.saveSizeDetail(allSizeDetail);
        }

        sizeToHtml(commonDto);
        return true;
    }

    @Override
    public void sizeToHtml(PackCommonSearchDto commonDto) {

//        try {
//            PackInfoStatus packInfoStatus = packInfoStatusService.get(commonDto.getForeignId(), commonDto.getPackType());
//            PackInfo packInfo = packInfoService.getById(commonDto.getForeignId());
//
//            SampleDesign sampleDesign = sampleDesignService.getById(packInfo.getForeignId());
//            String productSizes = sampleDesign.getProductSizes();
//            if(StrUtil.isBlank(productSizes)){
//                return ;
//            }
//            List<String> sizeList = StrUtil.split(productSizes, CharUtil.COMMA);
//            QueryWrapper<PackSize> qw = new QueryWrapper<>();
//            PackUtils.commonQw(qw, commonDto);
//            qw.orderByDesc("id");
//            List<PackSize> list = list(qw);
//            boolean washSkippingFlag = StrUtil.equals(packInfoStatus.getWashSkippingFlag(), BaseGlobal.YES);
//
//            Configuration config = new Configuration();
//            config.setDefaultEncoding("UTF-8");
//            config.setTemplateLoader(new ClassTemplateLoader(UtilFreemarker.class, "/"));
//            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//            Template template = config.getTemplate("ftl/sizeTable.html.ftl");
//            Map<String, Object> dataModel = new HashMap<>();
//            dataModel.put("sizeList", sizeList);
//            dataModel.put("colspan", washSkippingFlag?3:2);
//            dataModel.put("washSkippingFlag", washSkippingFlag);
//            List<List<Object>> dataList=new ArrayList<>(16);
//            if(CollUtil.isNotEmpty(list)){
//                for (PackSize packSize : list) {
//                    List<Object> row=new ArrayList<>();
//                    row.add(Opt.ofNullable(packSize.getPartName()).orElse(""));
//                    row.add(Opt.ofNullable(packSize.getMethod()).orElse(""));
//                    JSONObject jsonObject = JSONObject.parseObject(packSize.getStandard());
//                    for (String size : sizeList) {
//                        row.add(MapUtil.getStr(jsonObject,"template"+size,"-"));
//                        row.add(MapUtil.getStr(jsonObject,"garment"+size,"-"));
//                        if(washSkippingFlag){
//                            row.add(MapUtil.getStr(jsonObject,"washing"+size,"-"));
//                        }
//
//                    }
//                    row.add(StrUtil.isBlank(packSize.getCodeError())?"-":StrUtil.DASHED+packSize.getCodeError());
//                    row.add(Opt.ofNullable(packSize.getCodeError()).orElse(""));
//                    dataList.add(row);
//                }
//            }
//            int height=(dataList.size()+1)*24+50+35+30;
//            dataModel.put("dataList", dataList);
//            dataModel.put("height",height+"px");
//            StringWriter writer = new StringWriter();
//            template.process(dataModel, writer);
//            String output = writer.toString();
//            FileUtil.writeString(output,new File("F://sizeTable.html"), Charset.defaultCharset());
//            HtmlImageGenerator gen = new HtmlImageGenerator();
//            gen.loadHtml(output);
//            BufferedImage bufferedImage = gen.getBufferedImage();
//            Image cut = ImgUtil.cut(bufferedImage, new Rectangle(0, 0, bufferedImage.getWidth(), height));
//            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(ImgUtil.toBufferedImage(cut), "size_export_img"+packInfoStatus.getId() + ".png");
////            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(bufferedImage, "size_export_img"+packInfoStatus.getId() + ".png");
//            uploadFileService.delByUrl(packInfoStatus.getSizeExportImg());
//            packInfoStatus.setSizeExportImg(Opt.ofNullable(attachmentVo).map(AttachmentVo::getUrl).orElse(""));
//            packInfoStatusService.updateById(packInfoStatus);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean references(PackSizeConfigReferencesDto dto) {
        packSizeConfigService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType(), BaseGlobal.YES);
        this.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType(), BaseGlobal.NO);
        return true;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        if (StrUtil.equals(overlayFlag, BaseGlobal.YES)) {
            //删除目标数据
            del(targetForeignId, targetPackType);
            packSizeDetailService.del(targetForeignId, targetPackType);
        }

        //复制尺码表
        List<PackSize> sizeList = list(sourceForeignId, sourcePackType);

        if (CollUtil.isNotEmpty(sizeList)) {
            Snowflake snowflake = IdUtil.getSnowflake();
            Map<String, String> newIdMaps = new HashMap<>(16);
            for (PackSize packSize : sizeList) {
                String newId = snowflake.nextIdStr();
                newIdMaps.put(packSize.getId(), newId);
                packSize.setId(newId);
                packSize.setForeignId(targetForeignId);
                packSize.setPackType(targetPackType);
                packSize.setHistoricalData(BaseGlobal.NO);
                packSize.insertInit();
            }
            saveBatch(sizeList);
            //复制尺寸表明细
            List<PackSizeDetail> sizeDetails = packSizeDetailService.list(sourceForeignId, sourcePackType);
            if (CollUtil.isNotEmpty(sizeDetails)) {
                for (PackSizeDetail sizeDetail : sizeDetails) {
                    String newId = snowflake.nextIdStr();
                    sizeDetail.setId(newId);
                    sizeDetail.setPackSizeId(newIdMaps.get(sizeDetail.getPackSizeId()));
                    sizeDetail.setForeignId(targetForeignId);
                    sizeDetail.setPackType(targetPackType);
                }
                packSizeDetailService.saveSizeDetail(sizeDetails);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delByIds(String id) {
        boolean flg = super.delByIds(id);
        packSizeDetailService.delBypackSizeIds(id);
        return flg;
    }

    @Override
    public List<PackSize> list(String foreignId, String packType) {
        QueryWrapper<PackSize> query = new QueryWrapper<>();
        query.eq("foreign_id", foreignId);
        query.eq("pack_type", packType);
        query.orderByAsc("sort");
        return list(query);
    }

    @Override
    String getModeName() {
        return "尺寸表";
    }

// 自定义方法区 不替换的区域【other_end】

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult openSaveBatch(OpenPackSizeDto openPackSizeDto) {
        //查询大货款号是否存在
        List<StyleColor> styleColorList = styleColorService.listByField("style_no", openPackSizeDto.getStyleNo());
        if(CollUtil.isEmpty(styleColorList)) {
            //没有查询到大货款号
            return ApiResult.error("没有找到大货款号："+openPackSizeDto.getStyleNo(),500);
        }
        StyleColor styleColor = styleColorList.get(0);
        String id = styleColor.getId();
        //找到标准资料包id
        List<PackInfo> packInfoList = packInfoService.listByField("style_color_id", id);
        if(CollUtil.isEmpty(packInfoList)) {
            //没有查询到大货款号
            return ApiResult.error("没有找到大货款号："+openPackSizeDto.getStyleNo()+",对应的标准资料包",500);
        }
        String foreignId = packInfoList.get(0).getId();

        //删除历史数据，先查询
        List<PackSize> oldList = list(foreignId, openPackSizeDto.getPackType());
        String oldIds = oldList.stream().map(BaseEntity::getId).collect(Collectors.joining(","));
        delByIds(oldIds);

        //获取尺寸表配置
        PackSizeConfigVo oldConfig = packSizeConfigService.getConfig(foreignId, openPackSizeDto.getPackType());

        //查询号型类型
        List<BasicsdatumModelType> basicsdatumModelTypes = basicsdatumModelTypeService.queryByCode(BaseConstant.DEF_COMPANY_CODE, oldConfig.getSizeRange());
        if(CollUtil.isEmpty(basicsdatumModelTypes)) {
            return ApiResult.error("号型类型没有找到尺码信息",500);
        }
        String productSizes = basicsdatumModelTypes.get(0).getSize();
        List<String> productSizeList = Arrays.asList(productSizes.split(","));

        //校验对方修改的尺码表信息
        PackSizeConfig config = openPackSizeDto.getConfig();
        String activeSizes = config.getActiveSizes();
        List<String> activeSizeList = Arrays.asList(activeSizes.split(","));
        if(!activeSizeList.contains(config.getDefaultSize())){
            return ApiResult.error("尺码表默认尺码不能取消",500);
        }
        for (String activeSize : activeSizeList) {
            if(!productSizeList.contains(activeSize)) {
                return ApiResult.error("尺码表设计款中,不存在尺码："+activeSize,500);
            }
        }

        //修改尺码表数据
        boolean washSkippingFlag = "1".equals(config.getWashSkippingFlag());
        oldConfig.setProductSizes(productSizes);
        oldConfig.setWashSkippingFlag(config.getWashSkippingFlag());
        oldConfig.setSizeRange(config.getSizeRange());
        oldConfig.setSizeRangeName(config.getSizeRangeName());
        oldConfig.setDefaultSize(config.getDefaultSize());
        oldConfig.setActiveSizes(config.getActiveSizes());
        packSizeConfigService.updateById(oldConfig);

        //插入新数据，并校验数据正确性
        List<PackSizeDto> sizeDtos = openPackSizeDto.getSizeDtos();
        for (PackSizeDto sizeDto : sizeDtos) {
            sizeDto.setId(null);
            //1是分割行 不做校验
            if("0".equals(sizeDto.getRowType())){
                //校验尺码
                if(StrUtil.isEmpty(sizeDto.getSize()) || StrUtil.isEmpty(sizeDto.getCodeErrorSetting()) || StrUtil.isEmpty(sizeDto.getStandard())){
                    return ApiResult.error("尺码数据中尺码不能为空",500);
                }
                if(!StrUtil.equals(activeSizes, sizeDto.getSize())){
                    return ApiResult.error("尺码数据中尺码数据和尺码表配置不一致",500);
                }
                //校验档差配置
                String codeErrorSetting = sizeDto.getCodeErrorSetting();
                //校验样板、成衣尺寸
                String standard = sizeDto.getStandard();
                for (String activeSize : activeSizeList) {
                   if(!codeErrorSetting.contains(activeSize)){
                       return ApiResult.error("档差尺码数据和尺码表配置不一致",500);
                   }
                   if(!standard.contains("template"+activeSize) || !standard.contains("garment"+activeSize)){
                       return ApiResult.error("样板尺寸和成衣尺寸的尺码数据和尺码表配置不一致",500);
                   }
                   if(washSkippingFlag){
                       if(!standard.contains("washing"+activeSize)){
                           return ApiResult.error("开启水洗后尺码,水洗后尺码数据和尺码表配置不一致",500);
                       }
                   }
                }
            }
        }

        PackCommonSearchDto commonDto = new PackCommonSearchDto();
        commonDto.setForeignId(foreignId);
        commonDto.setPackType(openPackSizeDto.getPackType());
        saveBatchByDto(commonDto,sizeDtos);

        return ApiResult.success("保存成功");
    }

}

