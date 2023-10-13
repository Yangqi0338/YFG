/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevApplyAllocationDTO;
import com.base.sbc.module.fabric.dto.FabricDevApplySaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevApply;
import com.base.sbc.module.fabric.enums.DevApplyAllocationStatusEnum;
import com.base.sbc.module.fabric.mapper.FabricDevApplyMapper;
import com.base.sbc.module.fabric.service.FabricDevApplyService;
import com.base.sbc.module.fabric.service.FabricDevBasicInfoService;
import com.base.sbc.module.fabric.service.FabricDevMainInfoService;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.base.sbc.module.fabric.vo.FabricDevApplyVO;
import com.base.sbc.module.fabric.vo.FabricDevBasicInfoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：面料开发申请 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevApplyService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:28
 */
@Service
public class FabricDevApplyServiceImpl extends BaseServiceImpl<FabricDevApplyMapper, FabricDevApply> implements FabricDevApplyService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(FabricDevApplyService.class);

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;
    @Autowired
    private CodeGen codeGen;
    @Autowired
    private FabricDevBasicInfoService fabricDevBasicInfoService;
    @Autowired
    private FabricDevMainInfoService fabricDevMainInfoService;
    @Autowired
    private BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;


    @Override
    @Transactional
    public FabricDevApplyVO devAppSave(FabricDevApplySaveDTO fabricDevApplySaveDTO) {
        logger.info("FabricDevApplyService#devAppSave 保存 fabricDevApplySaveDTO：{}", JSON.toJSONString(fabricDevApplySaveDTO));
        FabricDevApply fabricDevApply = CopyUtil.copy(fabricDevApplySaveDTO, FabricDevApply.class);
        BasicsdatumMaterialSaveDto basicsdatumMaterial = fabricDevApplySaveDTO.getBasicsdatumMaterial();
        String materialCode = basicsdatumMaterial.getMaterialCode();
        if (StringUtils.isEmpty(fabricDevApplySaveDTO.getId())) {
            fabricDevApply.setId(new IdGen().nextIdStr());
            String companyCode = super.getCompanyCode();
            fabricDevApply.setCompanyCode(companyCode);
            fabricDevApply.insertInit();
            fabricDevApply.setDevApplyCode(codeGen.getIncrCode("K", "devApply", companyCode));
            basicsdatumMaterial.setMaterialCode(materialCode + "_" + fabricDevApply.getDevApplyCode());
        } else {
            fabricDevApply.updateInit();
            if (StringUtils.isEmpty(fabricDevApply.getMaterialId())) {
                FabricDevApply devApply = super.getById(fabricDevApply.getId());
                fabricDevApply.setMaterialId(devApply.getMaterialId());
            }
        }
        BasicsdatumMaterialVo basicsdatumMaterialVo = this.saveMaterial(basicsdatumMaterial, fabricDevApply.getMaterialId());
        if (StringUtils.isEmpty(fabricDevApply.getMaterialId())) {
            basicsdatumMaterialWidthService.updateMaterialCode(materialCode, basicsdatumMaterial.getMaterialCode());
        }
        fabricDevApply.setMaterialId(basicsdatumMaterialVo.getId());
        super.saveOrUpdate(fabricDevApply);
        FabricDevBasicInfoVO fabricDevBasicInfoVO = fabricDevBasicInfoService.saveBasicInfo(fabricDevApplySaveDTO.getFabricDevBasicInfo(), fabricDevApply.getId());
        FabricDevApplyVO fabricDevApplyVO = CopyUtil.copy(fabricDevApply, FabricDevApplyVO.class);
        fabricDevApplyVO.setFabricDevBasicInfo(fabricDevBasicInfoVO);
        fabricDevApplyVO.setBasicsdatumMaterial(basicsdatumMaterialVo);
        return fabricDevApplyVO;
    }

    @Override
    public FabricDevApplyVO getDetail(String id) {
        FabricDevApply fabricDevApply = super.getById(id);
        if (Objects.isNull(fabricDevApply)) {
            throw new OtherException("数据不存在");
        }
        FabricDevApplyVO fabricDevApplyVO = CopyUtil.copy(fabricDevApply, FabricDevApplyVO.class);
        fabricDevApplyVO.setFabricDevBasicInfo(fabricDevBasicInfoService.getByBizId(id));
        fabricDevApplyVO.setBasicsdatumMaterial(basicsdatumMaterialService.getBasicsdatumMaterial(fabricDevApplyVO.getMaterialId()));
        return fabricDevApplyVO;
    }

    @Override
    public PageInfo<FabricDevApplyListVO> getDevApplyList(FabricDevSearchDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        dto.setCompanyCode(super.getCompanyCode());
        List<FabricDevApplyListVO> devApplyList = super.getBaseMapper().getDevApplyList(dto);
        return new PageInfo<>(devApplyList);
    }

    @Override
    @Transactional
    public void allocationTasks(FabricDevApplyAllocationDTO fabricDevApplyAllocationDTO) {
        logger.info("FabricDevApplyService#assignTasks 分配任务 fabricDevApplyAllocationDTO：{}", JSON.toJSONString(fabricDevApplyAllocationDTO));
        FabricDevApplyVO fabricDevApplyVO = this.getDetail(fabricDevApplyAllocationDTO.getId());
        fabricDevMainInfoService.saveDevMainInfo(fabricDevApplyVO, fabricDevApplyAllocationDTO);
        FabricDevApply fabricDevApply = new FabricDevApply();
        fabricDevApply.setId(fabricDevApplyAllocationDTO.getId());
        fabricDevApply.updateInit();
        fabricDevApply.setAllocationStatus(DevApplyAllocationStatusEnum.IN_PROGRESS.getK());
        super.updateById(fabricDevApply);
    }

    @Override
    public void updateAllocationStatus(String devApplyCode, String allocationStatus) {
        LambdaUpdateWrapper<FabricDevApply> qw = new UpdateWrapper<FabricDevApply>()
                .lambda()
                .eq(FabricDevApply::getDevApplyCode, devApplyCode)
                .set(FabricDevApply::getAllocationStatus, allocationStatus);
        super.update(qw);
    }

    @Override
    public String getByDevApplyCode(String devApplyCode) {
        LambdaQueryWrapper<FabricDevApply> qw = new QueryWrapper<FabricDevApply>()
                .lambda()
                .eq(FabricDevApply::getDevApplyCode, devApplyCode).select(FabricDevApply::getId);
        FabricDevApply fabricDevApply = super.getBaseMapper().selectOne(qw);
        return Objects.isNull(fabricDevApply) ? null : fabricDevApply.getId();
    }

    /**
     * 保存物料信息
     *
     * @param basicsdatumMaterial
     * @return
     */
    private BasicsdatumMaterialVo saveMaterial(BasicsdatumMaterialSaveDto basicsdatumMaterial, String id) {
        basicsdatumMaterial.setId(id);
        basicsdatumMaterial.setBizType(BasicsdatumMaterialBizTypeEnum.DEV_APPLY.getK());
        return basicsdatumMaterialService.saveBasicsdatumMaterial(basicsdatumMaterial);
    }


// 自定义方法区 不替换的区域【other_end】

}
