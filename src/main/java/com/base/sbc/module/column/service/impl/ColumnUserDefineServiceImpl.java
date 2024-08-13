/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.column.dto.ColumnUserDefineDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.entity.ColumnUserDefine;
import com.base.sbc.module.column.entity.ColumnUserDefineItem;
import com.base.sbc.module.column.mapper.ColumnUserDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.column.service.ColumnGroupDefineService;
import com.base.sbc.module.column.service.ColumnUserDefineItemService;
import com.base.sbc.module.column.service.ColumnUserDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private ColumnGroupDefineService columnGroupDefineService;

    @Autowired
    private AmcService amcService;

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
        //查询用户组级配置，如果没有用户组配置则返回的系统级配置
        ApiResult jobList = amcService.getUserGroupByUserId(getUserId());
        List<LinkedHashMap> list1 = (List<LinkedHashMap>) (((LinkedHashMap) jobList.getData()).get("list"));
        List<String> jobs = list1.stream().map(o->o.get("id").toString()).collect(Collectors.toList());
        List<ColumnDefine> byTableCode = columnGroupDefineService.findDetailByJoblist(tableCode, jobs);

        //查询用户配置
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
                columnDefine.setIsEdit(columnDefine.getIsEdit());
                columnDefine.setIsRequired(columnDefine.getIsRequired());
            } else {
                columnDefine.setId(null);
            }
            // 代表是一个枚举
            String dictType = columnDefine.getDictType();
            if (StrUtil.startWith(dictType,"com.base.sbc")) {
                columnDefine.setDictType(decorateDictType(dictType));
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
            if (count > 1) {
                throw new OtherException("模板名称不能重复");
            }
        } else {
            columnUserDefine.preInsert();
            if (count > 0) {
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


    @Override
    public List<ColumnDefine> findDefaultDetailGroup(String tableCode) {
        List<ColumnDefine> columnDefines = findDefaultDetail(tableCode);
        //适配双层表头场景，将返回列配置组装成 树状结构
        String lastGroupName = null;

        List<ColumnDefine> newList = new LinkedList<>();
        for (ColumnDefine columnDefine : columnDefines) {
            if (BaseGlobal.YES.equals(columnDefine.getHidden())) {
                if (StrUtil.isNotEmpty(columnDefine.getGroupName())) {
                    if (lastGroupName != null && lastGroupName.equals(columnDefine.getGroupName())) {
                        ColumnDefine columnDefine1 = newList.get(newList.size() - 1);
                        columnDefine1.getChildren().add(columnDefine);
                    } else {
                        ColumnDefine columnDefine1 = new ColumnDefine();
                        columnDefine1.setGroupName(columnDefine.getGroupName());
                        columnDefine1.setColumnName(columnDefine.getGroupName());
                        columnDefine1.setChildren(new ArrayList<>(Collections.singletonList(columnDefine)));
                        newList.add(columnDefine1);
                    }
                    lastGroupName = columnDefine.getGroupName();
                } else {
                    lastGroupName = null;
                    newList.add(columnDefine);
                }
            }
        }

        return newList;
    }

    /** 反射获取限定名枚举的所有值 */
    private static String decorateDictType(String dictType){
        try {
            Class<?> clazz = Class.forName(dictType);
            JSONArray jsonArray = JSONUtil.createArray();
            if (clazz.isEnum()) {
                Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
                List<String> fieldNames = EnumUtil.getFieldNames(enumClass);
                if (CollUtil.containsAll(fieldNames, Arrays.asList("code","text"))) {
                    Map<String, Object> codeMap = EnumUtil.getNameFieldMap(enumClass,"code");
                    Map<String, Object> textMap = EnumUtil.getNameFieldMap(enumClass,"text");

                    codeMap.forEach((enumName, code)-> {
                        JSONObject jsonObject = JSONUtil.createObj();
                        Object text = textMap.get(enumName);
                        jsonObject.putAll(MapUtil.ofEntries(MapUtil.entry("value",code), MapUtil.entry("label",text)));
                        jsonArray.add(jsonObject);
                    });
                }
            }
            return JSONUtil.toJsonStr(jsonArray);
        } catch (ClassNotFoundException ignored) {}
        return dictType;
    }

    public static void main(String[] args) {
        String s = decorateDictType("com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum");
        System.out.println(s);
    }
}
