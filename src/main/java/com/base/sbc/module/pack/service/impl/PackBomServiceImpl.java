/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.enums.CcmBaseSettingEnum;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.ExecutorContext;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.EasyExcelUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.config.utils.TemplateExcelUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorSelectVo;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryGroup;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import com.base.sbc.module.fabricsummary.service.FabricSummaryGroupService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryPrintLogService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryStyleService;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.orderbook.dto.MaterialUpdateDto;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.pack.dto.BomTemplateSaveDto;
import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import com.base.sbc.module.pack.dto.PackBomColorDto;
import com.base.sbc.module.pack.dto.PackBomDto;
import com.base.sbc.module.pack.dto.PackBomPageSearchDto;
import com.base.sbc.module.pack.dto.PackBomSizeDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackBomMapper;
import com.base.sbc.module.pack.service.PackBomColorService;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import com.base.sbc.module.sample.dto.BomFabricDto;
import com.base.sbc.module.sample.dto.FabricStyleDto;
import com.base.sbc.module.sample.dto.FabricSummaryDTO;
import com.base.sbc.module.sample.dto.FabricSummaryExportExcel;
import com.base.sbc.module.sample.dto.FabricSummaryGroupDto;
import com.base.sbc.module.sample.dto.FabricSummarySaveDTO;
import com.base.sbc.module.sample.dto.FabricSummaryStyleDto;
import com.base.sbc.module.sample.dto.FabricSummaryStyleSaveDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.BomFabricVo;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricStyleUpdateResultVo;
import com.base.sbc.module.sample.vo.FabricStyleVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoExcel;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.base.sbc.module.sample.vo.FabricSummaryVO;
import com.base.sbc.module.sample.vo.MaterialSampleDesignVO;
import com.base.sbc.module.sample.vo.NeedUpdateVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleVo;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;
import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.STYLE_MANY_COLOR;
import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.*;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.vo.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;
import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.STYLE_MANY_COLOR;
import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：资料包-物料清单 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
@Service
public class PackBomServiceImpl extends AbstractPackBaseServiceImpl<PackBomMapper, PackBom> implements PackBomService {

// 自定义方法区 不替换的区域【other_start】

    @Resource
    private PackBomSizeService packBomSizeService;

    @Resource
    private PackBomVersionService packBomVersionService;

    @Resource
    private PackBomColorService packBomColorService;

    @Autowired
    private BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;
    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;
    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private BasicsdatumBomTemplateMaterialMapper basicsdatumBomTemplateMaterialMapper;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Resource
    private StyleService styleService;
    @Resource
    private MinioUtils minioUtils;

    @Resource
    @Lazy
    private SmpService smpService;

    @Resource
    @Lazy
    private HangTagService hangTagService;

    @Resource
    private PackInfoService packInfoService;

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private BaseController baseController;

    @Autowired
    private StylePricingService stylePricingService;

    @Autowired
    private PackPricingService packPricingService;

    @Autowired
    private EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

    @Autowired
    private BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    @Autowired
    @Lazy
    private PackInfoStatusService packInfoStatusService;

    @Autowired
    private FabricSummaryService fabricSummaryService;

    @Autowired
    private FabricSummaryStyleService fabricSummaryStyleService;

    @Lazy
    @Autowired
    private OrderBookDetailService orderBookDetailService;

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private FabricSummaryPrintLogService fabricSummaryPrintLogService;

    @Autowired
    @Lazy
    private BasicsdatumSupplierService basicsdatumSupplierService;

    @Autowired
    private FabricSummaryGroupService fabricSummaryGroupService;

    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Autowired
    private BasicsdatumMaterialColorService materialColorService;

    private final ReentrantLock saveLock = new ReentrantLock();

