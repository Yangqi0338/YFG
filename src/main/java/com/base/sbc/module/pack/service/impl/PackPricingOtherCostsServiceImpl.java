/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.business.PackPricingOtherCostsItemType;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.vo.TotalVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.entity.PackPricingOtherCostsGst;
import com.base.sbc.module.pack.mapper.PackPricingOtherCostsMapper;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsGstService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：资料包-核价信息-其他费用 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingOtherCostsService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:18
 */
@Service
public class PackPricingOtherCostsServiceImpl extends AbstractPackBaseServiceImpl<PackPricingOtherCostsMapper, PackPricingOtherCosts> implements PackPricingOtherCostsService {


    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private  PackPricingService packPricingService;


    @Autowired
    @Lazy
    private PackInfoService packInfoService;

    @Autowired
    @Lazy
    private PackBomService packBomService;

    @Autowired
    private PackPricingOtherCostsGstService otherCostsGstService;

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private StyleService styleService;
    @Autowired
    private MinioUtils minioUtils;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto) {
        BaseLambdaQueryWrapper<PackPricingOtherCosts> qw = new BaseLambdaQueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.notNullEq(PackPricingOtherCosts::getCostsItem, dto.getCostsItem());
        Page<PackPricingOtherCosts> page = PageHelper.startPage(dto);
        qw.notEmptyEq(PackPricingOtherCosts::getColorCode, dto.getColorCode());
        list(qw);
        PageInfo<PackPricingOtherCosts> pageInfo = page.toPageInfo();
        return CopyUtil.copy(pageInfo, PackPricingOtherCostsVo.class);
    }


