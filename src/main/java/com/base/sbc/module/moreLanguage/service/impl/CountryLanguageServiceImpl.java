/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;
import java.util.Date;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.google.common.collect.Maps;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.DictBusinessConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.mapper.CountryLanguageMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：国家地区表 service类
 * @address com.base.sbc.module.moreLanguage.service.CountryService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0  
 */
@Service
public class CountryLanguageServiceImpl extends BaseServiceImpl<CountryLanguageMapper, CountryLanguage> implements CountryLanguageService {

    @Autowired
    private StandardColumnCountryRelationService relationService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Autowired
    private CcmFeignService ccmFeignService;

    private final ReentrantLock saveLock = new ReentrantLock();

    @Override
    public List<CountryLanguageDto> listQuery(CountryQueryDto countryQueryDto) {
        List<CountryLanguage> countryLanguageList;
        if (countryQueryDto.isCache()) {
            RedisKeyBuilder parentKeyBuilder = RedisKeyConstant.COUNTRY_LANGUAGE.add(true, countryQueryDto.getCode());
            if (countryQueryDto.getType() != null) {
                parentKeyBuilder.add(countryQueryDto.getType().getCode() );
            }
            Set<String> keys = RedisStaticFunUtils.keys(parentKeyBuilder.build());
            countryLanguageList = keys.stream().map(key->  (CountryLanguage) RedisStaticFunUtils.get(key)).collect(Collectors.toList());
        }else {
            countryLanguageList = this.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                    .notEmptyIn(CountryLanguage::getCode, countryQueryDto.getCode())
                    .notNullEq(CountryLanguage::getType, countryQueryDto.getType())
                    .notEmptyEq(CountryLanguage::getCountryCode, countryQueryDto.getCountryCode())
                    .notEmptyIn(CountryLanguage::getLanguageCode, countryQueryDto.getLanguageCode())
                    .notEmptyIn(CountryLanguage::getCountryName, countryQueryDto.getCountryName())
                    .notEmptyIn(CountryLanguage::getLanguageName, countryQueryDto.getLanguageName())
                    .notEmptyEq(CountryLanguage::getEnableFlag, countryQueryDto.getEnableFlag())
                    .eq(CountryLanguage::getSingleLanguageFlag, countryQueryDto.isSingleLanguage() ? YesOrNoEnum.YES : YesOrNoEnum.NO)
                    .orderByDesc(CountryLanguage::getCodeIndex).orderByAsc(CountryLanguage::getType)
            );
        }

