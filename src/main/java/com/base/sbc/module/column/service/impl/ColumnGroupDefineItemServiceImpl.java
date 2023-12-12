/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.column.entity.ColumnGroupDefineItem;
import com.base.sbc.module.column.mapper.ColumnGroupDefineItemMapper;
import com.base.sbc.module.column.service.ColumnGroupDefineItemService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述： service类
 * @address com.base.sbc.module.column.service.ColumnGroupDefineItemService
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
 * @version 1.0  
 */
@Service
public class ColumnGroupDefineItemServiceImpl extends BaseServiceImpl<ColumnGroupDefineItemMapper, ColumnGroupDefineItem> implements ColumnGroupDefineItemService {

    @Override
    public List<ColumnGroupDefineItem> findListByHeadId(String tableCode, String userGroupId) {
        LambdaQueryWrapper<ColumnGroupDefineItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnGroupDefineItem::getTableCode, tableCode);
        queryWrapper.eq(ColumnGroupDefineItem::getUserGroupId, userGroupId);
        queryWrapper.eq(ColumnGroupDefineItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<ColumnGroupDefineItem> findListByHeadIdJobList(String tableCode, List<String> userGroupIds) {
        if(CollectionUtils.isEmpty(userGroupIds)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ColumnGroupDefineItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnGroupDefineItem::getTableCode, tableCode);
        queryWrapper.in(ColumnGroupDefineItem::getUserGroupId, userGroupIds);
        queryWrapper.eq(ColumnGroupDefineItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        return list(queryWrapper);
    }
}