//    @Override
//    public PackPricingOtherCostsVo saveByDto(PackPricingOtherCostsDto dto) {
//        //新增
//        if (CommonUtils.isInitId(dto.getId())) {
//            PackPricingOtherCosts pageData = BeanUtil.copyProperties(dto, PackPricingOtherCosts.class);
//            pageData.setId(null);
//            save(pageData);
//            return BeanUtil.copyProperties(pageData, PackPricingOtherCostsVo.class);
//        }
//        //修改
//        else {
//            PackPricingOtherCosts dbData = getById(dto.getId());
//            if (dbData == null) {
//                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
//            }
//            saveOrUpdateOperaLog(dto, dbData, genOperaLogEntity(dbData, "修改"));
//            BeanUtil.copyProperties(dto, dbData);
//            updateById(dbData);
//            return BeanUtil.copyProperties(dbData, PackPricingOtherCostsVo.class);
//        }
//    }


    @Override
    public boolean delByIds(String id) {
        boolean b = super.delByIds(id);
        otherCostsGstService.remove(new LambdaQueryWrapper<PackPricingOtherCostsGst>().eq(PackPricingOtherCostsGst::getParentId, id));
        return b;
    }

    /**
     * 批量保存他费用
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOtherCosts(List<PackPricingOtherCostsDto> dtoList) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName(getModeName());
        operaLogEntity.setDocumentCodeField("costsType");
        operaLogEntity.setDocumentNameField("costsItem");
        operaLogEntity.setPathField("packType");
        operaLogEntity.setParentIdField("foreignId");

        List<PackPricingOtherCosts> newEntityList = BeanUtil.copyToList(dtoList, PackPricingOtherCosts.class);
        newEntityList.stream().collect(CommonUtils.groupingBy((it)-> StrUtil.isNotBlank(it.getId()))).forEach((exists, list)-> {
            if (exists) {
                operaLogEntity.setType("修改");
                List<PackPricingOtherCosts> dbList = this.listByIds(list.stream().map(PackPricingOtherCosts::getId).collect(Collectors.toList()));
                list.forEach(newEntity-> {
                    PackPricingOtherCosts entity = dbList.stream().filter(it -> it.getId().equals(newEntity.getId()))
                            .findFirst().orElseThrow(() -> new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND));
                    BeanUtil.copyProperties(newEntity, entity);
                });
                updateBatchById(dbList);
                updateBatchOperaLog(dbList, list, operaLogEntity);
            }else {
                operaLogEntity.setType("新增");
                list.forEach(BaseDataEntity::insertInit);
                saveBatch(list);
                saveBatchOperaLog(list, operaLogEntity);
            }
        });

        List<String> foreignIdList = dtoList.stream().map(PackPricingOtherCostsDto::getForeignId).distinct().collect(Collectors.toList());
        List<String> rightForeignIdList = packInfoService.listOneField(new LambdaQueryWrapper<PackInfo>()
                .in(PackInfo::getId,foreignIdList)
                .ne(PackInfo::getDevtType, ProductionType.FOB), PackInfo::getId);
        if (CollUtil.isEmpty(rightForeignIdList)) return;

        newEntityList = newEntityList.stream().filter(it-> rightForeignIdList.contains(it.getForeignId())).collect(Collectors.toList());
        List<PackPricingOtherCostsGst> gstList = otherCostsGstService.list(new LambdaQueryWrapper<PackPricingOtherCostsGst>()
                .in(PackPricingOtherCostsGst::getParentId, newEntityList.stream().map(PackPricingOtherCosts::getId).collect(Collectors.toList()))
        );

        List<PackPricingOtherCostsGst> otherCostsGstList = newEntityList.stream().map(otherCosts -> {
            PackPricingOtherCostsGst otherCostsGst = gstList.stream().filter(it -> it.getParentId().equals(otherCosts.getId())).findFirst().orElseGet(() -> {
                PackPricingOtherCostsGst newOtherCostsGst = new PackPricingOtherCostsGst();
                newOtherCostsGst.setParentId(otherCosts.getId());
                return newOtherCostsGst;
            });
            BeanUtil.copyProperties(otherCosts, otherCostsGst);
            return otherCostsGst;
        }).collect(Collectors.toList());
        otherCostsGstService.saveOrUpdateBatch(otherCostsGstList);

        /*重新计算*/
        packPricingService.calculatePricingJson(dtoList.get(0).getForeignId(),dtoList.get(0).getPackType());
    }

    @Override
    public Map<String, BigDecimal> statistics(PackCommonSearchDto dto) {
        Map<String, BigDecimal> result = new HashMap<>(16);
        BaseLambdaQueryWrapper<PackPricingOtherCosts> qw = new BaseLambdaQueryWrapper<>();
        BaseLambdaQueryWrapper<PackPricingOtherCosts> qw1 = new BaseLambdaQueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        PackUtils.commonQw(qw1, dto);
        // 其他费用
        qw.notEmptyEq(PackPricingOtherCosts::getColorCode, dto.getColorCode());
        qw1.notEmptyEq(PackPricingOtherCosts::getColorCode, dto.getColorCode());
        qw.ne(PackPricingOtherCosts::getCostsItem, PackPricingOtherCostsItemType.OTHER);
        qw1.eq(PackPricingOtherCosts::getCostsItem, PackPricingOtherCostsItemType.OTHER);
        List<TotalVo> costsItemTotalList = getBaseMapper().newCostsItemTotal(qw,qw1);
        if (CollUtil.isNotEmpty(costsItemTotalList)) {
            costsItemTotalList =  costsItemTotalList.stream().filter(c -> !ObjectUtil.isEmpty(c)&& StrUtil.isNotBlank(c.getLabel())).collect(Collectors.toList());
            for (TotalVo total : costsItemTotalList) {
                if(StrUtil.isNotBlank(total.getLabel()) ){
                    result.put(total.getLabel(), total.getTotal());
                }
            }
        }
        //物料费用
        return result;
    }

