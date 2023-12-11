/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.column.dto.ColumnUserDefineDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.entity.ColumnUserDefine;
import com.base.sbc.module.column.entity.ColumnUserDefineItem;
import com.base.sbc.module.column.mapper.ColumnUserDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.column.service.ColumnUserDefineItemService;
import com.base.sbc.module.column.service.ColumnUserDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：用户级列自定义头 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.service.ColumnUserDefineService
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@Service
public class ColumnUserDefineServiceImpl extends BaseServiceImpl<ColumnUserDefineMapper, ColumnUserDefine> implements ColumnUserDefineService {

    private static final String MASTER_KEY = "master";

    @Autowired
    private ColumnDefineService columnDefineService;

    @Autowired
    private ColumnUserDefineItemService columnUserDefineItemService;

    @Override
    public List<ColumnUserDefine> findList(String tableCode) {
        LambdaQueryWrapper<ColumnUserDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnUserDefine::getTableCode, tableCode);
        queryWrapper.eq(ColumnUserDefine::getUserId, getUserId());
        queryWrapper.eq(ColumnUserDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        List<ColumnUserDefine> list = list(queryWrapper);
        ColumnUserDefine sysData = new ColumnUserDefine();
        sysData.setId(MASTER_KEY);
        sysData.setTableName("系统级");
        sysData.setTableCode(tableCode);
        list.add(sysData);
        return list;
    }

    @Override
    public List<ColumnDefine> findDetail(String tableCode, String id) {
        List<ColumnDefine> byTableCode = columnDefineService.getByTableCode(tableCode);

        List<ColumnUserDefineItem> list = columnUserDefineItemService.findListByHeadId(tableCode, id);
        Map<String, ColumnUserDefineItem> collect = list.stream().collect(Collectors.toMap(ColumnUserDefineItem::getSysId, o -> o, (v1, v2) -> v1));

        for (ColumnDefine columnDefine : byTableCode) {
            columnDefine.setSysId(columnDefine.getId());
            if (collect.containsKey(columnDefine.getId())) {
                ColumnUserDefineItem userDefineItem = collect.get(columnDefine.getId());
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
    public List<ColumnDefine> findDefaultDetail(String tableCode) {
        LambdaQueryWrapper<ColumnUserDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnUserDefine::getTableCode, tableCode);
        queryWrapper.eq(ColumnUserDefine::getUserId, getUserId());
        queryWrapper.eq(ColumnUserDefine::getIsDefault, BaseGlobal.YES);
        queryWrapper.eq(ColumnUserDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        ColumnUserDefine one = getOne(queryWrapper);
        return one != null ? this.findDetail(tableCode, one.getId()) : this.findDetail(tableCode, MASTER_KEY);
    }

    @Override
    @Transactional
    public void delete(String id) {
        removeById(id);
        LambdaQueryWrapper<ColumnUserDefineItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnUserDefineItem::getVersionId, id);
        columnUserDefineItemService.remove(queryWrapper);
    }

    @Override
    @Transactional
    public void saveMain(ColumnUserDefineDto dto) {
        String userId = getUserId();
        ColumnUserDefine columnUserDefine = dto.getColumnUserDefine();
        String tableCode = columnUserDefine.getTableCode();
        String tableName = columnUserDefine.getTableName();
        if (StrUtil.isEmpty(tableCode) || StrUtil.isEmpty(tableName)) {
            //名字不能为空
            throw new OtherException("模板名称不能为空");
        }
        LambdaQueryWrapper<ColumnUserDefine> checkOne = new LambdaQueryWrapper<>();
        checkOne.eq(ColumnUserDefine::getTableName, tableName);
        checkOne.eq(ColumnUserDefine::getUserId, userId);
        checkOne.eq(ColumnUserDefine::getTableCode, tableCode);
        checkOne.eq(ColumnUserDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        long count = count(checkOne);

        if (StrUtil.isNotEmpty(columnUserDefine.getId())) {
            columnUserDefine.updateInit();
            if(count > 1){
                throw new OtherException("模板名称不能重复");
            }
        } else {
            columnUserDefine.preInsert();
            if(count > 0){
                throw new OtherException("模板名称不能重复");
            }
            columnUserDefine.setIsDefault(BaseGlobal.NO);
        }
        columnUserDefine.setUserId(userId);
        String id = columnUserDefine.getId();
        saveOrUpdate(columnUserDefine);

        List<ColumnUserDefineItem> itemList = dto.getItemList();


        for (ColumnUserDefineItem userDefineItem : itemList) {
            if (StrUtil.isNotEmpty(userDefineItem.getId())) {
                userDefineItem.updateInit();
            } else {
                userDefineItem.insertInit();
            }
            userDefineItem.setTableCode(tableCode);
            userDefineItem.setUserId(userId);
            userDefineItem.setVersionId(id);
        }

        columnUserDefineItemService.saveOrUpdateBatch(itemList);
    }

    @Override
    @Transactional
    public void setDefault(String tableCode, String id) {
        LambdaUpdateWrapper<ColumnUserDefine> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ColumnUserDefine::getIsDefault, BaseGlobal.NO);
        updateWrapper.eq(ColumnUserDefine::getUserId, getUserId());
        updateWrapper.eq(ColumnUserDefine::getTableCode, tableCode);
        this.update(updateWrapper);
        if (!MASTER_KEY.equals(id)) {
            LambdaQueryWrapper<ColumnUserDefine> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ColumnUserDefine::getTableCode, tableCode);
            queryWrapper.eq(ColumnUserDefine::getId, id);
            ColumnUserDefine one = getOne(queryWrapper);
            if (one != null) {
                one.setIsDefault(BaseGlobal.YES);
                updateById(one);
            }
        }
    }
}
