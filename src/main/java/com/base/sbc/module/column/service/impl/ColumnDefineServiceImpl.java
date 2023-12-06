/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.mapper.ColumnDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：系统级列自定义 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.service.ColumnDefineService
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@Service
public class ColumnDefineServiceImpl extends BaseServiceImpl<ColumnDefineMapper, ColumnDefine> implements ColumnDefineService {
    @Override
    public List<ColumnDefine> getByTableCode(String tableCode) {
        LambdaQueryWrapper<ColumnDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnDefine::getTableCode, tableCode);
        queryWrapper.eq(ColumnDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        queryWrapper.orderByAsc(ColumnDefine::getSortOrder);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public void saveMain(List<ColumnDefine> list, String tableCode) {
        List<ColumnDefine> byTableCode = getByTableCode(tableCode);
        Map<String, ColumnDefine> collect = byTableCode.stream().collect(Collectors.toMap(BaseEntity::getId, o -> o));
        for (ColumnDefine columnDefine : list) {
            if (StrUtil.isEmpty(columnDefine.getId())) {
                columnDefine.preInsert();
            } else {
                columnDefine.updateInit();
            }
            columnDefine.setTableCode(tableCode);
        }

        saveOrUpdateBatch(list);
    }

}