//    @Override
//    public List<PackPricingOtherCosts> getPriceSumByForeignIds(List<String> foreignIds, String packType) {
//        if (CollectionUtils.isEmpty(foreignIds)) {
//            return Lists.newArrayList();
//        }
//        return super.getBaseMapper().getPriceSumByForeignIds(foreignIds,packType);
//    }

    /**
     * 生成费用明细单
     *
     * @param costsItemList      字典编码 多个用,分割
     * @param foreignId 父级id
     * @param packType
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCostDetail(List<PackPricingOtherCostsItemType> costsItemList, String foreignId, String packType, String productType) {
        /*获取字典值*/
        String dictKey = costsItemList.stream().map(it-> it.getDict(productType)).collect(Collectors.joining(COMMA));
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap(dictKey);
        List<PackPricingOtherCostsDto> list = new ArrayList<>();
        /**/
        if (CollUtil.isEmpty(dictInfoToMap)) return true;
        costsItemList.forEach(costsItem-> {
            Map<String, String> typeMap = dictInfoToMap.getOrDefault(costsItem.getDict(productType), new HashMap<>());

            typeMap.forEach((key,value)-> {
                PackPricingOtherCostsDto packPricingOtherCosts = new PackPricingOtherCostsDto();
                packPricingOtherCosts.setPackType(packType);
                packPricingOtherCosts.setForeignId(foreignId);
                packPricingOtherCosts.setCostsItem(costsItem);
                packPricingOtherCosts.setPrice(BigDecimal.ZERO);
                packPricingOtherCosts.setCostsType(value);
                packPricingOtherCosts.setCostsTypeId(key);
                list.add(packPricingOtherCosts);
            });
        });
        if(CollUtil.isNotEmpty(list)){
            this.batchOtherCosts(list);
        }
        return true;
    }

    /**
     * 其他费用外协加工费-删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean delOtherCosts(String id) {
        PackPricingOtherCosts packPricingOtherCosts = baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(packPricingOtherCosts)){
            throw new OtherException("id错误");
        }
        baseMapper.deleteById(packPricingOtherCosts.getId());
        packPricingService.calculatePricingJson(packPricingOtherCosts.getForeignId(),packPricingOtherCosts.getPackType());
        return true;
    }
    @Override
    public void generateWfgyPdf(OtherCostsPageDto dto, HttpServletResponse response) {
        String foreignId = dto.getForeignId();
        // 先获取当前资料包
        PackInfo packInfo = packInfoService.getById(foreignId);
        Map<String, Object> baseMap = MapUtil.ofEntries(
                MapUtil.entry("date", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)),
                MapUtil.entry("bulkStyleNo", packInfo.getStyleNo()),
                MapUtil.entry("designNo", packInfo.getDesignNo()),
                MapUtil.entry("technicianName", packInfo.getDesignNo()),
                MapUtil.entry("designer", packInfo.getDesignNo())
        );
        String styleColorId = packInfo.getStyleColorId();
        if (StrUtil.isNotBlank(styleColorId)) {
            styleColorService.warnMsg("未找到对应的大货款" + packInfo.getStyleNo());
            StyleColor styleColor = styleColorService.findOne(styleColorId);
            baseMap.put("bandName",styleColor.getBandName());
            baseMap.put("styleColorPic", minioUtils.getObjectUrl(styleColor.getStyleColorPic()));
        }else {
            styleService.warnMsg("未找到对应的设计款" + packInfo.getDesignNo());
            Style style = styleService.findOne(packInfo.getStyleId());
            baseMap.put("bandName",style.getBandName());
            baseMap.put("styleColorPic", minioUtils.getObjectUrl(style.getStylePic()));
        }

        dto.reset2QueryList();
        List<PackPricingOtherCostsVo> list = otherCostsGstService.pageInfo(dto).getList();
        list.forEach(it-> it.setIndex(1));

        dto.setCostsItem(PackPricingOtherCostsItemType.OUTSOURCE_PROCESS);
        list.addAll(pageInfo(dto).getList());
        List<String> costsTypeSize = list.stream().map(PackPricingOtherCostsVo::getCostsType).distinct().collect(Collectors.toList());
        int sheetIndex = (costsTypeSize.size() > 2 ? costsTypeSize.size()-2 : 0);

        List<String> materialCodeList = list.stream().map(PackPricingOtherCostsVo::getMaterialCode).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
        List<PackBom> packBomList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialCodeList)) {
            packBomList.addAll(packBomService.list(new LambdaQueryWrapper<PackBom>()
                    .eq(PackBom::getForeignId, foreignId)
                    .eq(PackBom::getPackType, dto.getPackType())
                    .in(PackBom::getMaterialCode, materialCodeList)
            ));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream templateInputStream = new ByteArrayInputStream(bos.toByteArray());
        //输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateInputStream).build()) {
            InputStream fileInputStream = new DefaultResourceLoader().getResource("excelTemp/ExternalCraftTemplate.xlsx").getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            //模板写到流里
            workbook.write(bos);
            List<String> rightSheetNameList = new ArrayList<>();
            list.stream().collect(CommonUtils.groupingBy(PackPricingOtherCostsVo::getCostsTypeId)).forEach((name,sameNameList)-> {
                rightSheetNameList.add(name);
                PackPricingOtherCostsVo baseOtherCosts = sameNameList.get(0);
                workbook.cloneSheet(sheetIndex, name);
                Map<String, Object> otherCostsMap = MapUtil.ofEntries(
                        MapUtil.entry("factoryName", baseOtherCosts.getFactoryName()),
                        MapUtil.entry("name", name)
                );

                List<PackBom> costsPackBomLList = packBomList.stream().filter(it -> it.getMaterialCode().equals(baseOtherCosts.getMaterialCode())).limit(5).collect(Collectors.toList());

                // 根据物料编号 获取当前packBom里的

                //创建对应的sheet
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetIndex).build();
                excelWriter.fill(baseMap,writeSheet);
                excelWriter.fill(otherCostsMap,writeSheet);
                sameNameList.stream().sorted(Comparator.comparing(PackPricingOtherCostsVo::getIndex)).forEach(it-> {
                    excelWriter.fill(new FillWrapper("data"+it.getIndex(), Collections.singletonList(it)),writeSheet);
                });
                if (CollUtil.isNotEmpty(costsPackBomLList)) {
                    Map<String, List<PackBom>> packBomMap = costsPackBomLList.stream().sorted(Comparator.comparing(PackBom::getSort))
                            .collect(CommonUtils.groupingBy(PackBom::getMaterialName));

                    List<List<String>> packBomParam = new ArrayList<>();
                    packBomMap.forEach((key,value)-> {
                        packBomParam.add(Arrays.asList(key,
                                CommonUtils.get(value,0,PackBom::getColor),
                                CommonUtils.get(value,1,PackBom::getColor),
                                CommonUtils.get(value,2,PackBom::getColor),
                                CommonUtils.get(value,3,PackBom::getColor),
                                CommonUtils.get(value,0,PackBom::getAuxiliaryMaterial),
                                CommonUtils.get(value,1,PackBom::getAuxiliaryMaterial),
                                CommonUtils.get(value,2,PackBom::getAuxiliaryMaterial),
                                CommonUtils.get(value,3,PackBom::getAuxiliaryMaterial)));
                    });
                    excelWriter.write(packBomParam,writeSheet);
                }
            });

            workbook.sheetIterator().forEachRemaining((it)-> {
                if (!rightSheetNameList.contains(it.getSheetName())) {
                    workbook.sheetIterator().remove();
                }
            });
            ExcelUtils.toPdf(new ByteArrayInputStream(out.toByteArray()), response);
        } catch (Exception e) {

        }
    }


    @Override
    String getModeName() {
        return "其他费用";
    }

// 自定义方法区 不替换的区域【other_end】

}
