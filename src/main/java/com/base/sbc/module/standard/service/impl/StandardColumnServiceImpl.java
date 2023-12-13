/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.dto.StandardColumnSaveDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.mapper.StandardColumnMapper;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类描述：吊牌&洗唛全量标准表 service类
 * @address com.base.sbc.module.moreLanguage.service.StandardColumnService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:01:58
 * @version 1.0  
 */
@Service
public class StandardColumnServiceImpl extends BaseServiceImpl<StandardColumnMapper, StandardColumn> implements StandardColumnService {
    @Override
    @Transactional
    public String save(StandardColumnSaveDto standardColumnSaveDto) {
        String rightOperationValue = YesOrNoEnum.NO.getValueStr();

        String id = standardColumnSaveDto.getId();

        // 初始化实体类
        StandardColumn standardColumn = new StandardColumn();
        standardColumn.setIsDefault(rightOperationValue);
        standardColumn.setType(StandardColumnType.TAG);

        // 构建code唯一qw
        LambdaQueryWrapper<StandardColumn> queryWrapper = new LambdaQueryWrapper<StandardColumn>().eq(StandardColumn::getName, standardColumnSaveDto.getName());
        if (StrUtil.isNotBlank(id)) {
            standardColumn = this.getById(id);
            queryWrapper.ne(StandardColumn::getId, id);
        }else {
            // 新创建编码
            StandardColumnType type = standardColumn.getType();
            long code = this.count(new BaseLambdaQueryWrapper<StandardColumn>()
                    .eq(StandardColumn::getType, type)) + 1;
            standardColumn.setCode(type.getPreCode() + code);
        }
//        // 无法修改系统默认数据
//        if (!rightOperationValue.equals(standardColumn.getIsDefault())) {
//            throw new OtherException("无法修改系统默认标准");
//        }
        // 属性拷贝
        BeanUtil.copyProperties(standardColumnSaveDto, standardColumn);
        if (this.count(queryWrapper) > 0) {
            throw new OtherException("已存在相同的标准表");
        }

        this.saveOrUpdate(standardColumn);
        return standardColumn.getId();
    }

    @Override
    @Transactional
    public boolean delByIds(List<String> list) {
        // 不能删除系统默认标准
        return this.remove(new BaseLambdaQueryWrapper<StandardColumn>()
                        .notEmptyIn(StandardColumn::getId, list)
                .eq(StandardColumn::getIsDefault, YesOrNoEnum.NO.getValueStr()));
    }

    @Override
    public List<StandardColumnDto> listQuery(StandardColumnQueryDto standardColumnQueryDto) {
        List<StandardColumnType> typeList = standardColumnQueryDto.getTypeList();
        StandardColumnModel noModel = standardColumnQueryDto.getNoModel();
        List<String> codeList = standardColumnQueryDto.getCodeList();

        BaseLambdaQueryWrapper<StandardColumn> queryWrapper = new BaseLambdaQueryWrapper<>();

        if (CollUtil.isEmpty(typeList)) {
            typeList = CollUtil.toList(StandardColumnType.TAG);
        }
        queryWrapper.in(StandardColumn::getType, typeList);
        queryWrapper.notNullNe(StandardColumn::getModel, noModel);
        queryWrapper.notEmptyIn(StandardColumn::getCode, codeList);

        List<StandardColumn> standardColumnList = this.list(queryWrapper);

        return BeanUtil.copyToList(standardColumnList, StandardColumnDto.class);
    }

    @Override
    public StandardColumn findByCode(String code) {
        return this.findOne(new BaseLambdaQueryWrapper<StandardColumn>().eq(StandardColumn::getCode, code));
    }

    // 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
