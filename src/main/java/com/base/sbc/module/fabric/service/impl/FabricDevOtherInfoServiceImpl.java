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
import com.base.sbc.module.fabric.dto.FabricDevOtherInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevOtherInfo;
import com.base.sbc.module.fabric.mapper.FabricDevOtherInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevOtherInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 类描述：面料开发其他信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevOtherInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:50
 */
@Service
public class FabricDevOtherInfoServiceImpl extends BaseServiceImpl<FabricDevOtherInfoMapper, FabricDevOtherInfo> implements FabricDevOtherInfoService {

    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void devOtherInfoSave(FabricDevOtherInfoSaveDTO fabricDevOtherInfoSave, String bizId, String companyCode) {
        FabricDevOtherInfo fabricDevOtherInfo = Objects.isNull(fabricDevOtherInfoSave) ? new FabricDevOtherInfo() :
                CopyUtil.copy(fabricDevOtherInfoSave, FabricDevOtherInfo.class);

        String id = this.getIdByBizId(bizId);
        if (StringUtils.isEmpty(id)) {
            IdGen idGen = new IdGen();
            fabricDevOtherInfo.setId(idGen.nextIdStr());
            fabricDevOtherInfo.insertInit();
            fabricDevOtherInfo.setCompanyCode(companyCode);
        } else {
            fabricDevOtherInfo.setId(id);
            fabricDevOtherInfo.updateInit();
        }
        super.saveOrUpdate(fabricDevOtherInfo);
    }

    private String getIdByBizId(String bizId) {
        LambdaQueryWrapper<FabricDevOtherInfo> queryWrapper = new QueryWrapper<FabricDevOtherInfo>()
                .lambda()
                .eq(FabricDevOtherInfo::getDelFlag, "0")
                .eq(FabricDevOtherInfo::getBizId, bizId)
                .select(FabricDevOtherInfo::getId);
        FabricDevOtherInfo one = super.getOne(queryWrapper);
        return Objects.isNull(one) ? null : one.getId();
    }


// 自定义方法区 不替换的区域【other_end】

}
