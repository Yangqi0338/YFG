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
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.google.common.collect.Maps;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.DictBusinessConstant;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.mapper.CountryLanguageMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.standard.dto.StandardColumnDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;

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
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private CcmFeignService ccmFeignService;

    private final ReentrantLock saveLock = new ReentrantLock();

    @Override
    public List<CountryLanguageDto> listQuery(CountryQueryDto countryQueryDto) {
        List<CountryLanguage> countryLanguageList;
        // 是否查缓存
        if (countryQueryDto.isCache()) {
            RedisKeyBuilder parentKeyBuilder = RedisKeyConstant.COUNTRY_LANGUAGE.add(true, countryQueryDto.getCode());
            if (countryQueryDto.getType() != null) {
                parentKeyBuilder.add(countryQueryDto.getType().getCode() );
            }
            // 获取对应的redis键
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
                    .orderByDesc(CountryLanguage::getCodeIndex).orderByAsc(Arrays.asList(CountryLanguage::getType, CountryLanguage::getSort))
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

        /* ----------------------------编码处理(缓存编码,新增编码,编码检查)---------------------------- */
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
            code = MoreLanguageProperties.countryLanguageCodePrefix + MoreLanguageProperties.getCountryZeroFill(maxCountryLanguageCode);
        }else {
            code = countryTypeLanguageSaveDto.getCode();
            baseCountryLanguage.setCodeIndex(Integer.parseInt(code.replace(MoreLanguageProperties.countryLanguageCodePrefix,"")));
            // 判断国家是否正确
            LambdaQueryWrapper<CountryLanguage> queryWrapper = new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCode, code)
                    .eq(CountryLanguage::getCountryCode, countryCode);
            if (!cache && !this.exists(queryWrapper)) throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_COUNTRY_LANGUAGE));
        }
        baseCountryLanguage.setCode(code);

        /* ----------------------------保存---------------------------- */

        // 查名字
        List<BasicBaseDict> dictInfoToList = new ArrayList<>();
        // 查询当前数据库的隐藏标准列
        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        List<StandardColumnDto> notShowStandardColumnList = standardColumnService.listQuery(queryDto);

        // 一个保存同步锁, 因为上面的code是程序计算, 解决连点导致的code重复
        saveLock.lock();
        try {
            List<StandardColumnCountryTranslate> needInsertTranslateList = new ArrayList<>();
            // 根据国家类型进行分组
            countryTypeLanguageSaveDto.getTypeLanguage().stream().sorted(CommonUtils.comparing(TypeLanguageSaveDto::getType)).forEach(typeLanguageSaveDto-> {
                CountryLanguageType type = typeLanguageSaveDto.getType();
                List<String> standardColumnCodeList = typeLanguageSaveDto.getStandardColumnCodeList();
                List<String> languageCodeList = typeLanguageSaveDto.getLanguageCodeList();

                // 深拷贝基础的国家语言实体类
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
                    throw new OtherException(MoreLanguageProperties.getMsg(EXIST_COUNTRY_LANGUAGE));
                }

                // redis中获取关联关系
                String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
                RedisStaticFunUtils.del(redisKey);

                if (CollectionUtil.isNotEmpty(languageCodeList)) {
                    // 获取已有数据在本次修改中已存在的语言列表
                    List<CountryLanguage> oldCountryLanguageList = this.list(queryWrapper.clone().in(CountryLanguage::getLanguageCode, languageCodeList));
                    // 删除掉本次修改中移除的语言
                    this.remove(queryWrapper.clone().notIn(CountryLanguage::getLanguageCode, languageCodeList));

                    // 计算语言的排序,用于回显
                    AtomicInteger sort = new AtomicInteger();
                    for (String languageCode : languageCodeList) {
                        Optional<CountryLanguage> countryLanguageOpt = oldCountryLanguageList.stream().filter(it -> languageCode.equals(it.getLanguageCode())).findFirst();
                        CountryLanguage countryLanguage = countryLanguageOpt.orElse(BeanUtil.copyProperties(countryTypeLanguage, CountryLanguage.class));

                        countryLanguage.setLanguageCode(languageCode);
                        countryLanguage.setSort(sort.getAndIncrement());
                        // 查找语言名字,并尝试初始化语言对应的语言实体类
                        BasicBaseDict basicBaseDict = dictInfoToList.stream().filter(dict -> dict.getValue().equals(languageCode)).findFirst().orElse(new BasicBaseDict());
                        initLanguage(Collections.singletonList(basicBaseDict));
                        countryLanguage.setLanguageName(basicBaseDict.getName());

                        // 更新
                        this.saveOrUpdate(countryLanguage);

                        String countryId = countryLanguage.getId();
                        // 设置缓存
                        RedisStaticFunUtils.set(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(cache, code, type.getCode(), countryId), countryLanguage);

                        // 重置关联
                        List<String> existRelationStandardColumnList = relationService.listOneField(new LambdaQueryWrapper<StandardColumnCountryRelation>()
                                .eq(StandardColumnCountryRelation::getCountryLanguageId, countryId), StandardColumnCountryRelation::getStandardColumnCode
                        );
                        List<String> handlerStandardColumnCodeList = notShowStandardColumnList.stream().filter(it -> it.getType().equals(type.getStandardColumnType()))
                                .map(StandardColumn::getCode).filter(existRelationStandardColumnList::contains).collect(Collectors.toList());
                        removeRelation(countryId);

                        handlerStandardColumnCodeList.addAll(standardColumnCodeList);
                        List<StandardColumnCountryRelation> countryRelationList = handlerStandardColumnCodeList.stream().map(standardColumnCode -> {
                            // 从redis标准列数据
                            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage(MoreLanguageProperties.getMsg(INCORRECT_STANDARD_CODE));
                            StandardColumn standardColumn = (StandardColumn)
                                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                            return new StandardColumnCountryRelation(countryId, standardColumn);
                        }).collect(Collectors.toList());

                        relationService.saveBatch(countryRelationList);

                        List<String> newRelationList = handlerStandardColumnCodeList.stream().filter(it -> !existRelationStandardColumnList.contains(it)).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(newRelationList) && !cache) {
                            if (CollUtil.isEmpty(dictInfoToList)) {
                                dictInfoToList.addAll(ccmFeignService.getDictInfoToList(DictBusinessConstant.LANGUAGE));
                            }
                            // 检查新增是否有单语言翻译, 拿过来
                            String languageId = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                                            .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.YES)
                                            .eq(CountryLanguage::getLanguageCode, languageCode)
                                            .eq(CountryLanguage::getType, type)
                                    , CountryLanguage::getId);
                            if (StrUtil.isNotBlank(languageId)) {
                                List<StandardColumnCountryTranslate> contentList = standardColumnCountryTranslateService.list(
                                        new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                                                .eq(StandardColumnCountryTranslate::getCountryLanguageId, languageId)
                                                .in(StandardColumnCountryTranslate::getTitleCode, newRelationList)
                                );
                                contentList.addAll(standardColumnCountryTranslateService.list(
                                        new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                                                .eq(StandardColumnCountryTranslate::getCountryLanguageId, languageId)
                                                .in(StandardColumnCountryTranslate::getPropertiesCode, newRelationList)
                                ));
                                if (CollectionUtil.isNotEmpty(contentList)) {
                                    contentList.forEach(it-> {
                                        it.setId(null);
                                        it.setCountryLanguageId(countryId);
                                    });
                                    needInsertTranslateList.addAll(contentList);
                                }
                            }
                        }
                        RedisStaticFunUtils.sSet(redisKey, handlerStandardColumnCodeList.stream().collect(Collectors.toList()));
                    }

                }
            });
            if (CollectionUtil.isNotEmpty(needInsertTranslateList)) {
                standardColumnCountryTranslateService.saveOrUpdateBatch(needInsertTranslateList);
            }
        }finally {
            saveLock.unlock();
        }
        if (!cache) {
            cancelSave(code);
        }
