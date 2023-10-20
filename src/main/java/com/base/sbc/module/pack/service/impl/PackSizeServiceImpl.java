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
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackSizeConfigReferencesDto;
import com.base.sbc.module.pack.dto.PackSizeDto;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeDetail;
import com.base.sbc.module.pack.mapper.PackSizeMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        packSizeConfigService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
        this.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
        return true;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        //删除目标数据
        del(targetForeignId, targetPackType);
        packSizeDetailService.del(targetForeignId, targetPackType);

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

}

