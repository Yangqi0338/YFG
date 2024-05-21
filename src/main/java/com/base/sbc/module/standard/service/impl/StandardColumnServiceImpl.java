/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.dto.StandardColumnSaveDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.mapper.StandardColumnMapper;
import com.base.sbc.module.standard.service.StandardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.EXIST_STANDARD;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

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

    @Autowired
    private StandardColumnCountryRelationService standardColumnCountryRelationService;

    @Autowired
    private CountryLanguageService countryLanguageService;

    private final ReentrantLock saveLock = new ReentrantLock();

    private static final SFunction<StandardColumn, YesOrNoEnum> isDefaultFunc = StandardColumn::getIsDefault;
    private static final SFunction<StandardColumn, String> nameFunc = StandardColumn::getName;
    private static final SFunction<StandardColumn, String> codeFunc = StandardColumn::getCode;
    private static final SFunction<StandardColumn, StandardColumnModel> modelFunc = StandardColumn::getModel;
    private static final SFunction<StandardColumn, StandardColumnType> typeFunc = StandardColumn::getType;
    private static final SFunction<StandardColumn, YesOrNoEnum> showFlagFunc = StandardColumn::getShowFlag;
    private static final SFunction<StandardColumn, String> tableCodeFunc = StandardColumn::getTableCode;
    private static final SFunction<StandardColumn, String> tableNameFunc = StandardColumn::getTableName;
    private static final SFunction<StandardColumn, String> tableTitleJsonFunc = StandardColumn::getTableTitleJson;
    private static final SFunction<StandardColumn, String> idFunc = StandardColumn::getId;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(StandardColumnSaveDto standardColumnSaveDto) {
        StandardColumnType type = standardColumnSaveDto.getType();

        String id = standardColumnSaveDto.getId();

        // 初始化实体类
        StandardColumn standardColumn = new StandardColumn();
        standardColumn.setIsDefault(YesOrNoEnum.NO);
        standardColumn.setModel(standardColumnSaveDto.getModel());

        // 做个最简单的lock
        saveLock.lock();
        try {
            // 构建code唯一qw
            LambdaQueryWrapper<StandardColumn> queryWrapper = new LambdaQueryWrapper<StandardColumn>()
                    .eq(nameFunc, standardColumnSaveDto.getName())
                    .eq(typeFunc, type);
            boolean isUpdate = StrUtil.isNotBlank(id);
            if (isUpdate) {
                standardColumn = this.getById(id);
                queryWrapper.ne(idFunc, id);
            }else {
                // 新创建编码
                StandardColumn maxStandColumn = this.findOne(new BaseLambdaQueryWrapper<StandardColumn>()
                        .eq(typeFunc, type).orderByDesc(idFunc));
                int code = Integer.parseInt(maxStandColumn.getCode().replace(type.getPreCode(), "")) + 1;
                standardColumn.setCode(type.getPreCode() + code);
            }
//        // 无法修改系统默认数据
//        if (!rightOperationValue.equals(standardColumn.getIsDefault())) {
//            throw new OtherException("无法修改系统默认标准");
//        }
            // 属性拷贝
            MORE_LANGUAGE_CV.copy2Entity(standardColumnSaveDto, standardColumn);
            if (this.count(queryWrapper) > 0) {
                throw new OtherException(MoreLanguageProperties.getMsg(EXIST_STANDARD));
            }
            if (!isUpdate) addSingleLanguageRelation(standardColumn);
            this.saveOrUpdate(standardColumn);
        }finally {
            saveLock.unlock();
        }
        RedisStaticFunUtils.hset(RedisKeyConstant.STANDARD_COLUMN_LIST.build(),
                standardColumn.getType().getCode() + RedisKeyBuilder.COMMA + standardColumn.getCode(),
                standardColumn);
        return standardColumn.getId();
    }

    @Async
    public void addSingleLanguageRelation(StandardColumn standardColumn){
        // 处理新增标准列的时候 单语言不同步的问题
        CountryQueryDto countryQueryDto = new CountryQueryDto();
        countryQueryDto.setType(CountryLanguageType.findByStandardColumnType(standardColumn.getType()));
        countryQueryDto.setSingleLanguageFlag(YesOrNoEnum.YES);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        List<StandardColumnCountryRelation> relationList = countryLanguageDtoList.stream().map(countryLanguageDto -> {
            String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(true, countryLanguageDto.getCode(), countryLanguageDto.getType().getCode());
            RedisStaticFunUtils.del(redisKey);
            return new StandardColumnCountryRelation(countryLanguageDto.getId(), standardColumn);
        }).collect(Collectors.toList());
        standardColumnCountryRelationService.saveOrUpdateBatch(relationList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delByIds(List<String> list) {
        List<String> standColumnCodeList = this.listByIds2OneField(list, codeFunc);
        boolean removeSuccess = this.remove(new BaseLambdaQueryWrapper<StandardColumn>()
                .in(idFunc, list)
                .eq(isDefaultFunc, YesOrNoEnum.NO.getValueStr()));
        if (removeSuccess) {
            standardColumnCountryRelationService.remove(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                    .in(StandardColumnCountryRelation::getStandardColumnCode, standColumnCodeList));
            RedisStaticFunUtils.removePattern(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.build());
        }
        RedisStaticFunUtils.del(RedisKeyConstant.STANDARD_COLUMN_LIST.build());
        // 不能删除系统默认标准
        return removeSuccess;
    }

    @Override
    public List<StandardColumnDto> listQuery(StandardColumnQueryDto standardColumnQueryDto) {
        List<StandardColumnType> typeList = standardColumnQueryDto.getTypeList();
        StandardColumnType type = standardColumnQueryDto.getType();
        StandardColumnModel noModel = standardColumnQueryDto.getNoModel();
        List<String> codeList = standardColumnQueryDto.getCodeList();
        YesOrNoEnum showFlag = standardColumnQueryDto.getShowFlag();

        BaseLambdaQueryWrapper<StandardColumn> queryWrapper = new BaseLambdaQueryWrapper<>();

        if (CollectionUtil.isEmpty(typeList) && type != null) {
            typeList = CollUtil.toList(type);
        }

        List<StandardColumn> standardColumnList = this.list(queryWrapper
                .notEmptyIn(typeFunc, typeList)
                .notNullNe(modelFunc, noModel)
                .notEmptyIn(codeFunc, codeList)
                .notNullEq(showFlagFunc, showFlag)
        );

        return BeanUtil.copyToList(standardColumnList, StandardColumnDto.class);
    }

    @Override
    public StandardColumn findByCode(String code) {
        String[] codes = code.split(RedisKeyBuilder.COMMA);
        return this.findOne(new BaseLambdaQueryWrapper<StandardColumn>().like(typeFunc, codes[0]).eq(codeFunc, codes.length > 1 ? codes[1] : ""));
    }



    // 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
