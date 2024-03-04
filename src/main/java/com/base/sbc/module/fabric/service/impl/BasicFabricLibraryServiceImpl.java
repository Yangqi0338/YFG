/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.alibaba.fastjson.JSON;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySaveDTO;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySearchDTO;
import com.base.sbc.module.fabric.dto.FabricDevBasicInfoSaveDTO;
import com.base.sbc.module.fabric.entity.BasicFabricLibrary;
import com.base.sbc.module.fabric.mapper.BasicFabricLibraryMapper;
import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
import com.base.sbc.module.fabric.service.FabricDevApplyService;
import com.base.sbc.module.fabric.service.FabricDevBasicInfoService;
import com.base.sbc.module.fabric.service.FabricPoolService;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryListVO;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryVO;
import com.base.sbc.module.fabric.vo.FabricDevMainVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.collection.CollUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类描述：基础面料库 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.BasicFabricLibraryService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:23
 */
@Service
public class BasicFabricLibraryServiceImpl extends BaseServiceImpl<BasicFabricLibraryMapper, BasicFabricLibrary> implements BasicFabricLibraryService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(BasicFabricLibraryService.class);

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;
    @Autowired
    private FabricDevBasicInfoService fabricDevBasicInfoService;
    @Autowired
    private FabricDevApplyService fabricDevApplyService;
    @Autowired
    private BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;

    @Autowired
    private BasicsdatumMaterialOldService materialOldService;
    @Autowired
    private BasicsdatumMaterialWidthService materialWidthService;

    @Autowired
    private BasicsdatumMaterialColorService materialColorService;
    @Autowired
    private BasicsdatumMaterialIngredientService materialIngredientService;
    @Autowired
    private BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;
    @Autowired
    private FabricPoolService fabricPoolService;

    @Override
    public void saveBasicFabric(FabricDevMainVO fabricDevMainVO) {
        logger.info("BasicFabricLibraryService#saveBasicFabric 保存 fabricDevMainVO：{}", JSON.toJSONString(fabricDevMainVO));
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        // 保存物料档案信息
        BasicsdatumMaterialVo basicsdatumMaterialVo = this.saveMaterial(CopyUtil.copy(fabricDevMainVO.getBasicsdatumMaterial(), BasicsdatumMaterialSaveDto.class), null, BasicsdatumMaterialBizTypeEnum.FABRIC_LIBRARY.getK());
        // 保存基础面料信息
        BasicFabricLibrary basicFabricLibrary = new BasicFabricLibrary();
        String id = idGen.nextIdStr();
        basicFabricLibrary.setId(id);
        basicFabricLibrary.insertInit();
        basicFabricLibrary.setMaterialId(basicsdatumMaterialVo.getId());
        basicFabricLibrary.setCompanyCode(companyCode);
        basicFabricLibrary.setDevApplyCode(fabricDevMainVO.getDevApplyCode());
        basicFabricLibrary.setDevCode(fabricDevMainVO.getDevCode());
        basicFabricLibrary.setDevMainId(fabricDevMainVO.getId());
        super.save(basicFabricLibrary);
        // 保存基本信息
        FabricDevBasicInfoSaveDTO fabricDevBasicInfoSaveDTO = CopyUtil.copy(fabricDevMainVO.getFabricDevBasicInfo(), FabricDevBasicInfoSaveDTO.class);
        fabricDevBasicInfoService.saveBasicInfo(fabricDevBasicInfoSaveDTO, id);
    }

    @Override
    public PageInfo<BasicFabricLibraryListVO> getBasicFabricLibraryList(BasicFabricLibrarySearchDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        dto.setCompanyCode(super.getCompanyCode());
        List<BasicFabricLibraryListVO> basicFabricLibraryList = super.getBaseMapper().getBasicFabricLibraryList(dto);
        return new PageInfo<>(basicFabricLibraryList);
    }

    @Override
    public BasicFabricLibraryVO getDetail(String id) {
        BasicFabricLibrary basicFabricLibrary = super.getById(id);
        if (Objects.isNull(basicFabricLibrary)) {
            throw new OtherException("数据不存在");
        }
        BasicFabricLibraryVO basicFabricLibraryVO = CopyUtil.copy(basicFabricLibrary, BasicFabricLibraryVO.class);
        basicFabricLibraryVO.setFabricDevBasicInfo(fabricDevBasicInfoService.getByBizId(id));
        basicFabricLibraryVO.setBasicsdatumMaterial(basicsdatumMaterialService.getBasicsdatumMaterial(basicFabricLibraryVO.getMaterialId()));
        return basicFabricLibraryVO;
    }

    @Override
    @Transactional
    public void update(BasicFabricLibrarySaveDTO dto) {
        logger.info("BasicFabricLibraryService#update 保存 dto：{}", JSON.toJSONString(dto));
        BasicFabricLibrary basicFabricLibrary = super.getById(dto.getId());
        if (Objects.isNull(basicFabricLibrary)) {
            throw new OtherException("数据不存在");
        }

        // 修改物料信息
        this.saveMaterial(dto.getBasicsdatumMaterial(), basicFabricLibrary.getMaterialId(), BasicsdatumMaterialBizTypeEnum.FABRIC_LIBRARY.getK());
        // 修改基本信息
        fabricDevBasicInfoService.saveBasicInfo(dto.getFabricDevBasicInfo(), basicFabricLibrary.getId());
        // 修改开发主信息
        BasicFabricLibrary fabricLibrary = new BasicFabricLibrary();
        fabricLibrary.setId(dto.getId());
        fabricLibrary.updateInit();
        fabricLibrary.setRemarks(dto.getRemarks());
        super.updateById(fabricLibrary);
    }

    @Override
    @Transactional
    public void generateMaterial(String id) {
        // 查询是否已经转至物料档案，如果已经转至，则报错，
        BasicFabricLibraryVO basicFabricLibraryVO = this.getDetail(id);
        if (!YesOrNoEnum.NO.getValueStr().equals(basicFabricLibraryVO.getFabricDevBasicInfo().getToMaterialFlag())) {
            throw new OtherException("该面料库已经转至物料档案");
        }
        // 生成物料档案
        BasicsdatumMaterialSaveDto basicsdatumMaterialSaveDto = CopyUtil.copy(basicFabricLibraryVO.getBasicsdatumMaterial(), BasicsdatumMaterialSaveDto.class);
        basicsdatumMaterialSaveDto.setConfirmStatus("0");
        basicsdatumMaterialSaveDto.setSource("2");
        String newMaterialCode = basicsdatumMaterialSaveDto.getMaterialCode().split("_")[0];
        basicsdatumMaterialSaveDto.setMaterialCode(newMaterialCode);
        basicsdatumMaterialSaveDto.setIngredientList(null);
        basicsdatumMaterialSaveDto.setFactoryCompositionList(null);
        BasicsdatumMaterialVo basicsdatumMaterialVo = this.saveMaterial(basicsdatumMaterialSaveDto, null, BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());

        basicsdatumMaterialPriceService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        materialOldService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        materialWidthService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        materialColorService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        materialIngredientService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        basicsdatumMaterialPriceDetailService.copyByMaterialCode(basicFabricLibraryVO.getBasicsdatumMaterial().getMaterialCode(), newMaterialCode);
        // 更新基本信息“是否转至物料档案”、“转至物料档案id”(包括开发、开发申请)
        String devApplyId = fabricDevApplyService.getByDevApplyCode(basicFabricLibraryVO.getDevApplyCode());
        fabricDevBasicInfoService.synchMaterialUpdate(basicsdatumMaterialVo.getId(), YesOrNoEnum.YES.getValueStr(), YesOrNoEnum.NO.getValueStr(),
                basicFabricLibraryVO.getId(), basicFabricLibraryVO.getDevMainId(), devApplyId);
    }

    @Override
    public void materialApproveProcessing(String materialId, String approveStatus, String materialCode) {
        BasicFabricLibrary basicFabricLibrary = super.getBaseMapper().getByToMaterialId(materialId);
        if (Objects.isNull(basicFabricLibrary)) {
            return;
        }
        String devApplyId = fabricDevApplyService.getByDevApplyCode(basicFabricLibrary.getDevApplyCode());

        if (BaseConstant.APPROVAL_PASS.equals(approveStatus)) {
            fabricDevBasicInfoService.synchMaterialUpdate(materialId, YesOrNoEnum.YES.getValueStr(), YesOrNoEnum.YES.getValueStr(),
                    basicFabricLibrary.getId(), basicFabricLibrary.getDevMainId(), devApplyId);
            String fabricLibraryMaterialCode = basicsdatumMaterialService.getMaterialCodeById(basicFabricLibrary.getMaterialId());
            fabricPoolService.materialReviewPassedSync(fabricLibraryMaterialCode, materialId, materialCode);
        } else {
            fabricDevBasicInfoService.synchMaterialUpdate(null, YesOrNoEnum.NO.getValueStr(), YesOrNoEnum.NO.getValueStr(),
                    basicFabricLibrary.getId(), basicFabricLibrary.getDevMainId(), devApplyId);
        }
    }

    @Override
    public Map<String, BasicFabricLibraryListVO> getByMaterialCodes(List<String> materialCodes) {
        if (CollUtil.isEmpty(materialCodes)) {
            return new HashMap<>();
        }
        BasicFabricLibrarySearchDTO dto = new BasicFabricLibrarySearchDTO();
        dto.setMaterialCodes(materialCodes);
        dto.setCompanyCode(super.getCompanyCode());
        List<BasicFabricLibraryListVO> basicFabricLibraryList = super.getBaseMapper().getBasicFabricLibraryList(dto);

        return CollUtil.isEmpty(basicFabricLibraryList) ? new HashMap<>() : basicFabricLibraryList.stream()
                .collect(Collectors.toMap(BasicFabricLibraryListVO::getMaterialCode, Function.identity(), (k1, k2) -> k2));
    }


    /**
     * 保存物料信息
     *
     * @param bizType
     * @param basicsdatumMaterial
     * @return
     */
    private BasicsdatumMaterialVo saveMaterial(BasicsdatumMaterialSaveDto basicsdatumMaterial, String id, String bizType) {
        basicsdatumMaterial.setId(id);
        basicsdatumMaterial.setBizType(bizType);
        return basicsdatumMaterialService.saveBasicsdatumMaterial(basicsdatumMaterial);
    }

// 自定义方法区 不替换的区域【other_end】

}
