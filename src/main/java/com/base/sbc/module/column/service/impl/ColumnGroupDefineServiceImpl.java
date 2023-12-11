/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.column.dto.ColumnGroupDefineDto;
import com.base.sbc.module.column.entity.*;
import com.base.sbc.module.column.mapper.ColumnGroupDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.column.service.ColumnGroupDefineItemService;
import com.base.sbc.module.column.service.ColumnGroupDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述： service类
 * @address com.base.sbc.module.column.service.ColumnGroupDefineService
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
 * @version 1.0  
 */
@Service
public class ColumnGroupDefineServiceImpl extends BaseServiceImpl<ColumnGroupDefineMapper, ColumnGroupDefine> implements ColumnGroupDefineService {

    private static final String MASTER_KEY = "master";

    @Autowired
    private ColumnDefineService columnDefineService;

    @Autowired
    private ColumnGroupDefineItemService columnGroupDefineItemService;

    @Override
    public List<ColumnDefine> findDetail(String tableCode, String userGroupId) {
        List<ColumnDefine> byTableCode = columnDefineService.getByTableCode(tableCode);

        List<ColumnGroupDefineItem> list = columnGroupDefineItemService.findListByHeadId(tableCode, userGroupId);
        Map<String, ColumnGroupDefineItem> collect = list.stream().collect(Collectors.toMap(ColumnGroupDefineItem::getSysId, o -> o, (v1, v2) -> v1));

        for (ColumnDefine columnDefine : byTableCode) {
            columnDefine.setSysId(columnDefine.getId());
            if (collect.containsKey(columnDefine.getId())) {
                ColumnGroupDefineItem userDefineItem = collect.get(columnDefine.getId());
                columnDefine.setId(userDefineItem.getId());
                columnDefine.setColumnName(userDefineItem.getColumnName());
                columnDefine.setColumnNameI18nKey(userDefineItem.getColumnNameI18nKey());
                columnDefine.setHidden(userDefineItem.getHidden());
                columnDefine.setAlignType(userDefineItem.getAlignType());
                columnDefine.setFixType(userDefineItem.getFixType());
                columnDefine.setColumnWidth(userDefineItem.getColumnWidth());
                columnDefine.setSortOrder(userDefineItem.getSortOrder());
                columnDefine.setColumnColor(userDefineItem.getColumnColor());
            } else {
                columnDefine.setId(null);
            }
        }
        byTableCode.sort(Comparator.comparing(ColumnDefine::getSortOrder));
        return byTableCode;
    }

    @Override
    @Transactional
    public void saveMain(ColumnGroupDefineDto dto) {
        String userGroupId = dto.getUserGroupId();
        ColumnGroupDefine columnGroupDefine = dto.getColumnGroupDefine();
        String tableCode = columnGroupDefine.getTableCode();
        String tableName = columnGroupDefine.getTableName();
        if (StrUtil.isEmpty(tableCode) || StrUtil.isEmpty(tableName)) {
            //名字不能为空
            throw new OtherException("模板名称不能为空");
        }
        LambdaQueryWrapper<ColumnGroupDefine> checkOne = new LambdaQueryWrapper<>();
        checkOne.eq(ColumnGroupDefine::getTableName, tableName);
        checkOne.eq(ColumnGroupDefine::getUserGroupId, userGroupId);
        checkOne.eq(ColumnGroupDefine::getTableCode, tableCode);
        checkOne.eq(ColumnGroupDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        long count = count(checkOne);

        if (StrUtil.isNotEmpty(columnGroupDefine.getId())) {
            columnGroupDefine.updateInit();
            if(count > 1){
                throw new OtherException("模板名称不能重复");
            }
        } else {
            columnGroupDefine.preInsert();
            if(count > 0){
                throw new OtherException("模板名称不能重复");
            }
            columnGroupDefine.setIsDefault(BaseGlobal.NO);
        }
        columnGroupDefine.setUserGroupId(userGroupId);
        String id = columnGroupDefine.getId();
        saveOrUpdate(columnGroupDefine);

        List<ColumnGroupDefineItem> itemList = dto.getItemList();


        for (ColumnGroupDefineItem groupDefineItem : itemList) {
            if (StrUtil.isNotEmpty(groupDefineItem.getId())) {
                groupDefineItem.updateInit();
            } else {
                groupDefineItem.insertInit();
            }
            groupDefineItem.setTableCode(tableCode);
            groupDefineItem.setUserGroupId(userGroupId);
            groupDefineItem.setVersionId(id);
        }

        columnGroupDefineItemService.saveOrUpdateBatch(itemList);
    }
}
