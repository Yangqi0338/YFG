/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.column.dto.ColumnGroupDefineDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.entity.ColumnGroupDefine;
import com.base.sbc.module.column.entity.ColumnGroupDefineItem;
import com.base.sbc.module.column.mapper.ColumnGroupDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.column.service.ColumnGroupDefineItemService;
import com.base.sbc.module.column.service.ColumnGroupDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述： service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.service.ColumnGroupDefineService
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
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
        List<ColumnDefine> byTableCode = columnDefineService.getByTableCode(tableCode,"fabricCompositionType", false);

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
                columnDefine.setIsEdit(userDefineItem.getIsEdit());
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
    public List<ColumnDefine> findDetailByJoblist(String tableCode, List<String> userGroupId) {
        //查询系统级
        List<ColumnDefine> byTableCode = columnDefineService.getByTableCode(tableCode, false);

        //根据用户组查询
        List<ColumnGroupDefineItem> listByHeadIdJobList = columnGroupDefineItemService.findListByHeadIdJobList(tableCode, userGroupId);
        if(CollectionUtils.isNotEmpty(listByHeadIdJobList)){
            //一个用户可能存在多个用户组，过滤出显示的字段，取并集去重
            Map<String, ColumnGroupDefineItem> collect = listByHeadIdJobList.stream().filter(o -> BaseGlobal.YES.equals(o.getHidden())).collect(Collectors.toMap(ColumnGroupDefineItem::getSysId, o -> o, (v1, v2) -> v1));

            List<ColumnDefine> byTableCode1 = new ArrayList<>();
            for (ColumnDefine columnDefine : byTableCode) {
                if (collect.containsKey(columnDefine.getId())) {
                    columnDefine.setSysId(columnDefine.getId());
                    ColumnGroupDefineItem groupDefineItem = collect.get(columnDefine.getId());
                    columnDefine.setId(groupDefineItem.getId());
                    columnDefine.setColumnName(groupDefineItem.getColumnName());
                    columnDefine.setColumnNameI18nKey(groupDefineItem.getColumnNameI18nKey());
                    columnDefine.setHidden(groupDefineItem.getHidden());
                    columnDefine.setAlignType(groupDefineItem.getAlignType());
                    columnDefine.setFixType(groupDefineItem.getFixType());
                    columnDefine.setIsEdit(groupDefineItem.getIsEdit());
                    columnDefine.setColumnWidth(groupDefineItem.getColumnWidth());
                    columnDefine.setSortOrder(groupDefineItem.getSortOrder());
                    columnDefine.setColumnColor(groupDefineItem.getColumnColor());
                    byTableCode1.add(columnDefine);
                }
            }
            byTableCode = byTableCode1;
        }
        byTableCode.sort(Comparator.comparing(ColumnDefine::getSortOrder));

        return byTableCode;
    }

    @Override
    @Transactional
    public void saveMain(ColumnGroupDefineDto dto) {
        String userGroupId = dto.getUserGroupId();
        /*ColumnGroupDefine columnGroupDefine = dto.getColumnGroupDefine();
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
            if (count > 1) {
                throw new OtherException("模板名称不能重复");
            }
        } else {
            columnGroupDefine.preInsert();
            if (count > 0) {
                throw new OtherException("模板名称不能重复");
            }
            columnGroupDefine.setIsDefault(BaseGlobal.NO);
        }
        columnGroupDefine.setUserGroupId(userGroupId);
        String id = columnGroupDefine.getId();
        saveOrUpdate(columnGroupDefine);*/

        List<ColumnGroupDefineItem> itemList = dto.getItemList();


        for (ColumnGroupDefineItem groupDefineItem : itemList) {
            if (StrUtil.isNotEmpty(groupDefineItem.getId())) {
                groupDefineItem.updateInit();
            } else {
                groupDefineItem.insertInit();
            }
            //groupDefineItem.setTableCode(tableCode);
            groupDefineItem.setUserGroupId(userGroupId);
            //groupDefineItem.setVersionId(id);
        }

        columnGroupDefineItemService.saveOrUpdateBatch(itemList);
    }

    @Override
    public List<ColumnDefine> findDetail(String tableCode,String columnCode, String userGroupId) {
        List<ColumnDefine> byTableCode = columnDefineService.getByTableCode(tableCode, columnCode, false);

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
                columnDefine.setIsEdit(userDefineItem.getIsEdit());
                columnDefine.setColumnWidth(userDefineItem.getColumnWidth());
                columnDefine.setSortOrder(userDefineItem.getSortOrder());
                columnDefine.setIsRequired(userDefineItem.getIsRequired());
                columnDefine.setColumnColor(userDefineItem.getColumnColor());
            } else {
                columnDefine.setId(null);
            }
        }
        byTableCode.sort(Comparator.comparing(ColumnDefine::getSortOrder));
        return byTableCode;
    }

}
