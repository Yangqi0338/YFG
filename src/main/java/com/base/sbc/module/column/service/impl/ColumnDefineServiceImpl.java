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
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.mapper.ColumnDefineMapper;
import com.base.sbc.module.column.service.ColumnDefineService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private FieldManagementService fieldManagementService;

    @Override
    public List<ColumnDefine> getByTableCode(String tableCode, boolean isSys) {
        LambdaQueryWrapper<ColumnDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnDefine::getTableCode, tableCode);
        queryWrapper.eq(ColumnDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        if (!isSys) {
            queryWrapper.eq(ColumnDefine::getHidden, BaseGlobal.YES);
        }
        queryWrapper.orderByAsc(ColumnDefine::getSortOrder);
        List<ColumnDefine> list = list(queryWrapper);
        if("styleMarking".equals(tableCode) || "styleMarkingOrder".equals(tableCode)){
            //款式打标导出专用，动态添加表单字段管理中 维度系数的字段
            styleMarkingColumnAdd(list);
        }
        return list;
    }

    private void styleMarkingColumnAdd(List<ColumnDefine> list) {
        List<String> codeList = list.stream().map(ColumnDefine::getColumnCode).distinct().collect(Collectors.toList());
        QueryFieldManagementDto dto = new QueryFieldManagementDto();
        dto.setFormTypeCode("dimensionalCoefficient");
        PageInfo<FieldManagementVo> fieldManagementList = fieldManagementService.getFieldManagementList(dto);
        int index = list.size();
        for (FieldManagementVo fieldManagementVo : fieldManagementList.getList()) {
            if(!codeList.contains(fieldManagementVo.getFieldName())){
                ColumnDefine columnDefine = new ColumnDefine();
                columnDefine.setId(fieldManagementVo.getId());
                columnDefine.setColumnCode(fieldManagementVo.getFieldName());
                columnDefine.setColumnName(fieldManagementVo.getFieldExplain());
                columnDefine.setColumnNameI18nKey(fieldManagementVo.getFieldExplain());
                columnDefine.setColumnType(fieldManagementVo.getFieldType());
                columnDefine.setHidden("1");
                columnDefine.setSortOrder(++index);
                columnDefine.insertInit();
                list.add(columnDefine);
                codeList.add(fieldManagementVo.getFieldName());
            }
        }
    }

    @Override
    @Transactional
    public void saveMain(List<ColumnDefine> list, String tableCode) {
        List<ColumnDefine> byTableCode = getByTableCode(tableCode, true);
        Map<String, ColumnDefine> collect = byTableCode.stream().collect(Collectors.toMap(BaseEntity::getId, o -> o));
        List<String> delIds = new ArrayList<>();
        for (ColumnDefine columnDefine : list) {
            if (StrUtil.isEmpty(columnDefine.getId())) {
                columnDefine.insertInit();
            } else {
                columnDefine.updateInit();
                if(BaseGlobal.DEL_FLAG_DELETE.equals(columnDefine.getDelFlag())){
                    delIds.add(columnDefine.getId());
                }
            }
            columnDefine.setTableCode(tableCode);
        }

        saveOrUpdateBatch(list);
        if(CollectionUtils.isNotEmpty(delIds)){
            removeBatchByIds(delIds);
        }
    }

    @Override
    public List<ColumnDefine> getByTableCode(String tableCode, String columnCode, boolean isSys) {
        LambdaQueryWrapper<ColumnDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnDefine::getTableCode, tableCode);
        queryWrapper.eq(ColumnDefine::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        if (!isSys) {
            queryWrapper.eq(ColumnDefine::getHidden, BaseGlobal.YES);
        }
        if (StringUtils.isNotBlank(columnCode)){
            queryWrapper.eq(ColumnDefine::getColumnCode, columnCode);
        }
        queryWrapper.orderByAsc(ColumnDefine::getSortOrder);
        List<ColumnDefine> list = list(queryWrapper);
        if("styleMarking".equals(tableCode) || "styleMarkingOrder".equals(tableCode)){
            //款式打标导出专用，动态添加表单字段管理中 维度系数的字段
            styleMarkingColumnAdd(list);
        }
        return list;
    }


}
