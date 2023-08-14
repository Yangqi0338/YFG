/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevBasicInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevBasicInfo;
import com.base.sbc.module.fabric.mapper.FabricDevBasicInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevBasicInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 类描述：面料开发基本信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevBasicInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:25
 */
@Service
public class FabricDevBasicInfoServiceImpl extends BaseServiceImpl<FabricDevBasicInfoMapper, FabricDevBasicInfo> implements FabricDevBasicInfoService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void devBasicInfoSave(FabricDevBasicInfoSaveDTO fabricDevBasicInfoSave, String bizId, String companyCode) {
        FabricDevBasicInfo fabricDevBasicInfo = Objects.isNull(fabricDevBasicInfoSave) ? new FabricDevBasicInfo() :
                CopyUtil.copy(fabricDevBasicInfoSave, FabricDevBasicInfo.class);

        String id = this.getIdByBizId(bizId);
        if (StringUtils.isEmpty(id)) {
            IdGen idGen = new IdGen();
            fabricDevBasicInfo.setId(idGen.nextIdStr());
            fabricDevBasicInfo.insertInit();
            fabricDevBasicInfo.setCompanyCode(companyCode);
        } else {
            fabricDevBasicInfo.setId(id);
            fabricDevBasicInfo.updateInit();
        }
        super.saveOrUpdate(fabricDevBasicInfo);
    }

    private String getIdByBizId(String bizId) {
        LambdaQueryWrapper<FabricDevBasicInfo> queryWrapper = new QueryWrapper<FabricDevBasicInfo>()
                .lambda()
                .eq(FabricDevBasicInfo::getDelFlag, "0")
                .eq(FabricDevBasicInfo::getBizId, bizId)
                .select(FabricDevBasicInfo::getId);
        FabricDevBasicInfo one = super.getOne(queryWrapper);
        return Objects.isNull(one) ? null : one.getId();
    }


// 自定义方法区 不替换的区域【other_end】

}
