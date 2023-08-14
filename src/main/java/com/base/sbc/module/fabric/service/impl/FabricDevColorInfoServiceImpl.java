/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricDevColorInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevColorInfo;
import com.base.sbc.module.fabric.mapper.FabricDevColorInfoMapper;
import com.base.sbc.module.fabric.service.FabricDevColorInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 类描述：面料开发颜色信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevColorInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:29
 */
@Service
public class FabricDevColorInfoServiceImpl extends BaseServiceImpl<FabricDevColorInfoMapper, FabricDevColorInfo> implements FabricDevColorInfoService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void devColorInfoSave(List<FabricDevColorInfoSaveDTO> fabricDevColorInfoSaves, String bizId, String companyCode) {
        this.delByBizId(bizId, companyCode);

        if (CollectionUtils.isEmpty(fabricDevColorInfoSaves)) {
            return;
        }
//        fabricDevColorInfoSaves.stream().map(saveDTO -> {
//            FabricDevColorInfo fabricDevColorInfo = CopyUtil.copy(saveDTO, FabricDevColorInfo.class);
//            fabricDevColorInfo.set
//        })


    }

    private void delByBizId(String bizId, String companyCode) {
        LambdaQueryWrapper<FabricDevColorInfo> queryWrapper = new QueryWrapper<FabricDevColorInfo>()
                .lambda()
                .eq(FabricDevColorInfo::getDelFlag, "0")
                .eq(FabricDevColorInfo::getCompanyCode, companyCode)
                .eq(FabricDevColorInfo::getBizId, bizId);
        super.getBaseMapper().delete(queryWrapper);
    }


// 自定义方法区 不替换的区域【other_end】

}
