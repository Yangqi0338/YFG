/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevBasicInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevBasicInfo;
import com.base.sbc.module.fabric.mapper.FabricDevBasicInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevBasicInfoService;
import com.base.sbc.module.fabric.vo.FabricDevBasicInfoVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 类描述：面料开发基本信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevBasicInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:34
 */
@Service
public class FabricDevBasicInfoServiceImpl extends BaseServiceImpl<FabricDevBasicInfoMapper, FabricDevBasicInfo> implements FabricDevBasicInfoService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(FabricDevBasicInfoService.class);


    @Override
    public FabricDevBasicInfoVO saveBasicInfo(FabricDevBasicInfoSaveDTO fabricDevBasicInfoSaveDTO, String bizId) {
        logger.info("FabricDevBasicInfoService#saveBasicInfo 保存 fabricDevBasicInfoSaveDTO：{}， bizId:{}", JSON.toJSONString(fabricDevBasicInfoSaveDTO), bizId);
        FabricDevBasicInfo fabricDevBasicInfo = CopyUtil.copy(fabricDevBasicInfoSaveDTO, FabricDevBasicInfo.class);
        fabricDevBasicInfo.setId(this.getId(bizId));
        fabricDevBasicInfo.setBizId(bizId);
        if (StringUtils.isEmpty(fabricDevBasicInfo.getId())) {
            fabricDevBasicInfo.insertInit();
            fabricDevBasicInfo.setCompanyCode(super.getCompanyCode());
            fabricDevBasicInfo.setId(new IdGen().nextIdStr());
        } else {
            fabricDevBasicInfo.updateInit();
        }
        super.saveOrUpdate(fabricDevBasicInfo);

        return CopyUtil.copy(fabricDevBasicInfo, FabricDevBasicInfoVO.class);
    }

    private String getId(String bizId) {
        LambdaQueryWrapper<FabricDevBasicInfo> qw = new QueryWrapper<FabricDevBasicInfo>().lambda()
                .eq(FabricDevBasicInfo::getBizId, bizId)
                .eq(FabricDevBasicInfo::getDelFlag, "0")
                .select(FabricDevBasicInfo::getId);
        FabricDevBasicInfo fabricDevBasicInfo = super.getBaseMapper().selectOne(qw);
        return Objects.isNull(fabricDevBasicInfo) ? null : fabricDevBasicInfo.getId();
    }

    @Override
    public FabricDevBasicInfoVO getByBizId(String bizId) {
        LambdaQueryWrapper<FabricDevBasicInfo> qw = new QueryWrapper<FabricDevBasicInfo>().lambda()
                .eq(FabricDevBasicInfo::getBizId, bizId)
                .eq(FabricDevBasicInfo::getDelFlag, "0");
        FabricDevBasicInfo fabricDevBasicInfo = super.getBaseMapper().selectOne(qw);
        return CopyUtil.copy(fabricDevBasicInfo, FabricDevBasicInfoVO.class);
    }

    @Override
    public void synchMaterialUpdate(String bizId, String devId, String devApplyId, String toMaterialId, String toMaterialFlag) {
        super.update(this.updateWrapper(bizId, toMaterialId, toMaterialFlag));
        super.update(this.updateWrapper(devId, toMaterialId, toMaterialFlag));
        super.update(this.updateWrapper(devApplyId, toMaterialId, toMaterialFlag));
    }

    private LambdaUpdateWrapper<FabricDevBasicInfo> updateWrapper(String bizId, String toMaterialId, String toMaterialFlag) {
        return new UpdateWrapper<FabricDevBasicInfo>().lambda()
                .eq(FabricDevBasicInfo::getBizId, bizId)
                .set(FabricDevBasicInfo::getToMaterialFlag, toMaterialFlag)
                .set(FabricDevBasicInfo::getToMaterialId, toMaterialId);
    }


// 自定义方法区 不替换的区域【other_end】

}