//        exportExcel(new MoreLanguageExcelQueryDto(countryId, new ArrayList<>()));
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String review(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto) {
        // 复用添加,cache为true
        String code = save(countryTypeLanguageSaveDto, true);
        // 强行报错回滚,为了将上面复用产生的DB数据回滚,但缓存已设置值,后续只要不点完成都会访问这个缓存值
        throw new RightException(code);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void removeRelation(String countryId){
        // 直接删除关联
        relationService.physicalDeleteQWrap(new BaseQueryWrapper<StandardColumnCountryRelation>().eq("country_language_id", countryId));
    }

    @Override
    public CountryTypeLanguageSaveDto detail(MoreLanguageQueryDto queryDto) {
        // 查询存在的多语言国家数据
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(queryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = this.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));

        // 取其一为基础并深拷贝
        CountryLanguage baseCountryLanguage = countryLanguageList.get(0);
        CountryTypeLanguageSaveDto countryTypeLanguageSaveDto = BeanUtil.copyProperties(baseCountryLanguage, CountryTypeLanguageSaveDto.class);

        // 规整对应的国家数据,按照国家类型分类
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
        // 查询关联关系,并通过关联关系查询缓存中的标准列数据
        return findStandardColumnCodeList(code, type, cache).stream().map(standardColumnCode ->
            MoreLanguageProperties.getStandardColumn(standardColumnService, type, standardColumnCode)
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<String> findStandardColumnCodeList(String code, CountryLanguageType type, boolean cache) {
        // 查询关联关系
        String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
        Set<Object> tempStandardColumnCodeList = RedisStaticFunUtils.sGet(redisKey);
        if (CollectionUtil.isEmpty(tempStandardColumnCodeList)) {
            // 查询国家
            String countryLanguageId = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCode, code)
                    .eq(CountryLanguage::getType, type), CountryLanguage::getId
            );
            if (StrUtil.isBlank(countryLanguageId)) return new ArrayList<>();
            // 查询关联
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
    @Transactional(rollbackFor = Exception.class)
    public void initLanguage(List<BasicBaseDict> dictList) {
        // 手动处理事务,因为不能影响调用这个方法的事务
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            // 查看DB已存在的对应语言的数据
            List<CountryLanguage> languageList = this.list(new LambdaQueryWrapper<CountryLanguage>()
                    .in(CountryLanguage::getLanguageCode, dictList.stream().map(BasicBaseDict::getValue).distinct().collect(Collectors.toList()))
                    .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.YES)
            );

            List<CountryLanguage> countryLanguageList = Arrays.stream(CountryLanguageType.values()).flatMap(type -> {
                // 找到单语言当前国家类型最大的编码
                Integer maxCode = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                        .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.YES)
                        .eq(CountryLanguage::getType, type)
                        .orderByDesc(CountryLanguage::getCodeIndex), CountryLanguage::getCodeIndex
                );

                AtomicInteger index = new AtomicInteger(maxCode == null ? 0 : maxCode);
                return dictList.stream().map(dict -> {
                    int codeIndex = index.incrementAndGet();
                    // 有就更新,没有就new
                    CountryLanguage countryLanguage = languageList.stream().filter(it -> it.getType().equals(type) && it.getLanguageCode().equals(dict.getValue()))
                            .findFirst().orElse(new CountryLanguage());
                    if (countryLanguage.getId() == null) {
                        countryLanguage.setCodeIndex(codeIndex);
                        countryLanguage.setCode(MoreLanguageProperties.languageCodePrefix + MoreLanguageProperties.getLanguageZeroFill(countryLanguage.getCodeIndex()));
                        countryLanguage.setLanguageCode(dict.getValue());
                        countryLanguage.setLanguageName(dict.getName());
                        countryLanguage.setEnableFlag(YesOrNoEnum.YES);
                        countryLanguage.setSingleLanguageFlag(YesOrNoEnum.YES);
                        countryLanguage.setType(type);
                    }
                    return countryLanguage;
                });
            }).collect(Collectors.toList());

            // 不需要更新,去除
            countryLanguageList.removeIf(countryLanguage -> languageList.stream().anyMatch(it-> it.getId().equals(countryLanguage.getId())));

            if (CollectionUtil.isNotEmpty(countryLanguageList)) {
                this.saveOrUpdateBatch(countryLanguageList);

                // 创建配置
                List<StandardColumnCountryRelation> relationList = new ArrayList<>();
                // 创建全量的关联
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

    @Override
    public List<CountryDTO> getAllCountry(String code) {
        List<CountryLanguage> languageList = list(new BaseLambdaQueryWrapper<CountryLanguage>()
                .notEmptyIn(CountryLanguage::getCode, code)
                .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.NO));
        List<CountryDTO> result = new ArrayList<>();
        // 根据编码分组
        languageList.stream().collect(Collectors.groupingBy(CountryLanguage::getCode)).forEach((groupCode,sameCodeList)-> {
            CountryLanguage countryLanguage = sameCodeList.get(0);
            // 再根据类型分组
            result.add(new CountryDTO(groupCode, countryLanguage.getCountryCode(), countryLanguage.getCountryName(),
                    sameCodeList.stream().collect(Collectors.groupingBy(CountryLanguage::getType,
                            Collectors.mapping(CountryLanguage::getLanguageCode, Collectors.toList())))));
        });
        return result;
    }

    @Override
    public long getAllCountrySize() {
        return this.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                        .select(CountryLanguage::getCode)
                .eq(CountryLanguage::getSingleLanguageFlag, YesOrNoEnum.NO)
                .groupBy(CountryLanguage::getCode)
        ).size();
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
