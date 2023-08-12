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
import com.base.sbc.module.fabric.dto.FabricDevMaterialInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevMaterialInfo;
import com.base.sbc.module.fabric.mapper.FabricDevMaterialInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevMaterialInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 类描述：面料开发物料信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevMaterialInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:46
 */
@Service
public class FabricDevMaterialInfoServiceImpl extends BaseServiceImpl<FabricDevMaterialInfoMapper, FabricDevMaterialInfo> implements FabricDevMaterialInfoService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void devMaterialInfoSave(FabricDevMaterialInfoSaveDTO fabricDevMaterialInfoSave, String bizId, String companyCode) {
        FabricDevMaterialInfo fabricDevMaterialInfo = Objects.isNull(fabricDevMaterialInfoSave) ? new FabricDevMaterialInfo() :
                CopyUtil.copy(fabricDevMaterialInfoSave, FabricDevMaterialInfo.class);

        String id = this.getIdByBizId(bizId);
        if (StringUtils.isEmpty(id)) {
            IdGen idGen = new IdGen();
            fabricDevMaterialInfo.setId(idGen.nextIdStr());
            fabricDevMaterialInfo.insertInit();
            fabricDevMaterialInfo.setCompanyCode(companyCode);
        } else {
            fabricDevMaterialInfo.setId(id);
            fabricDevMaterialInfo.updateInit();
        }
        super.saveOrUpdate(fabricDevMaterialInfo);
    }

    private String getIdByBizId(String bizId) {
        LambdaQueryWrapper<FabricDevMaterialInfo> queryWrapper = new QueryWrapper<FabricDevMaterialInfo>()
                .lambda()
                .eq(FabricDevMaterialInfo::getDelFlag, "0")
                .eq(FabricDevMaterialInfo::getBizId, bizId)
                .select(FabricDevMaterialInfo::getId);
        FabricDevMaterialInfo one = super.getOne(queryWrapper);
        return Objects.isNull(one) ? null : one.getId();
    }

// 自定义方法区 不替换的区域【other_end】

}