    @Override
    public PageInfo<PackBomVo> pageInfo(PackBomPageSearchDto dto) {

        BaseQueryWrapper<PackBom> qw = new BaseQueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getBomVersionId()), "bom_version_id", dto.getBomVersionId());
        qw.orderByAsc("sort").orderByAsc("id");
        if (StringUtils.isNotEmpty(dto.getStyleColorCode()) && !StringUtils.equals("all", dto.getStyleColorCode())) {
            List<String> bomIds = packBomColorService.getBomIdByColorCode(dto.getStyleColorCode(), dto.getBomVersionId());
            if (CollUtil.isEmpty(bomIds)) {
                return new PageInfo<>();
            }
            qw.in("id", bomIds);
        }
        qw.notEmptyLike("material_code", dto.getMaterialCode());
        qw.notEmptyEq("unit_use", dto.getUnitUse());
        qw.notEmptyLike("ingredient", dto.getIngredient());
        qw.notEmptyLike("supplier_factory_ingredient", dto.getSupplierFactoryIngredient());
        qw.notEmptyLike("supplier_material_code", dto.getSupplierMaterialCode());
        qw.notEmptyLike("material_name", dto.getMaterialName());
        Page<PackBom> page = PageHelper.startPage(dto);
        List<PackBom> list = list(qw);

        minioUtils.setObjectUrlToList(list, "imageUrl");
        PageInfo<PackBom> pageInfo = page.toPageInfo();
        PageInfo<PackBomVo> voPageInfo = CopyUtil.copy(pageInfo, PackBomVo.class);
        // 查询尺码配置
        List<PackBomVo> pbvs = voPageInfo.getList();
        querySubList(pbvs);
        return voPageInfo;
    }

    @Override
    public BigDecimal sumBomCost(PackBomPageSearchDto dto) {
        QueryWrapper<PackBom> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getBomVersionId()), "bom_version_id", dto.getBomVersionId());
        qw.eq("unusable_flag", BaseGlobal.NO);
        BigDecimal cost = BigDecimal.ZERO;
        List<PackBom> bomList = baseMapper.sumBomCost(qw);
        if (CollUtil.isNotEmpty(bomList)) {
            for (PackBom packBom : bomList) {
                packBom.calculateCost();
                cost = cost.add(packBom.getCost());
            }
        }
        return cost;
    }

    @Override
    public void querySubList(List<PackBomVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> materialCodes = list.stream()
                .filter(x -> StringUtils.equals(x.getDataSource(), "1"))
                .map(PackBomVo::getMaterialCode)
                .collect(Collectors.toList());
        Map<String, BasicsdatumMaterial> sourceMaps = basicsdatumMaterialService.getSourceAndIngredient(materialCodes);
        List<String> bomIds = list.stream().map(PackBomVo::getId).collect(Collectors.toList());
        //配码
        Map<String, List<PackBomSizeVo>> packBomSizeMap = packBomSizeService.getByBomIdsToMap(bomIds);
        // 查询资料包-物料清单-配色
        Map<String, List<PackBomColorVo>> packBomColorMap = packBomColorService.getByBomIdsToMap(bomIds);
        for (PackBomVo pbv : list) {
            pbv.setPackBomSizeList(packBomSizeMap.get(pbv.getId()));
            pbv.setPackBomColorVoList(packBomColorMap.get(pbv.getId()));
            BasicsdatumMaterial basicsdatumMaterial = sourceMaps.get(pbv.getMaterialCode());
            if (basicsdatumMaterial != null) {
                pbv.setSource(basicsdatumMaterial.getSource());
                //成分取最新的
                pbv.setIngredient(basicsdatumMaterial.getIngredient());
            }
            if (StringUtils.isBlank(pbv.getSupplierMaterialCode()) && !Objects.isNull(basicsdatumMaterial)){
                pbv.setSupplierMaterialCode(basicsdatumMaterial.getSupplierFabricCode());
            }
        }
        List<String> ids = list.stream().map(PackBomVo::getSupplierId).collect(Collectors.toList());
        SFunction<BasicsdatumSupplier, String> codeFunc = BasicsdatumSupplier::getSupplierCode;
        SFunction<BasicsdatumSupplier, String> valueFunc = BasicsdatumSupplier::getSupplierAbbreviation;
        Map<String,String> supplierAbbreviationMap = basicsdatumSupplierService.list(new BaseLambdaQueryWrapper<BasicsdatumSupplier>()
                .in(codeFunc, ids).select(codeFunc, valueFunc))
                .stream().collect(Collectors.toMap(codeFunc, valueFunc));
        list.forEach(it-> it.setSupplierAbbreviation(supplierAbbreviationMap.getOrDefault(it.getSupplierId(), null)));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackBomVo saveByDto(PackBomDto dto) {
        CommonUtils.removeQuery(dto, "imageUrl");
        dto.calculateCost();
        PackBom packBom = BeanUtil.copyProperties(dto, PackBom.class);
        /*是否是核价信息修改计控损耗*/
       Boolean b =  StrUtil.equals(dto.getPricingSendFlag(), BaseGlobal.STATUS_CLOSE);
        PackBomVersion version = null;
        if(b){
            version = packBomVersionService.getById(dto.getBomVersionId());
        }else {
            version = packBomVersionService.checkVersion(dto.getBomVersionId());
        }

        // 新增
        if (CommonUtils.isInitId(packBom.getId())) {
            packBom.setId(null);
            PackUtils.setBomVersionInfo(version, packBom);
            PackUtils.bomDefaultVal(packBom);
            packBom.setStageFlag(Opt.ofBlankAble(packBom.getStageFlag()).orElse(packBom.getPackType()));
            save(packBom);
        } else {
            PackBom db = getById(dto.getId());
            if (db == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            saveOrUpdateOperaLog(dto, db, genOperaLogEntity(db, "修改"));
            BeanUtil.copyProperties(dto, db);
            PackUtils.setBomVersionInfo(version, db);
            db.setStageFlag(Opt.ofBlankAble(packBom.getStageFlag()).orElse(packBom.getPackType()));
            db.setBulkUnitUse(dto.getBulkUnitUse());
            db.setDesignUnitUse(dto.getDesignUnitUse());
            BigDecimal totalCost = packPricingService.countTotalPrice(db.getForeignId(),BaseGlobal.STOCK_STATUS_CHECKED,2);
            updateById(db);
            //特殊 判断这几个字段是否修改为空 为空时，设置置空
            if (dto.getLossRate() == null || dto.getDesignUnitUse() == null || dto.getBulkUnitUse() == null) {
                LambdaUpdateWrapper<PackBom> up = new LambdaUpdateWrapper<>();
                up.set(PackBom::getLossRate, dto.getLossRate());
                up.set(PackBom::getDesignUnitUse, dto.getDesignUnitUse());
                up.set(PackBom::getBulkUnitUse, dto.getBulkUnitUse());
                up.eq(PackBom::getId, db.getId());
                update(up);
            }
            packBom = db;

            /*替换物料时 当变更物料后（对应类型参照嘉威的业务配置字典），对用户进行提示
            *
            * 要判断吊牌里面的状态是否都通过
            * */
            if (StrUtil.equals(dto.getReplaceMaterialFlag(), BaseGlobal.YES)) {
                PackInfo packInfo = packInfoService.getById(packBom.getForeignId());
                if(StrUtil.isNotBlank(packInfo.getStyleNo())) {
                    Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("matchRemind");
                    Map<String, String> map = dictInfoToMap.get("matchRemind");
                    if(CollUtil.isNotEmpty(map)){
                        List<String> valuesList = new ArrayList<>(map.values());
                        /*查询字典中的配置*/
                        if(valuesList.contains(dto.getReplaceCollocationName())){
                            /*判断款式定价的状态全部都通过*/
                            HangTag hangTag = hangTagService.getByOne("bulk_style_no", packInfo.getStyleNo());
                            if(ObjectUtils.isNotEmpty(hangTag)){
                                if (hangTag.getStatus() == HangTagStatusEnum.FINISH) {
                                    //发送消息
                                    messageUtils.sendMessage("",hangTag.getCreateId(),packInfo.getStyleNo()+"大货款号，物料已替换请注意查收","/beforeProdSample/bigGoodsDataPackage?id="+packInfo.getId()+"&styleId="+packInfo.getStyleId()+"&style="+packInfo.getStyleNo()+"&packType=packBigGoods","",baseController.getUser());
                                }
                            }
                        }
                    }
                }
            }
            /*核价信息编辑物料清单的可编辑状态直接下发*/
            List<String> stringList = com.base.sbc.config.utils.StringUtils.convertList("1,3");
            if (stringList.contains(db.getScmSendFlag()) && b) {
                /*解锁下发*/
                IdDto idDto = new IdDto();
                idDto.setId(db.getId());
                unlock(idDto);
                /*下发*/
                smpService.bom(db.getId().split(","));
            }
        if(StrUtil.equals(packBom.getScmSendFlag(),BaseGlobal.YES)|| StrUtil.equals(packBom.getScmSendFlag(),BaseGlobal.IN_READY)){
            costUpdate(packBom.getForeignId(),totalCost);
        }
        }
        //保存尺寸表
        List<PackBomSizeDto> packBomSizeDtoList = Optional.ofNullable(dto.getPackBomSizeList()).orElse(new ArrayList<>(2));
        List<PackBomSize> packBomSizeList = BeanUtil.copyToList(packBomSizeDtoList, PackBomSize.class);
        for (PackBomSize packBomSize : packBomSizeList) {
            packBomSize.setBomId(packBom.getId());
        }
        //保存资料包颜色表
        List<PackBomColorDto> packBomColorDtoList = Optional.ofNullable(dto.getPackBomColorDtoList()).orElse(new ArrayList<>(2));
        List<PackBomColor> packBomColorList = BeanUtil.copyToList(packBomColorDtoList, PackBomColor.class);
        for (PackBomColor packBomColor : packBomColorList) {
            packBomColor.setBomId(packBom.getId());
        }
        QueryWrapper sizeQw = new QueryWrapper();
        sizeQw.eq("bom_id", packBom.getId());
        sizeQw.eq("bom_version_id", version.getId());
        packBomSizeService.addAndUpdateAndDelList(packBomSizeList, sizeQw, true);
        PackBomVo packBomVo = BeanUtil.copyProperties(packBom, PackBomVo.class);
        packBomVo.setPackBomSizeList(BeanUtil.copyToList(packBomSizeList, PackBomSizeVo.class));

        QueryWrapper colorQw = new QueryWrapper();
        colorQw.eq("bom_id", packBom.getId());
        colorQw.eq("bom_version_id", version.getId());
        packBomColorService.addAndUpdateAndDelList(packBomColorList, colorQw, true);
        packBomVo.setPackBomColorVoList(BeanUtil.copyToList(packBomColorList, PackBomColorVo.class));
        return packBomVo;
    }




    /**
     * 用于物料修改替换删除时计算成本是否改变
     * 如果改变判断款式定价的状态都通过，发送消息，修改款式定价的状态未为未通过
     *
     * @param packInfoId
     * @param cost
     */
    public void costUpdate(String packInfoId, BigDecimal cost) {
        /*查询款式定价是否通过*/
        /*比较成本价是否有改动*/
        /*核价信息总成本*/
        BigDecimal totalCost = packPricingService.countTotalPrice(packInfoId,BaseGlobal.STOCK_STATUS_CHECKED,2);
        /*判断价格是否相等 如果等于空时代表新增的物料*/
        if( ObjectUtils.isEmpty(cost) || totalCost.compareTo(cost) != 0){
            PackInfo packInfo = packInfoService.getById(packInfoId);
            StylePricing stylePricing = stylePricingService.getByOne("pack_id", packInfoId);
            if (!ObjectUtils.isEmpty(stylePricing)) {
                /*判断款式定价的计控是否确认*/
                if (StrUtil.equals(stylePricing.getControlConfirm(), BaseGlobal.YES)
                ) {
                    //发送消息
                    messageUtils.sendMessage("计控,商企", "", packInfo.getStyleNo() + "大货款号，总成本加改动请注意", "/beforeProdSample/bigGoodsDataPackage?id="+packInfo.getId()+"&styleId="+packInfo.getStyleId()+"&style="+packInfo.getStyleNo()+"&packType=packBigGoods", packInfo.getPlanningSeasonId(), baseController.getUser());
                    stylePricing.setControlConfirm(BaseGlobal.NO);
                    stylePricing.setProductHangtagConfirm(BaseGlobal.NO);
                    stylePricing.setControlHangtagConfirm(BaseGlobal.NO);
                    /*重置状态*/
//                    stylePricingService.updateById(stylePricing);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<MaterialSupplierInfo> updateMaterial(MaterialUpdateDto dto) {
        QueryWrapper<PackBom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bom_version_id", dto.getBomVersionId());
        queryWrapper.eq("del_flag", "0");
        queryWrapper.eq("foreign_id",dto.getForeignId());
        List<PackBom> packBoms = list(queryWrapper);
//        if (CollectionUtil.isEmpty(packBoms)){
//            throw new OtherException("bom物料不存在");
//        }
        Map<String, List<PackBom>> map = packBoms.stream().collect(Collectors.groupingBy(PackBom::getMaterialCode, Collectors.toList()));
        // 检查是否可以更新
        checkUpdateMaterial(dto,map);

        PackInfo packInfo = packInfoService.getById(dto.getForeignId());
        if (null == packInfo){
            throw new OtherException("资料包不存在");
        }
        //获取款式信息
        StyleVo styleVo = styleService.getDetail(packInfo.getForeignId());
        if (Objects.isNull(styleVo)){
            styleVo = new StyleVo();
        }
        //尺码id
        List<String> productSizes = StringUtils.isBlank(styleVo.getProductSizes())? new ArrayList<>() : Arrays.asList(styleVo.getProductSizes().split(","));
        //尺码型号类型
        List<String> sizeIds = StringUtils.isBlank(styleVo.getSizeIds())?  new ArrayList<>() : Arrays.asList(styleVo.getSizeIds().substring(1).split(","));
        for (MaterialSupplierInfo materialSupplierInfo : dto.getMaterialSupplierInfos()) {
            List<String> materialCodes = basicsdatumMaterialService.getMaterialCodeBySupplierInfo(materialSupplierInfo);
            if (CollUtil.isEmpty(materialCodes)){
                throw new OtherException("此供应商未有物料关联："+materialSupplierInfo.getSupplierAbbreviation());
            }
            if (materialCodes.size() > 1){
                throw new OtherException("供应商"+materialSupplierInfo.getSupplierAbbreviation()+"关联了多个物料:"+JSON.toJSONString(materialCodes)+"，请检查是否有脏数据！");
            }
            String materialCode = materialCodes.get(0);
            List<PackBom> packBomList = map.get(materialSupplierInfo.getMaterialCode());
            //已有的物料，不做处理
            if (materialCode.equals(materialSupplierInfo.getMaterialCode())){
                continue;
            }
            //获取物料信息
            BasicsdatumMaterialQueryDto basicsdatumMaterialQueryDto = new BasicsdatumMaterialQueryDto();
            basicsdatumMaterialQueryDto.setMaterialCodeNoLike(materialCode);
            List<BomSelMaterialVo> list = basicsdatumMaterialService.getBomSelMaterialList(basicsdatumMaterialQueryDto).getList();
            if (CollUtil.isEmpty(list)){
                log.error("updateMaterial 物料不存在："+ JSON.toJSONString(basicsdatumMaterialQueryDto));
                throw new OtherException("物料不存在");
            }
            PackBomDto packBomDto = new PackBomDto();
            BeanUtil.copyProperties(list.get(0), packBomDto);
            packBomDto.setBomVersionId(dto.getBomVersionId());
            //补充规格信息
            fullPackBomDto(packBomDto, sizeIds, productSizes,list.get(0));
            //是否替换
            if (StringUtils.isNotBlank(materialSupplierInfo.getMaterialCode())){
                UpdateWrapper<PackBom> uw = new UpdateWrapper<>();
                uw.in("id", packBomList.get(0).getId());
                uw.set("del_flag", "1");
                update(uw);
            }
            materialSupplierInfo.setMaterialCode(materialCode);
            saveBatchByDto(dto.getBomVersionId(),"0",Lists.newArrayList(packBomDto));
        }
        return dto.getMaterialSupplierInfos();
    }

    /**
     * 检查更新物料
     * @param dto
     * @param map
     */
    private void checkUpdateMaterial(MaterialUpdateDto dto, Map<String, List<PackBom>> map) {

        PackInfoStatus packDesignStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_DESIGN);
        if (ObjectUtils.isNotEmpty(packDesignStatus) && BasicNumber.ONE.getNumber().equals(packDesignStatus.getBomStatus())){
            throw new OtherException("物料已转大货，不允许更新！");
        }

        //检查需要替换的物料编号是否在bom中
        if (StringUtils.isNotBlank(dto.getMaterialCode())){

            for (MaterialSupplierInfo materialSupplierInfo : dto.getMaterialSupplierInfos()) {
                List<PackBom> packBoms1 = map.get(materialSupplierInfo.getMaterialCode());
                if (CollectionUtil.isEmpty(packBoms1)){
                    throw new OtherException("需要替换的:"+materialSupplierInfo.getMaterialCode()+"物料编号在bom中未有关联！");
                }
                List<PackBom> packBoms = packBoms1.stream().filter(item -> !"1".equals(item.getScmSendFlag())).collect(Collectors.toList());
                if (CollUtil.isEmpty(packBoms)){
                    throw new OtherException("物料："+materialSupplierInfo.getMaterialCode()+"已发送，暂不允许替换");
                }
                map.put(materialSupplierInfo.getMaterialCode(),packBoms);

            }

        }

    }


    private void fullPackBomDto(PackBomDto dto, List<String> sizeRangeSizeIds, List<String> sizeRangeSizes, BomSelMaterialVo bomSelMaterialVo) {
        if (CollUtil.isEmpty(sizeRangeSizeIds) || CollUtil.isEmpty(sizeRangeSizes)){
            return;
        }
        if (sizeRangeSizeIds.size() != sizeRangeSizes.size()){
            throw new RuntimeException("尺码信息异常");
        }
        List<PackBomSizeDto> packBomSizeList = Lists.newArrayList();
        for (int i = 0; i < sizeRangeSizeIds.size(); i++) {
            PackBomSizeDto packBomSizeDto = new PackBomSizeDto();
            packBomSizeDto.setSize(sizeRangeSizes.get(i));
            packBomSizeDto.setSizeId(sizeRangeSizeIds.get(i));
            packBomSizeDto.setWidth(bomSelMaterialVo.getTranslate());
            packBomSizeDto.setWidthCode(bomSelMaterialVo.getTranslateCode());
            packBomSizeList.add(packBomSizeDto);
        }
        dto.setPackBomSizeList(packBomSizeList);
    }

    @Override
    public PageInfo<BomFabricVo> bomFabricList(BomFabricDto bomFabricDto, boolean isPictureShow ) {
        long l1 = System.currentTimeMillis();
        Page<String> page = PageHelper.startPage(bomFabricDto);
        QueryWrapper<Object> qw = new QueryWrapper();
        qw.likeLeft(StringUtils.isNotBlank(bomFabricDto.getMaterialCodeName()), "pb.material_code_name", bomFabricDto.getMaterialCodeName());
        qw.likeLeft(StringUtils.isNotBlank(bomFabricDto.getSupplierMaterialCode()), "pb.supplier_material_code", bomFabricDto.getMaterialCodeName());
        baseMapper.bomFabricMaterialCode(bomFabricDto, qw);
        long l2 = System.currentTimeMillis();
        System.out.println("==========l2-l1=================="+(l2-l1));
        if (CollUtil.isEmpty(page.getResult())){
            return new PageInfo();
        }

        QueryWrapper tb = new QueryWrapper();
        tb.in("tb.material_code",page.getResult());
        List<BomFabricVo> bomFabricVos = baseMapper.bomFabricDetailsList(tb);
        long l3 = System.currentTimeMillis();
        System.out.println("==========l3-l2=================="+(l3-l2));
        PageInfo<BomFabricVo> pageList = new PageInfo<>();
        BeanUtil.copyProperties(page.toPageInfo(),pageList);
        pageList.setList(bomFabricVos);
        if (isPictureShow){
            stylePicUtils.setStylePic(pageList.getList(), "imageUrl");
        }
        long l4 = System.currentTimeMillis();
        System.out.println("==========l4-l1=================="+(l4-l1));
        return pageList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveFabricSummary(FabricSummarySaveDTO feedSummarySaveDTO) {

        List<FabricSummarySaveDTO.FabricInfo> fabricInfos = feedSummarySaveDTO.getFabricInfos();
        if (CollUtil.isEmpty(fabricInfos)){
            return true;
        }
        BomFabricDto bomFabricDto = new BomFabricDto();
        bomFabricDto.setCompanyCode(feedSummarySaveDTO.getCompanyCode());
        bomFabricDto.setMaterialCodes(fabricInfos.stream().map(FabricSummarySaveDTO.FabricInfo::getMaterialCode).collect(Collectors.toList()));

        List<BomFabricVo> bomFabricVos = this.bomFabricList(bomFabricDto,false).getList();
        if (CollUtil.isEmpty(bomFabricVos)){
            return true;
        }

        List<FabricSummary> summaryList = Lists.newArrayList();
        Map<String, String> fabricInfoMap = fabricInfos.stream().filter(item ->StringUtils.isNotBlank(item.getWidthName())).collect(Collectors.toList())
                .stream().collect(Collectors.toMap(FabricSummarySaveDTO.FabricInfo::getMaterialCode, FabricSummarySaveDTO.FabricInfo::getWidthName));

        for (BomFabricVo bomFabricVo : bomFabricVos) {
            FabricSummary fabricSummary = new FabricSummary();
            BeanUtil.copyProperties(bomFabricVo,fabricSummary);
            //获取物料颜色
            List<BasicsdatumMaterialColorSelectVo> materialCodes = basicsdatumMaterialService.getMaterialCodes(bomFabricVo.getMaterialCode());
            if (CollUtil.isNotEmpty(materialCodes)){

                StringBuilder materialColor = new StringBuilder();
                materialCodes.forEach(item ->{
                    if (StringUtils.isNotBlank(item.getColor())){
                        materialColor.append(item.getColor()).append(",");
                    }
                });
                fabricSummary.setMaterialColor(materialColor.toString());
            }
            if (StringUtils.isNotBlank(bomFabricVo.getSupplierId())){
                List<BasicsdatumSupplier> basicsdatumSuppliers = basicsdatumSupplierService.getBySupplierId(bomFabricVo.getSupplierId());
                fabricSummary.setSupplierAbbreviation(CollUtil.isEmpty(basicsdatumSuppliers) ? "" : basicsdatumSuppliers.get(0).getSupplierAbbreviation());
            }
            fabricSummary.setGroupId(feedSummarySaveDTO.getGroupId());
            fabricSummary.setWidthName(fabricInfoMap.get(bomFabricVo.getMaterialCode()));
            fabricSummary.setId( new IdGen().nextIdStr());
            fabricSummary.setFabricSummaryCode("ML"+System.currentTimeMillis());
            fabricSummary.insertInit();
            summaryList.add(fabricSummary);
            saveOrUpdateOperaLog(feedSummarySaveDTO, fabricSummary, genOperaLogEntity(fabricSummary, "新增"));
        }
        return fabricSummaryService.saveBatch(summaryList);
    }
    @Override
    public PageInfo<FabricSummaryInfoVo> fabricSummaryListV2(FabricSummaryV2Dto dto){
        return fabricSummaryService.fabricSummaryInfoVoList(dto);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public FabricStyleUpdateResultVo updateFabricSummary(FabricSummaryV2Dto dto) {
        saveLock.lock();
        try{
            FabricSummary fabricSummary = fabricSummaryService.getById(dto.getId());
            if (null == fabricSummary){
                throw new OtherException("数据不存在！");
            }
            if (!fabricSummary.getFabricSummaryVersion().equals(dto.getFabricSummaryVersion())){
                throw new OtherException("当前版本异常，请刷新页面后再提交");
            }
            UpdateWrapper<FabricSummary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(FabricSummary::getId,fabricSummary.getId());
            updateWrapper.lambda().set(FabricSummary::getEnquiryCode,dto.getEnquiryCode());
            updateWrapper.lambda().set(FabricSummary::getYearSuffix,dto.getYearSuffix());

            updateWrapper.lambda().set(FabricSummary::getProductionDay,dto.getProductionDay());
            updateWrapper.lambda().set(FabricSummary::getFittingResult,dto.getFittingResult());
            updateWrapper.lambda().set(FabricSummary::getWidthName,dto.getWidthName());
            updateWrapper.lambda().set(FabricSummary::getPhysicochemistryDetectionResult,dto.getPhysicochemistryDetectionResult());

            Integer fabricSummaryVersion = fabricSummary.getFabricSummaryVersion()+1;
            updateWrapper.lambda().set(FabricSummary::getFabricSummaryVersion,fabricSummaryVersion);
            fabricSummary.updateInit();
            updateWrapper.lambda().set(FabricSummary::getUpdateDate,fabricSummary.getUpdateDate());
            updateWrapper.lambda().set(FabricSummary::getUpdateId,fabricSummary.getUpdateId());
            updateWrapper.lambda().set(FabricSummary::getUpdateName,fabricSummary.getUpdateName());
            saveOrUpdateOperaLog(dto, fabricSummary, genOperaLogEntity(fabricSummary, "更新"));
            FabricStyleUpdateResultVo result = new FabricStyleUpdateResultVo();
            result.setId(fabricSummary.getId());
            boolean b = fabricSummaryService.update(updateWrapper);
            result.setResult(b);
            if (b){
                result.setFabricSummaryVersion(fabricSummaryVersion);
            }
            return result;
        }finally{
            saveLock.unlock();
        }

    }

    @Override
    public PageInfo<FabricStyleVo> fabricStyleList(FabricStyleDto dto) {
        FabricSummary fabricSummary = fabricSummaryService.getById(dto.getFabricSummaryId());
        if (null == fabricSummary){
            throw new OtherException("汇总主题不存在");
        }
        dto.setMaterialCode(fabricSummary.getMaterialCode());
        Page<FabricStyleVo> page = PageHelper.startPage(dto);

        QueryWrapper qw = new QueryWrapper();
       if (StringUtils.isNotBlank(dto.getPlanningSeasonId())){
           qw.eq("sc.planning_season_id",dto.getPlanningSeasonId());
       }
        if (StringUtils.isNotBlank(dto.getStyleNo())){
            qw.eq("sc.style_no",dto.getStyleNo());
        }
        if (null != dto.getStartTime() && null != dto.getEndTime()){
            qw.between("sc.create_time",dto.getStartTime(), dto.getEndTime());
        };
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.fabric_summary.getK(), "sc.");
        baseMapper.fabricStyleList(dto, qw);
        PageInfo<FabricStyleVo> pageInfo = page.toPageInfo();
        pageInfo.getList().forEach(item -> {
            item.setMaterialCode(fabricSummary.getMaterialCode());
        });
        stylePicUtils.setStylePic(pageInfo.getList(), "stylePic");
        stylePicUtils.setStylePic(pageInfo.getList(), "styleColorPic");
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveFabricSummaryStyle(FabricSummaryStyleSaveDto dto) {
        if (CollUtil.isEmpty(dto.getStyleNos())){
            return true;
        }
        FabricSummary fabricSummary = fabricSummaryService.getById(dto.getFabricSummaryId());
        if (null == fabricSummary){
            throw new OtherException("汇总主题不存在");
        }
        FabricStyleDto fabricStyleDto = new FabricStyleDto();
        fabricStyleDto.setCompanyCode(getCompanyCode());
        fabricStyleDto.setMaterialCode(fabricSummary.getMaterialCode());
        QueryWrapper qw = new QueryWrapper();
        qw.in("sc.style_no", dto.getStyleNos());
        List<FabricStyleVo> fabricStyleVos = baseMapper.fabricStyleList(fabricStyleDto, qw);
        List<FabricStyleVo> voList = fabricStyleVos.stream().filter(item -> Constants.ONE_STR.equals(item.getChoiceFlag())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(voList)){
            throw new OtherException(voList.get(0).getStyleNo()+"的款式已经添加，请勿重复添加！");
        }
        List<FabricSummaryStyle> list = Lists.newArrayList();
        for (FabricStyleVo fabricStyleVo : fabricStyleVos) {

            FabricSummaryStyle fabricSummaryStyle = new FabricSummaryStyle();
            BeanUtil.copyProperties(fabricStyleVo,fabricSummaryStyle);
            fabricSummaryStyle.setGroupId(fabricSummary.getGroupId());
            fabricSummaryStyle.setFabricSummaryId(dto.getFabricSummaryId());
            fabricSummaryStyle.setId(new IdGen().nextIdStr());
            fabricSummaryStyle.insertInit();
            //补充供应商色号信息
            fullSupplierColorCode(fabricSummary,fabricSummaryStyle);
            //补充物料的一些信息
            fullMaterialBomInfo(fabricSummary,fabricSummaryStyle);
            // 总投产
            OrderBookDetailQueryDto orderBookDetailQueryDto = new OrderBookDetailQueryDto();
            orderBookDetailQueryDto.setBulkStyleNoFull(fabricSummaryStyle.getStyleNo());
            PageInfo<OrderBookDetailVo> orderBookDetailVoPageInfo = orderBookDetailService.queryPage(orderBookDetailQueryDto);
            if(CollUtil.isNotEmpty(orderBookDetailVoPageInfo.getList())){
                List<String> totalProductions = orderBookDetailVoPageInfo.getList().stream().map(OrderBookDetailVo::getTotalProduction).collect(Collectors.toList());
                fabricSummaryStyle.setTotalProduction(String.valueOf(totalProductions.stream().filter(StringUtils::isNotBlank).mapToDouble(Double::parseDouble).sum()));
            }
            list.add(fabricSummaryStyle);
            saveOrUpdateOperaLog(dto, fabricSummaryStyle, genOperaLogEntity(fabricSummaryStyle, "新增"));
        }
        return fabricSummaryStyleService.saveBatch(list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public FabricStyleUpdateResultVo updateFabricSummaryStyle(FabricSummaryStyleDto fabricSummaryStyleDto) {
        saveLock.lock();
        try{
            FabricSummaryStyle fabricSummaryStyle = fabricSummaryStyleService.getById(fabricSummaryStyleDto.getId());
            if (null == fabricSummaryStyle){
                throw new OtherException("款式不存在");
            }
            if (!fabricSummaryStyle.getFabricSummaryStyleVersion().equals(fabricSummaryStyleDto.getFabricSummaryStyleVersion())){
                throw new OtherException("当前版本异常，请刷新页面后再提交");
            }
            UpdateWrapper<FabricSummaryStyle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(FabricSummaryStyle::getId,fabricSummaryStyle.getId());

            Integer fabricSummaryStyleVersion = fabricSummaryStyleDto.getFabricSummaryStyleVersion()+1;
            updateWrapper.lambda().set(FabricSummaryStyle::getFabricSummaryStyleVersion, fabricSummaryStyleVersion);
            updateWrapper.lambda().set(FabricSummaryStyle::getSort, fabricSummaryStyleDto.getSort());
            updateWrapper.lambda().set(FabricSummaryStyle::getTotalProduction, fabricSummaryStyleDto.getTotalProduction());
            updateWrapper.lambda().set(FabricSummaryStyle::getColorCrash, fabricSummaryStyleDto.getColorCrash());
            updateWrapper.lambda().set(FabricSummaryStyle::getUnitUse, fabricSummaryStyleDto.getUnitUse());
            updateWrapper.lambda().set(FabricSummaryStyle::getRemarks, fabricSummaryStyleDto.getRemarks());
            fabricSummaryStyle.updateInit();
            updateWrapper.lambda().set(FabricSummaryStyle::getUpdateDate,fabricSummaryStyle.getUpdateDate());
            updateWrapper.lambda().set(FabricSummaryStyle::getUpdateId,fabricSummaryStyle.getUpdateId());
            updateWrapper.lambda().set(FabricSummaryStyle::getUpdateName,fabricSummaryStyle.getUpdateName());
            saveOrUpdateOperaLog(fabricSummaryStyleDto, fabricSummaryStyle, genOperaLogEntity(fabricSummaryStyle, "修改"));
            boolean b = fabricSummaryStyleService.update(updateWrapper);
            FabricStyleUpdateResultVo result =  new FabricStyleUpdateResultVo();
            result.setId(fabricSummaryStyle.getId());
            result.setResult(b);
            if (b){
                result.setFabricSummaryStyleVersion(fabricSummaryStyleVersion);
            }

            return result;
        } finally {
            saveLock.unlock();
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteFabricSummaryStyle(String id) {
        if (StringUtils.isEmpty(id)){
            return true;
        }
        UpdateWrapper<FabricSummaryStyle> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        wrapper.set("del_flag", "1");
        return fabricSummaryStyleService.update(wrapper);
    }

    @Override
    public void fabricSummaryExcel( HttpServletResponse response, FabricSummaryV2Dto dto) {
        if (StringUtils.isEmpty(dto.getId())){
            throw new OtherException("id不能为空");
        }
        PageInfo<FabricSummaryInfoVo> pageInfo = this.fabricSummaryListV2(dto);

        if (CollUtil.isEmpty(pageInfo.getList())){
            throw new OtherException("没有数据");
        }
        //物料信息
        Map<String, Object> materialMap = getMaterialMap(pageInfo.getList().get(0));
        //截取款式的信息
        Map<Integer, List<FabricSummaryInfoVo>> integerListMap = splitListIntoArrays(pageInfo.getList(), 4);
        try{
            //获取模板流
            ByteArrayOutputStream bos = EasyExcelUtils.templateSheetCreate("excelTemp/fabricSummaryTemplate.xlsx", integerListMap.size());
            InputStream templateInputStream = new ByteArrayInputStream(bos.toByteArray());
            //输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ExcelWriter excelWriter =
                    EasyExcel.write(out)
                            .withTemplate(templateInputStream)
                            .build();
            //填充模板数据
            for (int i = 0; i < integerListMap.size(); i++) {
                //创建对应的sheet
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                excelWriter.fill(materialMap,writeSheet);
                List<FabricSummaryInfoVo> fabricSummaryStyleList =  integerListMap.get(i);
                //款式信息 填充
                for (int i1 = 0; i1 < fabricSummaryStyleList.size(); i1++) {
                    FabricSummaryInfoVo fabricSummaryStyle = fabricSummaryStyleList.get(i1);
                    //填充字段信息
                    excelWriter.fill(new FillWrapper("data"+i1, getStyleMap(fabricSummaryStyle)),writeSheet);
                    //填充图片信息
                    List<Map<String, Object>> imgMap = getImgMap(fabricSummaryStyle);
                    if (CollUtil.isNotEmpty(imgMap)){
                        excelWriter.fill(new FillWrapper("img"+i1, imgMap),writeSheet);
                    }

                }
            }
            excelWriter.finish();
            //打印日志
            FabricSummaryPrintLog fabricSummaryPrintLog = new FabricSummaryPrintLog();
            fabricSummaryPrintLog.insertInit();
            fabricSummaryPrintLog.setFabricSummaryId(dto.getId());
            fabricSummaryPrintLogService.save(fabricSummaryPrintLog);
            //下载
            EasyExcelUtils.downLoadExcel("面料汇总.xlsx",out,response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteFabricSummary(String idList) {
        if (StringUtils.isEmpty(idList)){
            return true;
        }
        List<String> ids = Arrays.asList(idList.split(","));
        if (CollUtil.isEmpty(ids)){
            return true;
        }
        UpdateWrapper<FabricSummary> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",ids);
        updateWrapper.set("del_flag",'1');
        fabricSummaryService.update(updateWrapper);
        UpdateWrapper<FabricSummaryStyle> updateWrapperStyle = new UpdateWrapper<>();
        updateWrapperStyle.in("fabric_summary_id",ids);
        updateWrapperStyle.set("del_flag",'1');
        fabricSummaryStyleService.update(updateWrapperStyle);
        return true;
    }

    @Override
    public NeedUpdateVo ifNeedUpdate(String id) {
        FabricSummary fabricSummary = fabricSummaryService.getById(id);
        if (fabricSummary == null){
           throw new OtherException("物料不存在");
        }
        String materialCode = fabricSummary.getMaterialCode();
        NeedUpdateVo needUpdateVo = new NeedUpdateVo();
        needUpdateVo.setStatus("0");
        //查看款式是否还在使用
        if (!materialBomExist(materialCode)){
            needUpdateVo.setStatus("2");
            return needUpdateVo;
        }
        //检测面料供应商是否改变
        if (checkSupplier(fabricSummary,false)){
            needUpdateVo.setStatus("1");
            return needUpdateVo;
        }
        if (!checkStyleBomExist(fabricSummary,false)){
            needUpdateVo.setStatus("1");
            return needUpdateVo;
        }
        return needUpdateVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean fabricSummarySync(String id) {
        FabricSummary fabricSummary = fabricSummaryService.getById(id);
        if (fabricSummary == null){
            throw new OtherException("物料不存在");
        }
        String materialCode = fabricSummary.getMaterialCode();
        if (!materialBomExist(materialCode)){
            throw new OtherException("该物料无款式使用，请删除该物料");
        }
        checkSupplier(fabricSummary,true);
        checkStyleBomExist(fabricSummary,true);
        return true;
    }

    @Override
    public PageInfo<FabricStyleGroupVo> fabricSummaryGroup(FabricSummaryGroupDto dto) {
        Page<FabricStyleGroupVo> page = PageHelper.startPage(dto);
        QueryWrapper<FabricSummaryGroup> qw = new QueryWrapper<>();
        qw.lambda().eq(FabricSummaryGroup::getCompanyCode,companyUserInfo.get().getCompanyCode());
        qw.lambda().likeLeft(StringUtils.isNotBlank(dto.getGroupName()), FabricSummaryGroup::getGroupName, dto.getGroupName());
        qw.lambda().likeLeft(StringUtils.isNotBlank(dto.getCreateName()), FabricSummaryGroup::getCreateName, dto.getCreateName());
        if (null != dto.getStartDate() && null != dto.getEndDate()){
            qw.lambda().between(FabricSummaryGroup::getCreateDate,dto.getStartDate(),dto.getEndDate());
        }
        fabricSummaryGroupService.list(qw);
        return page.toPageInfo();
    }

    @Override
    public void fabricSummaryExcelExport(HttpServletResponse response, FabricSummaryV2Dto dto) {
        PageInfo<FabricSummaryInfoVo> infoVoPageInfo = fabricSummaryListV2(dto);
        List<FabricSummaryInfoVo> infoList = infoVoPageInfo.getList();
        List<FabricSummaryInfoExcel>  list = BeanUtil.copyToList(infoList, FabricSummaryInfoExcel.class);
        list.add(list.get(2));
        list.add(list.get(2));
        try {
            if (StrUtil.equals(dto.getImgFlag(), BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(infoList) && infoList.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (FabricSummaryInfoExcel fabricSummaryInfoExcel : list) {
                    ExecutorContext.imageExecutor.submit(() -> {
                        try {
                            final String stylePic = fabricSummaryInfoExcel.getStylePic();
                            final String imageUrl = fabricSummaryInfoExcel.getImageUrl();
                            fabricSummaryInfoExcel.setStylePic1(HttpUtil.downloadBytes(stylePic));
                            fabricSummaryInfoExcel.setImageUrl1(HttpUtil.downloadBytes(imageUrl));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list, FabricSummaryInfoExcel.class, "面料详单.xlsx", new ExportParams("面料详单", "面料详单", ExcelType.HSSF), response);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean designAffirm(PackBomVo dto) {
        if (StringUtils.isBlank(dto.getId())){
            return true;
        }
        if (StringUtils.isBlank(dto.getDesignVerify())){
            throw new OtherException("确认状态不能为空");
        }
        PackBom packBom = getById(dto.getId());
        if (dto.getDesignVerify().equals(packBom.getDesignVerify())){
            return true;
        }
        UpdateWrapper<PackBom> uw = new UpdateWrapper<>();
        uw.lambda().eq(PackBom::getId, dto.getId());
        uw.lambda().set(PackBom::getDesignVerify, dto.getDesignVerify());
        uw.lambda().set(PackBom::getDesignVerifyDate, BaseGlobal.YES.equals(dto.getDesignVerify()) ? new Date() : null);
        return update(uw);
    }

    /**
     * 物料bom是否还在使用
     * @param materialCode
     * @return
     */
    private boolean materialBomExist(String materialCode) {
        //bom中物料的使用次数
        QueryWrapper qw = new QueryWrapper();
        qw.eq("material_code",materialCode);
        int  materialBomCount  = baseMapper.materialBomCount(qw);
        return materialBomCount > 0;
    }

    /**
     * 检测面料供应商是否改变
     * @param fabricSummary
     * @param isUpdate 是否更新
     * @return
     */
    private boolean checkSupplier(FabricSummary fabricSummary, boolean isUpdate) {
        QueryWrapper<BasicsdatumMaterialPrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_code",fabricSummary.getMaterialCode());
        queryWrapper.eq("select_flag","1");
        queryWrapper.eq("del_flag","0");
        List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = basicsdatumMaterialPriceService.list(queryWrapper);
        if (CollUtil.isNotEmpty(basicsdatumMaterialPrices)){
            BasicsdatumMaterialPrice basicsdatumMaterialPrice = basicsdatumMaterialPrices.get(0);
            if (!fabricSummary.getSupplierId().equals(basicsdatumMaterialPrice.getSupplierId()) || !fabricSummary.getSupplierFabricCode().equals(basicsdatumMaterialPrice.getSupplierMaterialCode())){
                //面料供应商改变
                if (!isUpdate){
                    return true;
                }
                BasicsdatumMaterial materialByCode = basicsdatumMaterialService.getMaterialByCode(fabricSummary.getMaterialCode());
                fabricSummary.setSupplierId(basicsdatumMaterialPrice.getSupplierId());
                fabricSummary.setSupplierFabricCode(basicsdatumMaterialPrice.getSupplierMaterialCode());
                fabricSummary.setSupplierName(basicsdatumMaterialPrice.getSupplierName());
                fabricSummary.setSupplierQuotationPrice(basicsdatumMaterialPrice.getQuotationPrice());
                fabricSummary.setSupplierColorSay(materialByCode.getSupplierColorSay());
                fabricSummary.setIngredient(materialByCode.getIngredient());
                fabricSummaryService.updateById(fabricSummary);
            }
            //检测 含税价格是否改变
            if (fabricSummary.getSupplierQuotationPrice().compareTo(basicsdatumMaterialPrice.getQuotationPrice()) != 0){
                if (!isUpdate){
                    return true;
                }
                fabricSummary.setSupplierQuotationPrice(basicsdatumMaterialPrice.getQuotationPrice());
                fabricSummaryService.updateById(fabricSummary);
            }
        }
        return false;
    }

    /**
     * 检测款式是否使用了该物料
     * @param fabricSummary
     * @param isUpdate
     * @return
     */
    private boolean checkStyleBomExist(FabricSummary fabricSummary, boolean isUpdate) {
        QueryWrapper<FabricSummaryStyle> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("fabric_summary_id",fabricSummary.getId());
        objectQueryWrapper.eq("del_flag","0");
        List<FabricSummaryStyle> fabricSummaryStyles = fabricSummaryStyleService.list(objectQueryWrapper);
        if (CollUtil.isEmpty(fabricSummaryStyles)){
            return true;
        }
        for (FabricSummaryStyle fabricSummaryStyle : fabricSummaryStyles) {
            //检测该款式是否还用了此物料
            QueryWrapper  queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("pb.material_code",fabricSummary.getMaterialCode());
            int styleUseMaterialCount = baseMapper.materialBomCount(queryWrapper1);
            if (styleUseMaterialCount <= 0){
                if (!isUpdate){
                    return false;
                }
                UpdateWrapper objectUpdateWrapper = new UpdateWrapper<>();
                objectUpdateWrapper.eq("id",fabricSummaryStyle.getId());
                objectUpdateWrapper.set("del_flag","1");
                fabricSummaryStyleService.update(objectQueryWrapper);

            }
        }
        return true;
    }

    /**
     * 填充图片
     * @param fabricSummaryStyle
     * @return
     */
    private List<Map<String, Object>> getImgMap(FabricSummaryInfoVo fabricSummaryStyle){
        if (StringUtils.isEmpty(fabricSummaryStyle.getStylePic())){
            return null;
        }
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Map<String, Object> objectMap = Maps.newHashMap();
            WriteCellData<Void> voidWriteCellData = null;
        try {
            voidWriteCellData = TemplateExcelUtils.imageCells(HttpUtil.downloadBytes(fabricSummaryStyle.getStylePic()), 200.0, 100.0, 0.6,1.9);
       } catch (IOException e) {
            log.error("getImgMap error",e);
        }
        objectMap.put(FabricSummaryExportExcel.stylePic,voidWriteCellData);
        mapList.add(objectMap);
        return mapList;
    }

    /**
     * 填充字段的一些信息
     * @param fabricSummaryStyle
     * @return
     */
    private List<Map<String, Object>> getStyleMap(FabricSummaryInfoVo fabricSummaryStyle) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Map<String, Object> objectMap = Maps.newHashMap();
        objectMap.put(FabricSummaryExportExcel.partName,fabricSummaryStyle.getPartName());
        objectMap.put(FabricSummaryExportExcel.bandName,fabricSummaryStyle.getBandName());
        objectMap.put(FabricSummaryExportExcel.styleNo,fabricSummaryStyle.getStyleNo());
        objectMap.put(FabricSummaryExportExcel.styleId,fabricSummaryStyle.getDesignNo());
        objectMap.put(FabricSummaryExportExcel.senderDesignerName,fabricSummaryStyle.getPatternDesignName());
        objectMap.put(FabricSummaryExportExcel.colorCrash,"1".equals(fabricSummaryStyle.getColorCrash()) ? "是":"否");
        objectMap.put(FabricSummaryExportExcel.materialColor,fabricSummaryStyle.getColorName());
        objectMap.put(FabricSummaryExportExcel.supplierColor,fabricSummaryStyle.getSupplierColor());
        objectMap.put(FabricSummaryExportExcel.supplierColorNo,fabricSummaryStyle.getSupplierColorNo());
        objectMap.put(FabricSummaryExportExcel.unitUse,fabricSummaryStyle.getUnitUse());
        objectMap.put(FabricSummaryExportExcel.totalProduction,fabricSummaryStyle.getTotalProduction());
        if (StringUtils.isNotBlank(fabricSummaryStyle.getTotalProduction()) && null != fabricSummaryStyle.getUnitUse()){
            objectMap.put(FabricSummaryExportExcel.needMeter, Double.parseDouble(fabricSummaryStyle.getTotalProduction())*fabricSummaryStyle.getUnitUse().doubleValue());
        }
        mapList.add(objectMap);
        return mapList;
    }

    /**
     * 填充面料信息
     * @param bomFabricVo
     * @return
     */
    private Map<String, Object> getMaterialMap(FabricSummaryInfoVo bomFabricVo) {
        Map<String,Object> objectMap = new HashMap<>();
        if (StringUtils.isNotBlank(bomFabricVo.getPhysicochemistryDetectionResult())){
            objectMap.put(FabricSummaryExportExcel.physicochemistryDetectionResult, Constants.ZERO_STR.equals(bomFabricVo.getPhysicochemistryDetectionResult()) ? "是":"否");
        }
        if (StringUtils.isNotBlank(bomFabricVo.getFittingResult())){
            objectMap.put(FabricSummaryExportExcel.fittingResult, Constants.ZERO_STR.equals(bomFabricVo.getFittingResult()) ? "不合适":"合适");
        }
        if (StringUtils.isNotBlank(bomFabricVo.getIsProtection())){
            objectMap.put(FabricSummaryExportExcel.isProtection, Constants.ZERO_STR.equals(bomFabricVo.getIsProtection()) ? "否":"是");
        }
        objectMap.put(FabricSummaryExportExcel.enquiryCode, bomFabricVo.getEnquiryCode());
        objectMap.put(FabricSummaryExportExcel.materialCode, bomFabricVo.getMaterialCode());
        objectMap.put(FabricSummaryExportExcel.yearSuffix, bomFabricVo.getYearSuffix());
        objectMap.put(FabricSummaryExportExcel.supplierName, bomFabricVo.getSupplierName());
        objectMap.put(FabricSummaryExportExcel.supplierFabricCode, bomFabricVo.getSupplierFabricCode());
        objectMap.put(FabricSummaryExportExcel.supplierFactoryIngredient, bomFabricVo.getSupplierFactoryIngredient());
        objectMap.put(FabricSummaryExportExcel.widthName, bomFabricVo.getWidthName());
        objectMap.put(FabricSummaryExportExcel.gramWeight, bomFabricVo.getGramWeight());
        objectMap.put(FabricSummaryExportExcel.specification, bomFabricVo.getSpecification());
        objectMap.put(FabricSummaryExportExcel.minimumOrderQuantity, bomFabricVo.getMinimumOrderQuantity());
        objectMap.put(FabricSummaryExportExcel.productionDay, bomFabricVo.getProductionDay());
        objectMap.put(FabricSummaryExportExcel.supplierQuotationPrice, bomFabricVo.getSupplierQuotationPrice());
        objectMap.put(FabricSummaryExportExcel.createName, companyUserInfo.get().getCreateName());
        return objectMap;
    }


    /**
     * 截取集合
     * @param list
     * @param elementsPerSubList
     * @return
     */
    private Map<Integer, List<FabricSummaryInfoVo>>  splitListIntoArrays(List<FabricSummaryInfoVo> list, int elementsPerSubList) {
        Map<Integer, List<FabricSummaryInfoVo>> map = new HashMap<>();
        int index = 0;
        for (int i = 0; i < list.size(); i += elementsPerSubList) {
            int remainingElements = list.size() - i;
            int elementsToTake = Math.min(elementsPerSubList, remainingElements);
            List<FabricSummaryInfoVo> subList = new ArrayList<>(list.subList(i, i + elementsToTake));
            map.put(index, subList);
            index++;
        }
        return map;
    }



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatchByDto(String bomVersionId, String overlayFlg, List<PackBomDto> dtoList) {
        CommonUtils.removeQueryList(dtoList, "imageUrl");
        // 校验版本
        PackBomVersion version = packBomVersionService.checkVersion(bomVersionId);
        if (CollUtil.isEmpty(dtoList)) {
            dtoList = new ArrayList<>(2);
        }
        List<String> dbBomIds = getBomIdsByVersionId(version.getId());
        List<String> pageBomIds = new ArrayList<>();
        // 版本有几个物料信息
        Long versionBomCount = getBaseMapper().countByVersion(version.getId());
        // 保存物料清单表
        List<PackBom> packBoms = BeanUtil.copyToList(dtoList, PackBom.class);
        for (PackBom packBom : packBoms) {
            PackUtils.bomDefaultVal(packBom);
            PackUtils.setBomVersionInfo(version, packBom);
            packBom.setStageFlag(Opt.ofBlankAble(packBom.getStageFlag()).orElse(packBom.getPackType()));
            if (!CommonUtils.isInitId(packBom.getId())) {
                pageBomIds.add(packBom.getId());
            } else {
                packBom.setCode(null);
                if (StringUtils.isNotBlank(packBom.getCopyId())){
                    packBom.setSort(getBaseMapper().getByIdSort(packBom.getCopyId()));
                }else {
                    packBom.setSort(Math.toIntExact(versionBomCount++));
                }
            }
            packBom.calculateCost();
        }
        QueryWrapper<PackBom> bomQw = new QueryWrapper<>();
        bomQw.eq("bom_version_id", version.getId());
        //覆盖标识
        boolean fg = StrUtil.equals(overlayFlg, BasicNumber.ONE.getNumber());
        if (fg) {
            //删除之前的尺寸信息
            if (CollUtil.isNotEmpty(dbBomIds)) {
                QueryWrapper delSizeQw = new QueryWrapper();
                delSizeQw.eq("bom_version_id", version.getId());
                delSizeQw.in("bom_id", dbBomIds);
                packBomSizeService.remove(delSizeQw);
                packBomColorService.remove(delSizeQw);
            }
        } else {
            //删除之前的尺寸信息
            if (CollUtil.isNotEmpty(pageBomIds)) {
                QueryWrapper delSizeQw = new QueryWrapper();
                delSizeQw.eq("bom_version_id", version.getId());
                delSizeQw.in("bom_id", pageBomIds);
                packBomSizeService.remove(delSizeQw);
                packBomColorService.remove(delSizeQw);
            }
        }

        //保存
        addAndUpdateAndDelList(packBoms, bomQw, fg);
        // 处理尺码
        List<PackBomSize> bomSizeList = new ArrayList<>(16);
        // 处理物料颜色
        List<PackBomColor> packBomColorList = new ArrayList<>(16);
        for (int i = 0; i < dtoList.size(); i++) {
            PackBomDto packBomDto = dtoList.get(i);
            PackBom packBom = packBoms.get(i);
            // 获取尺码列表
            List<PackBomSizeDto> sizeDtoList = packBomDto.getPackBomSizeList();
            if (CollUtil.isNotEmpty(sizeDtoList)) {
                List<PackBomSize> packBomSizeList = BeanUtil.copyToList(sizeDtoList, PackBomSize.class);
                for (PackBomSize packBomSize : packBomSizeList) {
                    packBomSize.setId(null);
                    packBomSize.setBomId(packBom.getId());
                    packBomSize.setPackType(version.getPackType());
                    packBomSize.setForeignId(version.getForeignId());
                    packBomSize.setBomVersionId(version.getId());
                    /**/
                    packBomSize.setWidthCode(StrUtil.equals(packBomDto.getTranslateCode(),packBomSize.getWidthCode())?packBomDto.getTranslateCode():packBomSize.getWidthCode() );
                    packBomSize.setWidth(StrUtil.equals(packBomDto.getTranslate(),packBomSize.getWidth())?packBomDto.getTranslate():packBomSize.getWidth());
                    packBomSize.setQuantity(
                            Opt.ofNullable(packBomSize.getQuantity()).orElse(
                            StrUtil.equals(packBomDto.getPackType(),PackUtils.PACK_TYPE_DESIGN)?packBomDto.getDesignUnitUse():packBomDto.getBulkUnitUse())
                    );
                }
                bomSizeList.addAll(packBomSizeList);
            }
            // 获取颜色列表
            List<PackBomColorDto> packBomColorDtoList = packBomDto.getPackBomColorDtoList();
            if (CollUtil.isNotEmpty(packBomColorDtoList)) {
                List<PackBomColor> packBomColors = BeanUtil.copyToList(packBomColorDtoList, PackBomColor.class);
                for (PackBomColor packBomColor : packBomColors) {
                    packBomColor.setId(null);
                    packBomColor.setBomId(packBom.getId());
                    packBomColor.setPackType(version.getPackType());
                    packBomColor.setForeignId(version.getForeignId());
                    packBomColor.setBomVersionId(version.getId());
                    packBomColor.setMaterialColorName(StrUtil.isEmpty(packBomColor.getMaterialColorName()) ? packBomDto.getColor() : packBomColor.getMaterialColorName());
                    packBomColor.setMaterialColorCode(StrUtil.isEmpty(packBomColor.getMaterialColorCode()) ? packBomDto.getColorCode() : packBomColor.getMaterialColorCode());
                    packBomColor.insertInit();
                }
                packBomColorList.addAll(packBomColors);
            }
        }
        // 保存尺码
        if (CollUtil.isNotEmpty(bomSizeList)) {
            packBomSizeService.saveBatch(bomSizeList);
        }
        // 保存物料颜色
        if (CollUtil.isNotEmpty(packBomColorList)) {
            packBomColorService.saveBatch(packBomColorList);
        }
        return true;
    }

    /**
     * bom模板引用新增
     *
     * @param bomTemplateSaveDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean bomTemplateSave(BomTemplateSaveDto bomTemplateSaveDto) {
        if (CollUtil.isEmpty(bomTemplateSaveDto.getMaterialList())) {
            throw new OtherException("为选中模板");
        }
        /*获取模板中的物料*/
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = basicsdatumBomTemplateMaterialMapper.selectBatchIds(bomTemplateSaveDto.getMaterialList());
        if (CollUtil.isEmpty(templateMaterialList)) {
            throw new OtherException("模板无物料清单");
        }
        /*获取款的*/
        Style style = styleService.getById(bomTemplateSaveDto.getStyleId());
        String productSizes = style.getProductSizes();
        if (StrUtil.isBlank(productSizes)) {
            throw new OtherException("不存在尺码");
        }
        Boolean styleManyColorSwitch = ccmFeignService.getSwitchByCode(STYLE_MANY_COLOR.getKeyCode());
        String[] productSizess = productSizes.split(",");
        String[] sizeIds = style.getSizeIds().split(",");
        String[] colorCodes = null;
        String[] colors = null;
        if (StringUtils.isNotBlank(style.getColorCodes())) {
            colorCodes = style.getColorCodes().split(",");
            colors = style.getProductColors().split(",");
        }
        // 查询物料
        List<String> materialIdList = templateMaterialList.stream().map(BasicsdatumBomTemplateMaterial::getMaterialId).collect(Collectors.toList());
        Map<String, String> category1CodeMap = basicsdatumMaterialService.mapOneField(new LambdaQueryWrapper<BasicsdatumMaterial>()
                .in(BasicsdatumMaterial::getId, materialIdList), BasicsdatumMaterial::getId, BasicsdatumMaterial::getCategory1Code);
        List<PackBomDto> bomDtoList = BeanUtil.copyToList(templateMaterialList, PackBomDto.class);
        for (PackBomDto b : bomDtoList) {
            b.setId(null);
            b.setBomTemplateId(bomTemplateSaveDto.getBomTemplateId());
            b.setDesignUnitUse(b.getUnitUse());
            b.setBulkUnitUse(b.getUnitUse());
            b.setCategory1Code(category1CodeMap.getOrDefault(b.getMaterialId(), ""));
            List<PackBomSizeDto> sizeDtoList = new ArrayList<>();
            for (int i = 0; i < productSizess.length; i++) {
                PackBomSizeDto packBomSizeDto = new PackBomSizeDto();
                packBomSizeDto.setWidthCode(b.getTranslateCode());
                packBomSizeDto.setWidth(b.getTranslate());
                packBomSizeDto.setSize(productSizess[i]);
                packBomSizeDto.setSizeId(sizeIds[i]);
                sizeDtoList.add(packBomSizeDto);
            }
            b.setPackBomSizeList(sizeDtoList);
            if (styleManyColorSwitch) {
                if (colorCodes != null && colorCodes.length != 0) {
                    List<PackBomColorDto> colorDtos = new ArrayList<>();
                    for (int i = 0; i < colorCodes.length; i++) {
                        PackBomColorDto colorDto = new PackBomColorDto();
                        colorDto.setColorCode(colorCodes[i]);
                        colorDto.setColorName(colors[i]);
                        colorDtos.add(colorDto);
                    }
                    b.setPackBomColorDtoList(colorDtos);
                }
            }
        }
        saveBatchByDto(bomTemplateSaveDto.getBomVersionId(), null, bomDtoList);
        return true;
    }

    @Override
    public List<String> getBomIdsByVersionId(String bomVersionId) {
        return getBaseMapper().getBomIdsByVersionId(bomVersionId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean unusableChange(String id, String unusableFlag) {
        List<String> split = StrUtil.split(id, CharUtil.COMMA);
        PackBom byId = getById(split.get(0));
        /*校验吊牌是否在审核中或已审核完成*/
        PackInfo packInfo = packInfoService.getById(byId.getForeignId());
        HangTag hangTag = hangTagService.getByOne("bulk_style_no", packInfo.getStyleNo());
        if (hangTag != null) {
            /*不是在未填写，未提交阶段时提示不能修改*/
            /*查询这个物料是否存在检查报告 存在是消息通知*/
            List<EscmMaterialCompnentInspectCompanyDto> companyServiceList = escmMaterialCompnentInspectCompanyService.getList(new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().eq("thtic.hang_tag_id", hangTag.getId()).eq("temcic.materials_no", byId.getMaterialCode()));
            if(CollUtil.isNotEmpty(companyServiceList)){
                /*消息通知吊牌的创建人和新增人*/
                messageUtils.sendMessage(null,hangTag.getCreateId()+","+hangTag.getUpdateId(),"物料变更检查报告通知","/hangTag/hangTagAdd?bulkStyleNo="+hangTag.getBulkStyleNo()+"&typeCode=编辑",null,baseController.getUser());
            }
        }
        BigDecimal totalCost = packPricingService.countTotalPrice(byId.getForeignId(),BaseGlobal.STOCK_STATUS_CHECKED,2);
        // 校验版本
        packBomVersionService.checkVersion(byId.getBomVersionId());
        UpdateWrapper<PackBom> uw = new UpdateWrapper<>();
        uw.in("id", split);
        uw.set("unusable_flag", unusableFlag);
        setUpdateInfo(uw);
        log(id, StrUtil.equals(unusableFlag, BaseGlobal.YES) ? "停用" : "启用");
        update(uw);
        if(StrUtil.equals(byId.getScmSendFlag(),BaseGlobal.YES)|| StrUtil.equals(byId.getScmSendFlag(),BaseGlobal.IN_READY)){
            costUpdate(byId.getForeignId(),totalCost);
        }
        return true;
    }

    @Override
    public BigDecimal calculateCosts(PackCommonSearchDto dto) {
        BigDecimal result = BigDecimal.ZERO;
        //查询当前启用版本
        PackBomVersion version = packBomVersionService.getEnableVersion(dto);
        if (version == null) {
            return result;
        }
        // 查询物料列表
//        PackBomVersion enableVersion = packBomVersionService.getEnableVersion(dto.getForeignId(), dto.getPackType());
        dto.setBomVersionId(version.getId());
        PackCommonPageSearchDto packCommonPageSearchDto = BeanUtil.copyProperties(dto, PackCommonPageSearchDto.class);
        packCommonPageSearchDto.setUnusableFlag(BaseGlobal.STATUS_NORMAL);
        List<PackBomVo> packBomPage = baseMapper.getPackBomPage(packCommonPageSearchDto);
        List<PackBom> bomList = BeanUtil.copyToList(packBomPage, PackBom.class);
        if (CollUtil.isEmpty(bomList)) {
            return result;
        }

        // 是否开启笛莎资料包计算开关 开启后资料包计算都是大货物料费用
        Boolean ifSwitch =false;
        try {
            ifSwitch  = ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.DESIGN_DISHA_DATA_PACKAGE_COUNT.getKeyCode());
        }catch (Exception ignored){}
            /*设计还是大货*/
        Boolean loss = "packDesign".equals(dto.getPackType());
        //款式物料费用=款式物料用量*款式物料单价*（1+损耗率)
        //大货物料费用=物料大货用量*物料大货单价*（1+额定损耗率)
        Boolean finalIfSwitch = ifSwitch;
        return bomList.stream().map(packBom -> {
                    /*获取损耗率*/
                    BigDecimal divide = BigDecimal.ONE.add(Optional.ofNullable(finalIfSwitch || !loss ? packBom.getPlanningLoossRate() : packBom.getLossRate()).orElse(BigDecimal.ZERO).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP));
                    BigDecimal mul = NumberUtil.mul(
                            finalIfSwitch || !loss ? packBom.getBulkUnitUse() : packBom.getDesignUnitUse(),
                            packBom.getPrice(),
                            divide
                    );
                    return mul;
                }
        ).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).setScale(3, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> calculateCosts(List<String> foreignIdList, String packType) {
        Map<String, BigDecimal> map = new HashMap<>();
        //查询当前启用版本
        QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
        qw.eq("del_flag", BaseGlobal.NO);
        qw.in("foreign_id", foreignIdList);
        qw.eq("pack_type", packType);
        qw.eq("status", BaseGlobal.YES);
        qw.groupBy("foreign_id");
        List<PackBomVersion> packBomVersionList = packBomVersionService.list(qw);
        if (ObjectUtil.isEmpty(packBomVersionList)) {
            return map;
        }
        // 查询物料列表

        List<String> packBomVersionIdList = packBomVersionList.stream().map(PackBomVersion::getId).collect(Collectors.toList());
        PackCommonPageSearchDto packCommonPageSearchDto = new PackCommonPageSearchDto();
        packCommonPageSearchDto.setUnusableFlag(BaseGlobal.STATUS_NORMAL);
        packCommonPageSearchDto.setPackType(packType);
        packCommonPageSearchDto.setForeignIdList(foreignIdList);
        packCommonPageSearchDto.setBomVersionIdList(packBomVersionIdList);
        List<PackBom> packBomList = baseMapper.getPackBomPageList(packCommonPageSearchDto);
        if (CollUtil.isEmpty(packBomList)) {
            return map;
        }
        //
        // 是否开启笛莎资料包计算开关 开启后资料包计算都是大货物料费用
        Boolean ifSwitch =false;
        try {
            ifSwitch  = ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.DESIGN_DISHA_DATA_PACKAGE_COUNT.getKeyCode());
        }catch (Exception ignored){}
        /*设计还是大货*/
        Boolean loss = "packDesign".equals(packType);
        //款式物料费用=款式物料用量*款式物料单价*（1+损耗率)
        //大货物料费用=物料大货用量*物料大货单价*（1+额定损耗率)
        Boolean finalIfSwitch = ifSwitch;
        Map<String, List<PackBom>> packBomMap = packBomList.stream().collect(Collectors.groupingBy(PackBom::getBomVersionId));

        for (Map.Entry<String, List<PackBom>> stringListEntry : packBomMap.entrySet()) {
            BigDecimal bigDecimal = stringListEntry.getValue().stream().map(packBom -> {
                        /*获取损耗率*/
                        BigDecimal divide = BigDecimal.ONE.add(Optional.ofNullable(finalIfSwitch || !loss ? packBom.getPlanningLoossRate() : packBom.getLossRate()).orElse(BigDecimal.ZERO).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP));
                        BigDecimal mul = NumberUtil.mul(
                                finalIfSwitch || !loss ? packBom.getBulkUnitUse() : packBom.getDesignUnitUse(),
                                packBom.getPrice(),
                                divide
                        );
                        return mul;
                    }
            ).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).setScale(3, RoundingMode.HALF_UP);
            map.put(stringListEntry.getValue().get(0).getForeignId(), ObjectUtil.isEmpty(bigDecimal) ? BigDecimal.ZERO:bigDecimal);
        }
        return map;
    }

    @Override
    public List<PackBom> getListByVersionId(String versionId, String unusableFlag) {
        QueryWrapper<PackBom> qw = new QueryWrapper<>();
        qw.eq("bom_version_id", versionId);
        qw.eq(StrUtil.isNotBlank(unusableFlag), "unusable_flag", unusableFlag);
        return list(qw);
    }

    @Override
    public PageInfo<PackBomVo> pageInfo(PackCommonPageSearchDto dto) {
        PackBomVersion enableVersion = packBomVersionService.getEnableVersion(dto.getForeignId(), dto.getPackType());
        if (enableVersion == null) {
            return null;
        }
        PackBomPageSearchDto bomDto = BeanUtil.copyProperties(dto, PackBomPageSearchDto.class);
        bomDto.setBomVersionId(enableVersion.getId());
        dto.setBomVersionId(enableVersion.getId());
        Page<PackBomVo> page = PageHelper.startPage(dto);
        baseMapper.getPackBomPage(dto);
        PageInfo<PackBomVo> pageInfo = page.toPageInfo();
        // 查询尺码、颜色信息
        List<PackBomVo> packBomVos = pageInfo.getList();
        if (CollUtil.isNotEmpty(packBomVos)) {
            List<String> bomIds = packBomVos.stream().map(PackBomVo::getId).collect(Collectors.toList());
            Map<String, List<PackBomSizeVo>> packBomSizeMap = packBomSizeService.getByBomIdsToMap(bomIds);
            // 查询资料包-物料清单-配色
            Map<String, List<PackBomColorVo>> packBomColorMap = packBomColorService.getByBomIdsToMap(bomIds);
            List<String> materialCodes = packBomVos.stream()
                    .filter(x -> Objects.isNull(x.getBulkPrice()) && Objects.isNull(x.getSupplierPrice()))
                    .map(PackBomVo::getMaterialCode)
                    .collect(Collectors.toList());
            Map<String, BigDecimal> defaultSupplerQuotationPrice = basicsdatumMaterialPriceService.getDefaultSupplerQuotationPrice(materialCodes);
            for (PackBomVo pbv : packBomVos) {
                pbv.setPackBomSizeList(packBomSizeMap.get(pbv.getId()));
                pbv.setPackBomColorVoList(packBomColorMap.get(pbv.getId()));
                if (Objects.isNull(pbv.getBulkPrice()) && Objects.isNull(pbv.getSupplierPrice())) {
                    pbv.setBulkPrice(defaultSupplerQuotationPrice.get(pbv.getMaterialCode()));
                }
                if (Objects.isNull(pbv.getBulkPrice()) && Objects.nonNull(pbv.getSupplierPrice())) {
                    pbv.setBulkPrice(pbv.getSupplierPrice());
                }
            }
        }
        return pageInfo;
    }

    @Override
    public PageInfo<FabricSummaryVO> fabricSummaryList(FabricSummaryDTO fabricSummaryDTO) {
        Page<FabricSummaryVO> page = PageHelper.startPage(fabricSummaryDTO);
        QueryWrapper qw = new QueryWrapper();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.fabric_summary.getK(), "t2.");
//        baseMapper.fabricSummaryList(fabricSummaryDTO, qw);
        if (CollectionUtil.isNotEmpty(page.toPageInfo().getList())) {
            for (FabricSummaryVO fabricSummaryVO : page.toPageInfo().getList()) {
                // 统计物料下被多少款使用
                Integer count = baseMapper.querySampleDesignInfoByMaterialIdCount(new FabricSummaryDTO(fabricSummaryDTO.getCompanyCode(), fabricSummaryVO.getId()));
                fabricSummaryVO.setCuttingNumber(null != count ? count.toString() : String.valueOf(BaseGlobal.ZERO));
            }
        }
        return page.toPageInfo();
    }

    @Override
    public PageInfo<MaterialSampleDesignVO> querySampleDesignInfoByMaterialId(FabricSummaryDTO fabricSummaryDTO) {
        Page<MaterialSampleDesignVO> page = PageHelper.startPage(fabricSummaryDTO);
        baseMapper.querySampleDesignInfoByMaterialIdPage(fabricSummaryDTO);
        return page.toPageInfo();
    }

    @Override
    public List<PricingMaterialCostsVO> getPricingMaterialCostsByForeignId(String foreignId) {
        return baseMapper.getPricingMaterialCostsByForeignId(foreignId);
    }

    /**
     * 获取物料计算
     *
     * @param foreignIds
     * @return
     */
    @Override
    public List<PackBomCalculateBaseVo> getPackBomCalculateBaseVo(List<String> foreignIds) {
        return baseMapper.getPackBomCalculateBaseVo(foreignIds);
    }

    /**
     * 查询物料下发状态
     *
     * @param stringList
     * @return
     */
    @Override
    public Map<String, String> getPackSendStatus(List<String> stringList) {
        Map<String, String> map = new HashMap<>();
        if (CollUtil.isNotEmpty(stringList)) {
            List<Map<String, String>> list = baseMapper.getPackSendStatus(stringList);
            if (CollUtil.isNotEmpty(list)) {
                for (Map<String, String> stringStringMap : list) {
                    map.put(stringStringMap.get("foreign_id"), stringStringMap.get("send_status"));
                }
            }
        }
        return map;
    }


    @Override
    public List<PackBomVo> list(String foreignId, String packType, String bomVersionId) {
        QueryWrapper<PackBom> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, foreignId, packType, null);
        qw.eq("bom_version_id", bomVersionId);
        List<PackBom> list = list(qw);
        return BeanUtil.copyToList(list, PackBomVo.class);
    }

    /**
     * 解锁
     *
     * @param idDto
     * @return
     */
    @Override
    public Boolean unlock(IdDto idDto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("scm_send_flag", BaseGlobal.IN_READY);
        updateWrapper.in("id", com.base.sbc.config.utils.StringUtils.convertList(idDto.getId()));
        baseMapper.update(null, updateWrapper);
        return true;
    }


    @Override
    String getModeName() {
        return "物料清单";
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delByIds(String id) {
        /*cha*/
        /*控制是否下发外部SMP系统开关*/
        Boolean systemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());
        List<PackBom> packBomList = baseMapper.selectBatchIds(StrUtil.split(id, ','));
        if(systemSwitch){
            List<PackBom> packBomList1 = packBomList.stream().filter(s -> StrUtil.equals(s.getScmSendFlag(), BaseGlobal.YES) || StrUtil.equals(s.getScmSendFlag(), BaseGlobal.IN_READY)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(packBomList1)) {
                throw new OtherException("物料中存在已下发数据");
            }
        }
        BigDecimal totalCost = packPricingService.countTotalPrice(packBomList.get(0).getForeignId(),BaseGlobal.STOCK_STATUS_CHECKED,2);
        baseMapper.deleteBatchIds(StrUtil.split(id, ','));
        if(StrUtil.equals(packBomList.get(0).getScmSendFlag(),BaseGlobal.YES)|| StrUtil.equals(packBomList.get(0).getScmSendFlag(),BaseGlobal.IN_READY)){
            costUpdate(packBomList.get(0).getForeignId(),totalCost);
        }
        return true;
    }

// 自定义方法区 不替换的区域【other_end】

    @Override
    public List<PackBomVo> getPackBomVoList(PackCommonPageSearchDto dto) {
        // 查询物料清单数据
        List<PackBomVo> packBomVos = baseMapper.getPackBomListOpen(dto);
        // 查询尺码、颜色信息
        if (CollUtil.isNotEmpty(packBomVos)) {
            List<String> bomIds = packBomVos.stream().map(PackBomVo::getId).collect(Collectors.toList());
            Map<String, List<PackBomSizeVo>> packBomSizeMap = packBomSizeService.getByBomIdsToMap(bomIds);
            // 查询资料包-物料清单-配色
            Map<String, List<PackBomColorVo>> packBomColorMap = packBomColorService.getByBomIdsToMap(bomIds);
            List<String> materialCodes = packBomVos.stream()
                    .filter(x -> Objects.isNull(x.getBulkPrice()) && Objects.isNull(x.getSupplierPrice()))
                    .map(PackBomVo::getMaterialCode)
                    .collect(Collectors.toList());
            Map<String, BigDecimal> defaultSupplerQuotationPrice = basicsdatumMaterialPriceService.getDefaultSupplerQuotationPrice(materialCodes);
            for (PackBomVo pbv : packBomVos) {
                pbv.setPackBomSizeList(packBomSizeMap.get(pbv.getId()));
                pbv.setPackBomColorVoList(packBomColorMap.get(pbv.getId()));
                if (Objects.isNull(pbv.getBulkPrice()) && Objects.isNull(pbv.getSupplierPrice())) {
                    pbv.setBulkPrice(defaultSupplerQuotationPrice.get(pbv.getMaterialCode()));
                }
                if (Objects.isNull(pbv.getBulkPrice()) && Objects.nonNull(pbv.getSupplierPrice())) {
                    pbv.setBulkPrice(pbv.getSupplierPrice());
                }
            }
        }
        return packBomVos;
    }

    public ApiResult packBomMaterialColor(String companyCode, String id, String colorCode){
        //查询资料包-物料清单-物料版本 启用状态的数据
        QueryWrapper<PackBomVersion> versionQw = new QueryWrapper<>();
        versionQw.eq("foreign_id", id);
        versionQw.eq("pack_type", "packDesign");
        versionQw.eq("status", BaseGlobal.YES);
        versionQw.eq("del_flag", BaseGlobal.NO);
        versionQw.orderByDesc("update_date");
        List<PackBomVersion> packBomVersionList = packBomVersionService.list(versionQw);

        if (CollectionUtil.isNotEmpty(packBomVersionList)) {
            //再根据版本，查找物料清单
            PackBomVersion packBomVersion = packBomVersionList.get(0);

            QueryWrapper<PackBom> packBomQw = new QueryWrapper<>();
            PackUtils.commonQw(packBomQw, id, "packDesign", "0");
            packBomQw.eq("bom_version_id", packBomVersion.getId());
            List<PackBom> packBomList = this.list(packBomQw);
            if(CollectionUtil.isEmpty(packBomList)){
                //没有物料
                return ApiResult.error("找不到物料！", 500);
            }

            QueryWrapper<PackBomColor> bomColorQw;
            for (PackBom bom : packBomList) {
                //查询物料清单 - 配色
                bomColorQw = new QueryWrapper<>();
                PackUtils.commonQw(bomColorQw, id, "packDesign", null);
                bomColorQw.eq("bom_id", bom.getId());
                List<PackBomColor> packBomColorList = packBomColorService.list(bomColorQw);

                Map<String, PackBomColor> colorMap = packBomColorList.stream().collect(Collectors.toMap(PackBomColor::getColorCode, item -> item));

                PackBomColor packBomColor = colorMap.get(colorCode);
                if (packBomColor != null) {
                    bom.setColor(packBomColor.getMaterialColorName());
                    bom.setColorCode(packBomColor.getMaterialColorCode());
                }else{
                    bom.setColor(null);
                    bom.setColorCode(null);
                }
            }
            return ApiResult.success("查询成功！", packBomList);
        }
        return ApiResult.error("找不到数据！", 500);
    }

    @Override
    public Long countByVersion(String id) {
        return baseMapper.countByVersion(id);
    }

    @Override
    public long count(String foreignId, String packType) {
        PackBomVersion enableVersion = packBomVersionService.getEnableVersion(foreignId, packType);
        if (enableVersion == null) {
            return 0;
        }
        return count(new QueryWrapper<PackBom>().lambda().eq(PackBom::getBomVersionId, enableVersion.getId()));

    }

    @Override
    public ApiResult getRenovatePackBomInfo(String id) {
        // 1.查询packBom
        List<String> split = StrUtil.split(id, CharUtil.COMMA);
        PackBom packBom = getById(split.get(0));

        // 2.查询配料，比较变更
        if (null != packBom && StrUtil.isNotEmpty(packBom.getMaterialId())) {
            BasicsdatumMaterial basicsdatumMaterial = basicsdatumMaterialService.getById(packBom.getMaterialId());
            if (null != basicsdatumMaterial) {
                String desc = checkPackBomUpdate(packBom, basicsdatumMaterial);
                return ApiResult.success("查询成功！", desc);
            }
        }

        return ApiResult.success("查询成功！", null);
    }

    @Override
    public boolean renovatePackBom(String id) {
        // 1.查询packBom
        List<String> split = StrUtil.split(id, CharUtil.COMMA);
        PackBom packBom = getById(split.get(0));

        // 2.查询配料，更新packBom
        if (null != packBom && StrUtil.isNotEmpty(packBom.getMaterialId())) {
            BasicsdatumMaterial basicsdatumMaterialVo = basicsdatumMaterialService.getById(packBom.getMaterialId());
            if (null != basicsdatumMaterialVo) {
                syncPackBomFromMaterial(packBom, basicsdatumMaterialVo);

                // 3.保存新的 packBom
                updateById(packBom);
                return true;
            }
        }

        return true;
    }

    /**
     * 比较 packBom 和 BasicsDatumMaterial 是否有变更
     * @param packBom
     * @param basicsdatumMaterialVo
     */
    private String checkPackBomUpdate( PackBom packBom, BasicsdatumMaterial basicsdatumMaterialVo) {
        StringBuffer desc = new StringBuffer();
        if (!StringUtils.equals(normalizeString(packBom.getImageUrl()), normalizeString(basicsdatumMaterialVo.getImageUrl()))) {
            desc.append("物料图片地址有变更" + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getMaterialCodeName()), normalizeString(basicsdatumMaterialVo.getMaterialCodeName()))) {
            desc.append("材料 旧值：" + packBom.getMaterialCodeName() + " 新值：" + basicsdatumMaterialVo.getMaterialCodeName() + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getStockUnitCode()), normalizeString(basicsdatumMaterialVo.getStockUnitCode()))) {
            desc.append("库存单位 旧值：" + packBom.getStockUnitCode() + " 新值：" + basicsdatumMaterialVo.getStockUnitCode() + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getGramWeight()), normalizeString(basicsdatumMaterialVo.getGramWeight()))) {
            desc.append("克重 旧值：" + packBom.getGramWeight() + " 新值：" + basicsdatumMaterialVo.getGramWeight() + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getAuxiliaryMaterial()), normalizeString(basicsdatumMaterialVo.getAuxiliaryMaterial()))) {
            desc.append("辅料材质 旧值：" + packBom.getAuxiliaryMaterial() + " 新值：" + basicsdatumMaterialVo.getAuxiliaryMaterial()+ "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getPurchaseUnitCode()), normalizeString(basicsdatumMaterialVo.getPurchaseUnitCode()))) {
            desc.append("采购单位 旧值：" + packBom.getPurchaseUnitCode() + " 新值：" + basicsdatumMaterialVo.getPurchaseUnitCode() + "\n");
        }
        if (normalizeBigDecimal(packBom.getSupplierPrice()).compareTo(normalizeBigDecimal(basicsdatumMaterialVo.getSupplierQuotationPrice())) != 0) {
            desc.append("供应商报价 旧值：" + normalizeBigDecimal(packBom.getSupplierPrice()) + " 新值：" + normalizeBigDecimal(basicsdatumMaterialVo.getSupplierQuotationPrice()) + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getSupplierMaterialCode()), normalizeString(basicsdatumMaterialVo.getSupplierFabricCode()))) {
            desc.append("供应商物料号 旧值：" + packBom.getSupplierMaterialCode() + " 新值：" + basicsdatumMaterialVo.getSupplierFabricCode() + "\n");
        }
        if (!StringUtils.equals(normalizeString(packBom.getSupplierFactoryIngredient()), normalizeString(basicsdatumMaterialVo.getFactoryComposition()))) {
            desc.append("厂家成分 旧值：" + packBom.getSupplierFactoryIngredient() + " 新值：" + basicsdatumMaterialVo.getFactoryComposition() + "\n");
        }

        // 获取默认供应商
        if (StringUtils.isNotEmpty(basicsdatumMaterialVo.getMaterialCode())) {
            String materialCode = basicsdatumMaterialVo.getMaterialCode();
            BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
            queryWrapper.eq("material_code", materialCode);

            List<BasicsdatumMaterialColor> basicsdatumMaterialColors = basicsdatumMaterialColorService.list(queryWrapper);
            if (CollUtil.isNotEmpty(basicsdatumMaterialColors)) {
                if (!StringUtils.equals(normalizeString(packBom.getColorPic()), normalizeString(basicsdatumMaterialColors.get(0).getPicture()))) {
                    desc.append("颜色图片地址字段有变更" + "\n");
                }
            }

            queryWrapper.eq("select_flag", 1);
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = basicsdatumMaterialPriceService.list(queryWrapper);
            if (CollUtil.isNotEmpty(basicsdatumMaterialPrices)) {
                if (!packBom.getPrice().stripTrailingZeros().equals(basicsdatumMaterialPrices.get(0).getQuotationPrice().stripTrailingZeros())) {
                    desc.append("单价 旧值：" + packBom.getPrice() + " 新值：" + basicsdatumMaterialPrices.get(0).getQuotationPrice() + "\n");
                }
            }
        }

        return desc.toString();
    }

    /**
     * 辅助方法,用来标准化String值，忽略空字符串
     */
    private String normalizeString(String str) {
        return StringUtils.isBlank(str) ? null : str; // 如果为null或空字符串，统一返回null
    }

    /**
     * 辅助方法,用来标准化BigDecimal 空值返回0.0000
     */
    private BigDecimal normalizeBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal == null ? BigDecimal.ZERO.setScale(4) : bigDecimal.setScale(4, BigDecimal.ROUND_UP);
    }

    /**
     * 同步最新材料库数据至PackBom
     * @param packBom
     * @param basicsdatumMaterialVo
     */
    private void syncPackBomFromMaterial (PackBom packBom, BasicsdatumMaterial basicsdatumMaterialVo) {
        packBom.setImageUrl(basicsdatumMaterialVo.getImageUrl());
        packBom.setMaterialCodeName(basicsdatumMaterialVo.getMaterialCodeName());
        packBom.setStockUnitCode(basicsdatumMaterialVo.getStockUnitCode());
        packBom.setStockUnitName(basicsdatumMaterialVo.getStockUnitName());
        packBom.setGramWeight(basicsdatumMaterialVo.getGramWeight());
        packBom.setAuxiliaryMaterial(basicsdatumMaterialVo.getAuxiliaryMaterial());
        packBom.setPurchaseUnitCode(basicsdatumMaterialVo.getPurchaseUnitCode());
        packBom.setSupplierPrice(basicsdatumMaterialVo.getSupplierQuotationPrice());
        packBom.setSupplierMaterialCode(basicsdatumMaterialVo.getSupplierFabricCode());
        packBom.setSupplierFactoryIngredient(basicsdatumMaterialVo.getFactoryComposition());

        // 获取默认供应商
        if (StringUtils.isNotEmpty(basicsdatumMaterialVo.getMaterialCode())) {
            String materialCode = basicsdatumMaterialVo.getMaterialCode();
            BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
            queryWrapper.eq("material_code", materialCode);

            // 检查图片是否更新
            List<BasicsdatumMaterialColor> basicsdatumMaterialColorList = basicsdatumMaterialColorService.list(queryWrapper);
            if (CollUtil.isNotEmpty(basicsdatumMaterialColorList)) {
                BasicsdatumMaterialColor basicsdatumMaterialColor = basicsdatumMaterialColorList.stream()
                        .filter(materialColor -> StringUtils.equals(packBom.getColorCode(), materialColor.getColorCode()))
                        .findFirst().orElse(null);
                if (null != basicsdatumMaterialColor) {
                    packBom.setColorPic(basicsdatumMaterialColor.getPicture());
                }
            }

            // 供应商，单价、成本
            queryWrapper.eq("select_flag", 1);
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = basicsdatumMaterialPriceService.list(queryWrapper);
            if (CollUtil.isNotEmpty(basicsdatumMaterialPrices)) {
                packBom.setPrice(basicsdatumMaterialPrices.get(0).getQuotationPrice());
                packBom.calculateCost();
            }
        }
    }

    /**
     * 补充供应商色号信息
     * @param fabricSummary
     * @param fabricStyleVo
     */
    private void fullSupplierColorCode(FabricSummary fabricSummary, FabricSummaryStyle fabricStyleVo) {
        if (StringUtils.isBlank(fabricStyleVo.getColorCode())){
            return;
        }
        List<BasicsdatumMaterialColor> list  = materialColorService.getBasicsdatumMaterialColorCodeList(fabricSummary.getMaterialCode(), fabricStyleVo.getMaterialColorCode());
        if (CollUtil.isNotEmpty(list)){
            fabricStyleVo.setSupplierColorNo(list.get(0).getSupplierColorCode());
        }

    }

    /**
     * 补充物料相关的信息
     * @param fabricSummary
     * @param fabricSummaryStyle
     */
    private void fullMaterialBomInfo(FabricSummary fabricSummary, FabricSummaryStyle fabricSummaryStyle) {
        QueryWrapper qc = new QueryWrapper();
        qc.eq("pb.material_code",fabricSummary.getMaterialCode());
        List<PackBom> packBomList = baseMapper.selectByForeignId(qc);
        PackBom packBom ;
        if (CollUtil.isNotEmpty(packBomList) && packBomList.size() > 1){
            List<PackBom> packBoms = packBomList.stream().filter(item -> Constants.ONE_STR.equals(item.getMainFlag())).collect(Collectors.toList());
            packBom = CollUtil.isEmpty(packBoms) ? new PackBom() : packBoms.get(0);
        }else {
            packBom = CollUtil.isEmpty(packBomList) ? new PackBom() : packBomList.get(0);
        }
        fabricSummaryStyle.setUnitUse(packBom.getUnitUse());
        fabricSummaryStyle.setPartCode(packBom.getPartCode());
        fabricSummaryStyle.setPartName(packBom.getPartName());
        fabricSummaryStyle.setMaterialColor(packBom.getColor());
        fabricSummaryStyle.setMaterialColorCode(packBom.getColorCode());
        fabricSummaryStyle.setMaterialColorHex(packBom.getColorHex());
        fabricSummaryStyle.setMaterialColorPic(packBom.getColorPic());
    }


}
