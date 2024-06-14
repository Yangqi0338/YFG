/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import com.base.sbc.module.fabricsummary.mapper.FabricSummaryStyleMapper;
import com.base.sbc.module.fabricsummary.service.FabricSummaryStyleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：款式管理-面料款式关联 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryStyleService
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-28 14:30:28
 * @version 1.0  
 */
@Service
public class FabricSummaryStyleServiceImpl extends BaseServiceImpl<FabricSummaryStyleMapper, FabricSummaryStyle> implements FabricSummaryStyleService {
    @Override
    public List<FabricSummaryStyle> getByGroupStyle(String fabricSummaryId,String groupId, String pomId, String styleNo) {
        QueryWrapper<FabricSummaryStyle> qw = new QueryWrapper<>();
        qw.lambda().eq(StringUtils.isNotEmpty(fabricSummaryId),FabricSummaryStyle::getFabricSummaryId, fabricSummaryId);
        qw.lambda().eq(FabricSummaryStyle::getBomId, pomId);
        qw.lambda().eq(FabricSummaryStyle::getDelFlag, "0");
        qw.lambda().eq(FabricSummaryStyle::getStyleNo, styleNo);
        qw.lambda().eq(FabricSummaryStyle::getGroupId, groupId);
        return list(qw);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            return true;
        }
        UpdateWrapper<FabricSummaryStyle> qw = new UpdateWrapper<>();
        qw.lambda().in(FabricSummaryStyle::getId,ids);
        qw.lambda().set(FabricSummaryStyle::getDelFlag, "1");
        return update(qw);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
