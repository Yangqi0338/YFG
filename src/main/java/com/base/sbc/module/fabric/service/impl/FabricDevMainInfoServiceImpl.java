/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevApplyAllocationDTO;
import com.base.sbc.module.fabric.dto.FabricDevBasicInfoSaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevMainSaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevInfo;
import com.base.sbc.module.fabric.entity.FabricDevMainInfo;
import com.base.sbc.module.fabric.enums.DevApplyAllocationStatusEnum;
import com.base.sbc.module.fabric.enums.DevStatusEnum;
import com.base.sbc.module.fabric.mapper.FabricDevMainInfoMapper;
import com.base.sbc.module.fabric.service.*;
import com.base.sbc.module.fabric.vo.FabricDevApplyVO;
import com.base.sbc.module.fabric.vo.FabricDevMainListVO;
import com.base.sbc.module.fabric.vo.FabricDevMainVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：面料开发主信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevMainInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:58:04
 */
@Service
public class FabricDevMainInfoServiceImpl extends BaseServiceImpl<FabricDevMainInfoMapper, FabricDevMainInfo> implements FabricDevMainInfoService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(FabricDevMainInfoService.class);
    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;
    @Autowired
    private FabricDevBasicInfoService fabricDevBasicInfoService;
    @Autowired
    private FabricDevInfoService fabricDevInfoService;
    @Autowired
    private FabricDevApplyService fabricDevApplyService;
    @Autowired
    private BasicFabricLibraryService basicFabricLibraryService;

    @Autowired
    private CodeGen codeGen;

    @Override
    public void saveDevMainInfo(FabricDevApplyVO fabricDevApplyVO, FabricDevApplyAllocationDTO applyAllocationDTO) {
        logger.info("FabricDevMainInfoService#saveDevMainInfo 保存 fabricDevApplyVO：{},applyAllocationDTO:{}", JSON.toJSONString(fabricDevApplyVO), JSON.toJSONString(applyAllocationDTO));
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        String[] supplerIds = applyAllocationDTO.getSupplerIds().split(",");
        String[] suppler = applyAllocationDTO.getSuppler().split(",");
        for (int i = 0; i < supplerIds.length; i++) {
            // 保存物料档案信息
            BasicsdatumMaterialVo basicsdatumMaterialVo = this.saveMaterial(CopyUtil.copy(fabricDevApplyVO.getBasicsdatumMaterial(), BasicsdatumMaterialSaveDto.class), null);
            // 保存面料开发信息
            FabricDevMainInfo fabricDevMainInfo = new FabricDevMainInfo();
            String id = idGen.nextIdStr();
            fabricDevMainInfo.setId(id);
            fabricDevMainInfo.insertInit();
            fabricDevMainInfo.setMaterialId(basicsdatumMaterialVo.getId());
            fabricDevMainInfo.setDevCode(codeGen.getIncrCode("K", "dev", companyCode));
            fabricDevMainInfo.setDevApplyCode(fabricDevApplyVO.getDevApplyCode());
            fabricDevMainInfo.setCompanyCode(companyCode);
            fabricDevMainInfo.setSupplerId(supplerIds[i]);
            fabricDevMainInfo.setSuppler(suppler[i]);
            fabricDevMainInfo.setExpectEndDate(applyAllocationDTO.getExpectEndDate());
            fabricDevMainInfo.setExpectStartDate(applyAllocationDTO.getExpectStartDate());
            super.save(fabricDevMainInfo);

            // 保存基本信息
            FabricDevBasicInfoSaveDTO fabricDevBasicInfoSaveDTO = CopyUtil.copy(fabricDevApplyVO.getFabricDevBasicInfo(), FabricDevBasicInfoSaveDTO.class);
            fabricDevBasicInfoService.saveBasicInfo(fabricDevBasicInfoSaveDTO, id);
            applyAllocationDTO.getDevConfigIds().forEach(devConfigId -> {
                fabricDevInfoService.saveDevInfo(devConfigId, applyAllocationDTO.getExpectStartDate(), applyAllocationDTO.getExpectEndDate(), id);
            });
        }
    }

    @Override
    public PageInfo<FabricDevMainListVO> getDevList(FabricDevSearchDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        dto.setCompanyCode(super.getCompanyCode());
        List<FabricDevMainListVO> devList = super.getBaseMapper().getDevList(dto);
        return new PageInfo<>(devList);
    }

    @Override
    public FabricDevMainVO getDetail(String id) {
        FabricDevMainInfo fabricDevMainInfo = super.getById(id);
        if (Objects.isNull(fabricDevMainInfo)) {
            throw new OtherException("数据不存在");
        }
        FabricDevMainVO fabricDevApplyVO = CopyUtil.copy(fabricDevMainInfo, FabricDevMainVO.class);
        fabricDevApplyVO.setFabricDevBasicInfo(fabricDevBasicInfoService.getByBizId(id));
        fabricDevApplyVO.setBasicsdatumMaterial(basicsdatumMaterialService.getBasicsdatumMaterial(fabricDevApplyVO.getMaterialId()));
        return fabricDevApplyVO;
    }

    @Override
    public void update(FabricDevMainSaveDTO fabricDevMainSaveDTO) {
        logger.info("FabricDevMainInfoService#update 保存 fabricDevApplySaveDTO：{}", JSON.toJSONString(fabricDevMainSaveDTO));
        FabricDevMainInfo fabricDevMainInfo = super.getById(fabricDevMainSaveDTO.getId());
        if (Objects.isNull(fabricDevMainInfo)) {
            throw new OtherException("数据不存在");
        }

        // 修改物料信息
        this.saveMaterial(fabricDevMainSaveDTO.getBasicsdatumMaterial(), fabricDevMainInfo.getMaterialId());
        // 修改基本信息
        fabricDevBasicInfoService.saveBasicInfo(fabricDevMainSaveDTO.getFabricDevBasicInfo(), fabricDevMainInfo.getId());
        // 修改开发主信息
        FabricDevMainInfo devMainInfo = new FabricDevMainInfo();
        devMainInfo.setId(fabricDevMainInfo.getId());
        devMainInfo.updateInit();
        devMainInfo.setRemarks(fabricDevMainInfo.getRemarks());
        super.updateById(devMainInfo);
    }

    @Override
    @Transactional
    public void updateDevStatus(String id, String devId, String status) {
        logger.info("FabricDevMainInfoService#updateDevStatus 更新开发状态 id：{}，devId:{},status：{}", id, devId, status);
        fabricDevInfoService.updateStatus(devId, status);

        List<FabricDevInfo> aLlPass = fabricDevInfoService.getAllPass(id);
        boolean allPassFlag = aLlPass.stream()
                .anyMatch(e -> !StringUtils.equals(e.getStatus(), DevStatusEnum.ADOPT.getK()));

        // 更新开发主信息的实际完成时间
        Date practicalStartDate = aLlPass.stream()
                .filter(x ->  Objects.nonNull(x.getPracticalStartDate()))
                .sorted(Comparator.comparing(FabricDevInfo::getPracticalStartDate).reversed())
                .map(FabricDevInfo::getPracticalStartDate)
                .findFirst()
                .orElse(new Date());
        FabricDevMainInfo fabricDevMainInfo = new FabricDevMainInfo();
        fabricDevMainInfo.setId(id);
        fabricDevMainInfo.updateInit();
        fabricDevMainInfo.setPracticalStartDate(practicalStartDate);
        fabricDevMainInfo.setPracticalEndDate(new Date());
        fabricDevMainInfo.setStatus(allPassFlag ? DevStatusEnum.FAIL.getK() : DevStatusEnum.ADOPT.getK());
        super.updateById(fabricDevMainInfo);


        FabricDevMainVO detail = this.getDetail(id);

        //更新开发申请分配状态
        fabricDevApplyService.updateAllocationStatus(detail.getDevApplyCode(), DevApplyAllocationStatusEnum.COMPLETED.getK());
        if (allPassFlag) {
            return;
        }
        // 生成基础面料库
        basicFabricLibraryService.saveBasicFabric(detail);
    }


    /**
     * 保存物料信息
     *
     * @param basicsdatumMaterial
     * @return
     */
    private BasicsdatumMaterialVo saveMaterial(BasicsdatumMaterialSaveDto basicsdatumMaterial, String id) {
        basicsdatumMaterial.setId(id);
        basicsdatumMaterial.setBizType(BasicsdatumMaterialBizTypeEnum.DEV.getK());
        return basicsdatumMaterialService.saveBasicsdatumMaterial(basicsdatumMaterial);
    }


// 自定义方法区 不替换的区域【other_end】

}