        return countryLanguageList.stream().map(list-> BeanUtil.copyProperties(list, CountryLanguageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto, boolean cache) {

        String countryName = countryTypeLanguageSaveDto.getCountryName();
        String countryCode = countryTypeLanguageSaveDto.getCountryCode();

        // 插入国家表
        CountryLanguage baseCountryLanguage = new CountryLanguage();

        baseCountryLanguage.setCountryCode(countryCode);
        baseCountryLanguage.setCountryName(countryName);
        baseCountryLanguage.setEnableFlag(YesOrNoEnum.YES);
        baseCountryLanguage.setSingleLanguageFlag(YesOrNoEnum.NO);

        String code;
        boolean isNew = countryTypeLanguageSaveDto.getCode() == null;
        if (isNew) {
            // 如果走缓存,返回缓存的最大,否则为数据库最大
            int maxCountryLanguageCode;
            if (cache) {
                maxCountryLanguageCode = (int) RedisStaticFunUtils.incr(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(cache,"size"), 1);
            } else {
                maxCountryLanguageCode = Opt.ofNullable(this.findOneField(new BaseLambdaQueryWrapper<CountryLanguage>()
                        .eq(CountryLanguage::getSingleLanguageFlag, baseCountryLanguage.getSingleLanguageFlag())
                        .orderByDesc(CountryLanguage::getCodeIndex), CountryLanguage::getCodeIndex)).orElse(0) + 1;
            }
            baseCountryLanguage.setCodeIndex(maxCountryLanguageCode);
            // 还要检查是否存在预览数据
            String fill = maxCountryLanguageCode < 10 ? "0" : "";
            code = "GY" + fill + maxCountryLanguageCode;
        }else {
            code = countryTypeLanguageSaveDto.getCode();
            baseCountryLanguage.setCodeIndex(Integer.parseInt(code.replace("GY","")));
            // 判断国家是否正确
            LambdaQueryWrapper<CountryLanguage> queryWrapper = new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCode, code)
                    .eq(CountryLanguage::getCountryCode, countryCode);
            if (!this.exists(queryWrapper)) throw new OtherException("国家对应不上,请清理缓存");
        }
        baseCountryLanguage.setCode(code);

        // 查名字
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(DictBusinessConstant.LANGUAGE);

        saveLock.lock();
        try {
            countryTypeLanguageSaveDto.getTypeLanguage().stream().sorted(Comparator.comparing(it-> it.getType().ordinal())).forEach(typeLanguageSaveDto-> {
                CountryLanguageType type = typeLanguageSaveDto.getType();
                List<String> standardColumnCodeList = typeLanguageSaveDto.getStandardColumnCodeList();
                List<String> languageCodeList = typeLanguageSaveDto.getLanguageCodeList();

                CountryLanguage countryTypeLanguage = BeanUtil.copyProperties(baseCountryLanguage, CountryLanguage.class);
                countryTypeLanguage.setType(type);

                // 查找根
                String rootStandardColumnCode = standardColumnService.findOneField(new LambdaQueryWrapper<StandardColumn>()
                        .eq(StandardColumn::getType, type.getStandardColumnType()), StandardColumn::getCode);
                standardColumnCodeList.add(rootStandardColumnCode);
                standardColumnCodeList = CollUtil.distinct(standardColumnCodeList);

                LambdaQueryWrapper<CountryLanguage> queryWrapper = new LambdaQueryWrapper<CountryLanguage>()
                        .eq(CountryLanguage::getCountryCode, countryCode)
                        .eq(CountryLanguage::getType, type);

                // 检查是否已有国家表
                if (isNew && !cache && this.exists(queryWrapper)) {
                    throw new OtherException("已存在对应国家");
                }

                String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
                RedisStaticFunUtils.del(redisKey);

                if (CollectionUtil.isNotEmpty(languageCodeList)) {
                    List<CountryLanguage> oldCountryLanguageList = this.list(queryWrapper.clone().in(CountryLanguage::getLanguageCode, languageCodeList));

                    this.remove(queryWrapper.clone().notIn(CountryLanguage::getLanguageCode, languageCodeList));

                    for (String languageCode : languageCodeList) {
                        CountryLanguage countryLanguage = oldCountryLanguageList.stream().filter(it -> languageCode.equals(it.getLanguageCode())).findFirst().orElse(null);

                        if (countryLanguage == null) {
                            BasicBaseDict basicBaseDict = dictInfoToList.stream().filter(dict -> dict.getValue().equals(languageCode)).findFirst().orElse(new BasicBaseDict());
                            initLanguage(Arrays.asList(basicBaseDict));
                            countryLanguage = BeanUtil.copyProperties(countryTypeLanguage, CountryLanguage.class);
                            countryLanguage.setLanguageCode(languageCode);
                            countryLanguage.setLanguageName(basicBaseDict.getName());
                            this.save(countryLanguage);
                        }

                        String countryId = countryLanguage.getId();

                        RedisStaticFunUtils.set(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(cache, code, type.getCode(), countryId), countryLanguage);

                        removeRelation(countryId);

                        List<StandardColumnCountryRelation> countryRelationList = standardColumnCodeList.stream().skip(1).map(standardColumnCode -> {
                            // 从redis标准列数据
                            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
                            StandardColumn standardColumn = (StandardColumn)
                                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                            return new StandardColumnCountryRelation(countryId, standardColumn);
                        }).collect(Collectors.toList());

                        relationService.saveBatch(countryRelationList);
                    }

                    RedisStaticFunUtils.sSet(redisKey, standardColumnCodeList.stream().collect(Collectors.toList()));
                }
            });
        }finally {
            saveLock.unlock();
        }
        if (!cache) {
            cancelSave(code);
        }
//        // 使用redis作为中间通信,standardColumnCodeList参数暂时没用了
//        exportExcel(new MoreLanguageExcelQueryDto(countryId, new ArrayList<>()));
        RedisStaticFunUtils.clear();
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String review(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto) {
        //  复用增加
        String code = save(countryTypeLanguageSaveDto, true);
        // 强行报错回滚,让controller进行捕捉
        throw new RightException(code);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void removeRelation(String countryId){
        // 直接删除关联
        relationService.physicalDeleteQWrap(new BaseQueryWrapper<StandardColumnCountryRelation>().eq("country_language_id", countryId));
    }

    @Override
    public CountryTypeLanguageSaveDto detail(MoreLanguageQueryDto queryDto) {
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(queryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = this.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException("未查询到国家语言");

        CountryLanguage baseCountryLanguage = countryLanguageList.get(0);
        CountryTypeLanguageSaveDto countryTypeLanguageSaveDto = BeanUtil.copyProperties(baseCountryLanguage, CountryTypeLanguageSaveDto.class);

        countryTypeLanguageSaveDto.setTypeLanguage(Arrays.stream(CountryLanguageType.values()).map(type-> {
            TypeLanguageSaveDto typeLanguageSaveDto = new TypeLanguageSaveDto();
            typeLanguageSaveDto.setType(type);
            typeLanguageSaveDto.setStandardColumnCodeList(this.findStandardColumnCodeList(queryDto.getCode(), type, queryDto.isCache()));

            List<CountryLanguageDto> sameTypeCountryLanguageList = countryLanguageList.stream().filter(it -> it.getType().equals(type)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sameTypeCountryLanguageList)) {
                typeLanguageSaveDto.setLanguageCodeList(sameTypeCountryLanguageList.stream().map(CountryLanguageDto::getLanguageCode).distinct().collect(Collectors.toList()));
            }
            return typeLanguageSaveDto;
        }).collect(Collectors.toList()));
        return countryTypeLanguageSaveDto;
    }

    @Override
    public List<StandardColumn> findStandardColumnList(String code, CountryLanguageType type, boolean cache) {
        // 查询关联关系
        List<StandardColumn> standardColumnList = findStandardColumnCodeList(code, type, cache).stream().map(standardColumnCode -> {
            // 从redis标准列数据
            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
            return (StandardColumn) RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
        }).collect(Collectors.toList());
        RedisStaticFunUtils.clear();
        return standardColumnList;
    }

    @Override
    public List<String> findStandardColumnCodeList(String code, CountryLanguageType type, boolean cache) {
        // 查询关联关系
        String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
        Set<Object> tempStandardColumnCodeList = RedisStaticFunUtils.sGet(redisKey);
        if (CollectionUtil.isEmpty(tempStandardColumnCodeList)) {
            String countryLanguageId = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCode, code)
                    .eq(CountryLanguage::getType, type), CountryLanguage::getId
            );
            if (StrUtil.isBlank(countryLanguageId)) return new ArrayList<>();
            List<String> standardColumnCodeList = relationService.listOneField(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                    .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId), StandardColumnCountryRelation::getStandardColumnCode
            );
            RedisStaticFunUtils.sSet(redisKey, standardColumnCodeList.stream().collect(Collectors.toList()));
            return standardColumnCodeList;
        } else {
            return tempStandardColumnCodeList.stream().map(Object::toString).collect(Collectors.toList());
        }
    }

    @Override
    public String cancelSave(String code) {
        if (StrUtil.isBlank(code)) return "";

        // 删除缓存 countryLanguage
        RedisStaticFunUtils.removePattern(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(true, code));

        // 删除缓存 standardColumnRelation
        RedisStaticFunUtils.removePattern(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(true, code));
        return null;
    }

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Autowired
    private TransactionDefinition transactionDefinition;

    @Override
    public void initLanguage(List<BasicBaseDict> dictList) {
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            // 创建语言, type分组
            List<CountryLanguage> languageList = this.list(new LambdaQueryWrapper<CountryLanguage>()
                    .in(CountryLanguage::getLanguageCode, dictList.stream().map(BasicBaseDict::getValue).distinct().collect(Collectors.toList()))
                    .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.YES)
                    .eq(CountryLanguage::getEnableFlag, YesOrNoEnum.YES)
            );

            List<CountryLanguage> countryLanguageList = Arrays.stream(CountryLanguageType.values()).flatMap(type -> {
                Integer maxCode = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                        .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.YES)
                        .eq(CountryLanguage::getType, type)
                        .orderByDesc(CountryLanguage::getCodeIndex), CountryLanguage::getCodeIndex
                );

                AtomicInteger index = new AtomicInteger(maxCode == null ? 0 : maxCode);
                return dictList.stream().map(dict -> {
                    int codeIndex = index.incrementAndGet();
                    CountryLanguage countryLanguage = languageList.stream().filter(it -> it.getType().equals(type) && it.getLanguageCode().equals(dict.getValue()))
                            .findFirst().orElse(new CountryLanguage());
                    if (countryLanguage.getId() == null) {
                        countryLanguage.setCodeIndex(codeIndex);
                        countryLanguage.setCode("Y" + (countryLanguage.getCodeIndex() < 10 ? "0" + countryLanguage.getCodeIndex() : countryLanguage.getCodeIndex()));
                        countryLanguage.setLanguageCode(dict.getValue());
                        countryLanguage.setLanguageName(dict.getName());
                        countryLanguage.setEnableFlag(YesOrNoEnum.YES);
                        countryLanguage.setSingleLanguageFlag(YesOrNoEnum.YES);
                        countryLanguage.setType(type);
                    }
                    return countryLanguage;
                });
            }).collect(Collectors.toList());

            countryLanguageList.removeIf(countryLanguage -> languageList.stream().anyMatch(it-> it.getId().equals(countryLanguage.getId())));

            if (CollectionUtil.isNotEmpty(countryLanguageList)) {
                this.saveOrUpdateBatch(countryLanguageList);

                // 创建配置
                List<StandardColumnCountryRelation> relationList = new ArrayList<>();
                countryLanguageList.stream().collect(Collectors.groupingBy(CountryLanguage::getType)).forEach((type,sameTypeList)-> {
                    // 获取这个type下的所有属性
                    StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
                    List<StandardColumnType> childrenTypeList = new ArrayList<>(type.getStandardColumnType().getChildrenTypeList());
                    childrenTypeList.add(type.getStandardColumnType());
                    queryDto.setTypeList(childrenTypeList);
                    List<StandardColumnDto> standardColumnDtoList = standardColumnService.listQuery(queryDto);
                    standardColumnDtoList.forEach(standardColumnDto -> {
                        sameTypeList.forEach(countryLanguage -> {
                            StandardColumnCountryRelation relation = new StandardColumnCountryRelation();
                            relation.setCountryLanguageId(countryLanguage.getId());
                            relation.setStandardColumnCode(standardColumnDto.getCode());
                            relation.setStandardColumnName(standardColumnDto.getName());
                            relationList.add(relation);
                        });
                    });

                });

                relationService.saveOrUpdateBatch(relationList);
            }

            platformTransactionManager.commit(transaction);
        }catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            throw e;
        }
        // 要清除Redis? TODO

        // 创建翻译(使用翻译软件做基础翻译? TODO)
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
