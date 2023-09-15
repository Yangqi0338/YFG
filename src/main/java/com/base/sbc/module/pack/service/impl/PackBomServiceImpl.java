/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.enums.CcmBaseSettingEnum;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.mapper.PackBomMapper;
import com.base.sbc.module.pack.service.PackBomColorService;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import com.base.sbc.module.sample.dto.FabricSummaryDTO;
import com.base.sbc.module.sample.vo.FabricSummaryVO;
import com.base.sbc.module.sample.vo.MaterialSampleDesignVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
public class PackBomServiceImpl extends PackBaseServiceImpl<PackBomMapper, PackBom> implements PackBomService {

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

    @Override
    public PageInfo<PackBomVo> pageInfo(PackBomPageSearchDto dto) {
        QueryWrapper<PackBom> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getBomVersionId()), "bom_version_id", dto.getBomVersionId());
        qw.orderByAsc("sort");
        Page<PackBom> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackBom> pageInfo = page.toPageInfo();
        PageInfo<PackBomVo> voPageInfo = CopyUtil.copy(pageInfo, PackBomVo.class);
        // 查询尺码配置
        List<PackBomVo> pbvs = voPageInfo.getList();
        if (CollUtil.isNotEmpty(pbvs)) {
            List<String> materialCodes = pbvs.stream()
                    .filter(x -> StringUtils.equals(x.getDataSource(), "1"))
                    .map(PackBomVo::getMaterialCode)
                    .collect(Collectors.toList());
            Map<String, String> sourceMaps = basicsdatumMaterialService.getSource(materialCodes);

            List<String> bomIds = pbvs.stream().map(PackBomVo::getId).collect(Collectors.toList());
            Map<String, List<PackBomSizeVo>> packBomSizeMap = packBomSizeService.getByBomIdsToMap(bomIds);
            // 查询资料包-物料清单-配色
            Map<String, List<PackBomColorVo>> packBomColorMap = packBomColorService.getByBomIdsToMap(bomIds);
            for (PackBomVo pbv : pbvs) {
                pbv.setPackBomSizeList(packBomSizeMap.get(pbv.getId()));
                pbv.setPackBomColorVoList(packBomColorMap.get(pbv.getId()));
                pbv.setSource(sourceMaps.get(pbv.getMaterialCode()));
            }
        }
        return voPageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackBomVo saveByDto(PackBomDto dto) {
        dto.calculateCost();
        PackBom packBom = BeanUtil.copyProperties(dto, PackBom.class);
        PackBomVersion version = packBomVersionService.checkVersion(dto.getBomVersionId());

        // 新增
        if (CommonUtils.isInitId(packBom.getId())) {
            packBom.setId(null);
            PackUtils.setBomVersionInfo(version, packBom);
            PackUtils.bomDefaultVal(packBom);
            save(packBom);
        } else {
            PackBom db = getById(dto.getId());
            if (db == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, db);
            PackUtils.setBomVersionInfo(version, db);
            updateById(db);
            packBom = db;
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

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatchByDto(String bomVersionId, String overlayFlg, List<PackBomDto> dtoList) {
        // 校验版本
        PackBomVersion version = packBomVersionService.checkVersion(bomVersionId);
        if (CollUtil.isEmpty(dtoList)) {
            dtoList = new ArrayList<>(2);
        }
        List<String> dbBomIds = getBomIdsByVersionId(version.getId());
        List<String> pageBomIds = new ArrayList<>();
        // 版本有几个物料信息
        Long versionBomCount = null;
        // 保存物料清单表
        List<PackBom> packBoms = BeanUtil.copyToList(dtoList, PackBom.class);
        for (PackBom packBom : packBoms) {
            PackUtils.bomDefaultVal(packBom);
            PackUtils.setBomVersionInfo(version, packBom);
            if (!CommonUtils.isInitId(packBom.getId())) {
                pageBomIds.add(packBom.getId());
            } else {
                if (versionBomCount == null) {
                    versionBomCount = getBaseMapper().countByVersion(version.getId());
                }
                packBom.setCode(version.getVersion() + StrUtil.DASHED + (++versionBomCount));
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

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bom_template_id", bomTemplateSaveDto.getBomTemplateId());
        List<BasicsdatumBomTemplateMaterial> templateMaterialList = basicsdatumBomTemplateMaterialMapper.selectList(queryWrapper);

        if (CollUtil.isEmpty(templateMaterialList)) {
            throw new OtherException("模板无物料清单");
        }
        // 校验版本
        PackBomVersion version = packBomVersionService.checkVersion(bomTemplateSaveDto.getBomVersionId());
        queryWrapper.clear();
        QueryWrapper<PackBom> qw = new QueryWrapper();
        PackUtils.commonQw(qw, version);
        qw.eq("bom_version_id", version.getId());
        qw.eq("foreign_id", version.getForeignId()).and(q -> q.ne("bom_template_id", "").or().isNotNull("bom_template_id"));
        baseMapper.delete(qw);

        List<PackBomDto> bomDtoList = BeanUtil.copyToList(templateMaterialList, PackBomDto.class);
        bomDtoList.forEach(b -> {
            b.setId(null);
            b.setBomTemplateId(bomTemplateSaveDto.getBomTemplateId());
        });
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
        // 校验版本
        packBomVersionService.checkVersion(byId.getBomVersionId());
        UpdateWrapper<PackBom> uw = new UpdateWrapper<>();
        uw.in("id", split);
        uw.set("unusable_flag", unusableFlag);
        setUpdateInfo(uw);
        return update(uw);
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
        PackBomVersion enableVersion = packBomVersionService.getEnableVersion(dto.getForeignId(), dto.getPackType());
        dto.setBomVersionId(enableVersion.getId());
        PackCommonPageSearchDto packCommonPageSearchDto = BeanUtil.copyProperties(dto, PackCommonPageSearchDto.class);
        List<PackBomVo> packBomPage = baseMapper.getPackBomPage(packCommonPageSearchDto);
        List<PackBom> bomList = BeanUtil.copyToList(packBomPage, PackBom.class);
        if (CollUtil.isEmpty(bomList)) {
            return result;
        }

        // 是否开启笛莎资料包计算开关 开启后资料包计算都是大货物料费用
        Boolean ifSwitch = ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.DESIGN_DISHA_DATA_PACKAGE_COUNT.getKeyCode());
        /*设计还是大货*/
        Boolean loss = dto.getPackType().equals("packDesign");
        //款式物料费用=款式物料用量*款式物料单价*（1+损耗率)
        //大货物料费用=物料大货用量*物料大货单价*（1+额定损耗率)
        return bomList.stream().map(packBom -> {
            /*获取损耗率*/
                    BigDecimal divide = BigDecimal.ONE.add(Optional.ofNullable( ifSwitch || !loss ?packBom.getPlanningLoossRate():packBom.getLossRate()).orElse(BigDecimal.ZERO).divide(new BigDecimal("100")));
                    BigDecimal mul = NumberUtil.mul(
                            ifSwitch || !loss? packBom.getBulkUnitUse() :  packBom.getDesignUnitUse() ,
                            packBom.getPrice(),
                            divide
                    );
                    return mul;
                }
        ).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
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
        baseMapper.fabricSummaryList(fabricSummaryDTO);
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
    public boolean delByIds(String id) {
        /*cha*/
        List<PackBom> packBomList = baseMapper.selectBatchIds(StrUtil.split(id, ','));
        List<PackBom> packBomList1 = packBomList.stream().filter(s -> StrUtil.equals(s.getScmSendFlag(), BaseGlobal.YES) || StrUtil.equals(s.getScmSendFlag(), BaseGlobal.IN_READY)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(packBomList1)) {
            throw new OtherException("物料中存在已下发数据");
        }
        baseMapper.deleteBatchIds(StrUtil.split(id, ','));
        return true;
    }

// 自定义方法区 不替换的区域【other_end】

}
