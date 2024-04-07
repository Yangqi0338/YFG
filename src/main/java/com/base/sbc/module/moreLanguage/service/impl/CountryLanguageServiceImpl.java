/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
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
import com.base.sbc.module.moreLanguage.mapper.CountryLanguageMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.EXIST_COUNTRY_LANGUAGE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_COUNTRY_LANGUAGE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_STANDARD_CODE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.NOT_FOUND_COUNTRY_LANGUAGE;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

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

    public static final SFunction<CountryLanguage, String> languageCodeFunc = CountryLanguage::getLanguageCode;
    public static final SFunction<CountryLanguage, String> nameFunc = CountryLanguage::getName;
    public static final SFunction<CountryLanguage, String> codeFunc = CountryLanguage::getCode;
    public static final SFunction<CountryLanguage, Integer> codeIndexFunc = CountryLanguage::getCodeIndex;
    public static final SFunction<CountryLanguage, CountryLanguageType> typeFunc = CountryLanguage::getType;
    public static final SFunction<CountryLanguage, YesOrNoEnum> enableFlagFunc = CountryLanguage::getEnableFlag;
    public static final SFunction<CountryLanguage, YesOrNoEnum> singleLanguageFlagFunc = CountryLanguage::getSingleLanguageFlag;
    public static final SFunction<CountryLanguage, Integer> sortFunc = CountryLanguage::getSort;
    public static final SFunction<CountryLanguage, String> idFunc = CountryLanguage::getId;

    @Override
    public List<CountryLanguageDto> listQuery(CountryQueryDto countryQueryDto) {
        List<CountryLanguage> countryLanguageList;
        // 是否查缓存
        boolean cache = countryQueryDto.isCache();
        if (cache) {
            RedisKeyBuilder parentKeyBuilder = RedisKeyConstant.COUNTRY_LANGUAGE.add(cache, countryQueryDto.getCode());
            if (countryQueryDto.getType() != null) {
                parentKeyBuilder.add(countryQueryDto.getType().getCode() );
            }
            // 获取对应的redis键
            Set<String> keys = RedisStaticFunUtils.keys(parentKeyBuilder.build());
            countryLanguageList = keys.stream().map(key->  (CountryLanguage) RedisStaticFunUtils.get(key)).collect(Collectors.toList());
        }else {
            countryLanguageList = this.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                    .notEmptyIn(codeFunc, countryQueryDto.getCode())
                    .notNullEq(typeFunc, countryQueryDto.getType())
                    .notEmptyIn(languageCodeFunc, countryQueryDto.getLanguageCode())
                    .notEmptyLike(nameFunc, countryQueryDto.getName())
                    .notEmptyEq(enableFlagFunc, countryQueryDto.getEnableFlag())
                    .eq(singleLanguageFlagFunc, countryQueryDto.isSingleLanguage() ? YesOrNoEnum.YES : YesOrNoEnum.NO)
                    .orderByDesc(codeIndexFunc).orderByAsc(Arrays.asList(typeFunc, sortFunc))
            );
        }

        List<CountryLanguageDto> countryLanguageDtoList = MORE_LANGUAGE_CV.copyList2Dto(countryLanguageList);
        if (countryQueryDto.isDecorateLanguageName()) {
            List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(MoreLanguageProperties.languageDictCode);
            countryLanguageDtoList.forEach(countryLanguageDto -> {
                countryLanguageDto.buildLanguageName(dictInfoToList);
            });
        }
        return countryLanguageDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto, boolean cache) {
        String name = countryTypeLanguageSaveDto.getName();

        // 插入国家表
        CountryLanguage baseCountryLanguage = new CountryLanguage();

        baseCountryLanguage.setName(name);
        baseCountryLanguage.setEnableFlag(YesOrNoEnum.YES);
        baseCountryLanguage.setSingleLanguageFlag(YesOrNoEnum.NO);

        /* ----------------------------编码处理(缓存编码,新增编码,编码检查)---------------------------- */
        if (StrUtil.isBlank(countryTypeLanguageSaveDto.getCode())) {
            // 如果走缓存,返回缓存的最大,否则为数据库最大
            int maxCountryLanguageCode;
            if (cache) {
                maxCountryLanguageCode = (int) RedisStaticFunUtils.incr(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(cache,"size"), 1);
            } else {
                maxCountryLanguageCode = Opt.ofNullable(this.findOneField(new BaseLambdaQueryWrapper<CountryLanguage>()
                        .eq(singleLanguageFlagFunc, baseCountryLanguage.getSingleLanguageFlag())
                        .orderByDesc(codeIndexFunc), codeIndexFunc)).orElse(0) + 1;
            }
            baseCountryLanguage.setCodeIndex(maxCountryLanguageCode);
            // 还要检查是否存在预览数据
            countryTypeLanguageSaveDto.setCode(MoreLanguageProperties.countryLanguageCodePrefix + MoreLanguageProperties.getCountryZeroFill(maxCountryLanguageCode));
        }else {
            String code = countryTypeLanguageSaveDto.getCode();
            baseCountryLanguage.setCodeIndex(Integer.parseInt(code.replace(MoreLanguageProperties.countryLanguageCodePrefix,"")));
            // 判断国家是否正确
            LambdaQueryWrapper<CountryLanguage> queryWrapper = new LambdaQueryWrapper<CountryLanguage>()
                    .eq(codeFunc, code);
            if (!cache && !this.exists(queryWrapper)) throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_COUNTRY_LANGUAGE));
        }
        String code = countryTypeLanguageSaveDto.getCode();

        /* ----------------------------保存---------------------------- */

        List<CountryLanguage> countryLanguageList = this.list(new LambdaQueryWrapper<CountryLanguage>().eq(codeFunc, code));

        // 一个保存同步锁, 因为上面的code是程序计算, 解决连点导致的code重复
        saveLock.lock();
        try {
            List<CountryLanguage> newCountryLanguageList = new ArrayList<>();
            // 根据国家类型进行分组
            countryTypeLanguageSaveDto.getTypeLanguage().stream().sorted(CommonUtils.comparing(TypeLanguageSaveDto::getType)).forEach(typeLanguageSaveDto-> {
                CountryLanguageType type = typeLanguageSaveDto.getType();
                String modelLanguageCode = typeLanguageSaveDto.getModelLanguageCode();
                List<String> languageCodeList = typeLanguageSaveDto.getLanguageCodeList();
                boolean modelLanguageCodeBlank = StrUtil.isBlank(modelLanguageCode);
                if (type == CountryLanguageType.TAG && modelLanguageCodeBlank) throw new OtherException("未选择号型语言");

                // 深拷贝基础的国家语言实体类
                CountryLanguage countryTypeLanguage = MORE_LANGUAGE_CV.copyMyself(baseCountryLanguage);
                countryTypeLanguage.setType(type);

                // 检查是否已有国家表
                if (!cache && this.exists(new LambdaQueryWrapper<CountryLanguage>()
                        .eq(nameFunc, name)
                        .eq(typeFunc, type)
                        .ne(codeFunc, code)
                )) {
                    throw new OtherException(MoreLanguageProperties.getMsg(EXIST_COUNTRY_LANGUAGE));
                }

                if (CollectionUtil.isNotEmpty(languageCodeList)) {
                    // 获取已有数据在本次修改中已存在的语言列表
                    List<CountryLanguage> oldCountryLanguageList = new ArrayList<>();
                    countryLanguageList.stream().filter(it-> it.getType() == type)
                            .collect(Collectors.groupingBy(it-> !languageCodeList.contains(it.getLanguageCode()))).forEach((needDelete,list)-> {
                                if (needDelete) {
                                    // 删除掉本次修改中移除的语言
                                    this.removeByIds(list.stream().map(idFunc).collect(Collectors.toList()));
                                }else {
                                    oldCountryLanguageList.addAll(list);
                                }
                            });
                    // 计算语言的排序,用于回显
                    AtomicInteger sort = new AtomicInteger();
                    for (String languageCode : languageCodeList) {
                        CountryLanguage countryLanguage = oldCountryLanguageList.stream().filter(it -> languageCode.equals(it.getLanguageCode()))
                                .findFirst().orElse(MORE_LANGUAGE_CV.copyMyself(countryTypeLanguage));

                        countryLanguage.setName(name);
                        countryLanguage.setLanguageCode(languageCode);
                        countryLanguage.setModelLanguageCode(modelLanguageCodeBlank ? languageCode : modelLanguageCode);
                        countryLanguage.setSort(sort.getAndIncrement());

                        // 更新
                        newCountryLanguageList.add(countryLanguage);
                    }
                }
            });
            if (CollectionUtil.isNotEmpty(newCountryLanguageList)) {
                this.saveOrUpdateBatch(newCountryLanguageList);

                List<StandardColumn> rootStandardColumnList = standardColumnService.list(new LambdaQueryWrapper<StandardColumn>()
                        .select(StandardColumn::getCode, StandardColumn::getType)
                        .in(StandardColumn::getType, StandardColumnType.findRootList())
                );

                // 查询当前数据库的隐藏标准列
                List<String> notShowStandardColumnCodeList = standardColumnService.listOneField(
                        new BaseLambdaQueryWrapper<StandardColumn>().eq(StandardColumn::getShowFlag, YesOrNoEnum.NO), StandardColumn::getCode
                );

                Map<CountryLanguageType, List<String>> typeListMap = countryTypeLanguageSaveDto.getTypeLanguage()
                        .stream().collect(Collectors.toMap(TypeLanguageSaveDto::getType, (typeLanguageSaveDto)-> {
                            List<String> standardColumnCodeList = typeLanguageSaveDto.getStandardColumnCodeList();
                            rootStandardColumnList.stream().filter(it-> it.getType().equals(typeLanguageSaveDto.getType().getStandardColumnType())).findFirst().ifPresent(standardColumn -> {
                                standardColumnCodeList.add(standardColumn.getCode());
                            });
                            standardColumnCodeList.add(MoreLanguageProperties.modelStandardColumnCode);
                            standardColumnCodeList.addAll(notShowStandardColumnCodeList);
                            return CollUtil.distinct(standardColumnCodeList);
                        }));

                newCountryLanguageList.stream().collect(Collectors.groupingBy(typeFunc)).forEach((type, sameTypeList)-> {
                    // redis中获取关联关系
                    String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
                    RedisStaticFunUtils.del(redisKey);

                    // 查找根
                    List<String> standardColumnCodeList = typeListMap.get(type);

                    sameTypeList.forEach(countryLanguage-> {
                        String countryId = countryLanguage.getId();

                        // 设置缓存
                        RedisStaticFunUtils.set(RedisKeyConstant.COUNTRY_LANGUAGE.addEnd(cache, code, type.getCode(), countryId), countryLanguage);

                        // 重置关联
                        removeRelation(countryId);

                        List<StandardColumnCountryRelation> countryRelationList = standardColumnCodeList.stream().map(standardColumnCode -> {
                            // 从redis标准列数据
                            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage(MoreLanguageProperties.getMsg(INCORRECT_STANDARD_CODE));
                            StandardColumn standardColumn = (StandardColumn)
                                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                            return new StandardColumnCountryRelation(countryId, standardColumn);
                        }).collect(Collectors.toList());

                        relationService.saveBatch(countryRelationList);

                        RedisStaticFunUtils.sSet(redisKey, standardColumnCodeList.stream().collect(Collectors.toList()));
                    });
                });
            }
        }finally {
            saveLock.unlock();
        }
        if (!cache) {
            cancelSave(code);
        }
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
        List<CountryLanguageDto> countryLanguageList = this.listQuery(MORE_LANGUAGE_CV.copy2QueryDto(queryDto));
        if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));

        // 取其一为基础并深拷贝
        CountryLanguage baseCountryLanguage = countryLanguageList.get(0);
        CountryTypeLanguageSaveDto countryTypeLanguageSaveDto = MORE_LANGUAGE_CV.copy2SaveDto(baseCountryLanguage);

        // 规整对应的国家数据,按照国家类型分类
        countryTypeLanguageSaveDto.setTypeLanguage(Arrays.stream(CountryLanguageType.values()).map(type-> {
            List<CountryLanguageDto> languageDtoList = countryLanguageList.stream().filter(it -> it.getType().equals(type)).collect(Collectors.toList());
            TypeLanguageSaveDto typeLanguageSaveDto = new TypeLanguageSaveDto();
            typeLanguageSaveDto.setModelLanguageCode(languageDtoList.stream().findFirst().map(CountryLanguageDto::getModelLanguageCode).orElse(""));
            typeLanguageSaveDto.setType(type);
            typeLanguageSaveDto.setStandardColumnCodeList(this.findStandardColumnCodeList(queryDto.getCode(), type, queryDto.isCache()));
            typeLanguageSaveDto.setLanguageCodeList(languageDtoList.stream().map(CountryLanguageDto::getLanguageCode).distinct().collect(Collectors.toList())
            );
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
                    .eq(codeFunc, code)
                    .eq(typeFunc, type), idFunc
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
    public void initLanguage(List<BasicBaseDict> dictList) {
        if (CollUtil.isEmpty(dictList)) return;
        // 查看DB已存在的对应语言的数据
        List<CountryLanguage> languageList = this.list(new LambdaQueryWrapper<CountryLanguage>()
                .in(languageCodeFunc, dictList.stream().map(BasicBaseDict::getValue).distinct().collect(Collectors.toList()))
                .eq(singleLanguageFlagFunc, YesOrNoEnum.YES)
        );

        if (languageList.size() != dictList.size()) {
            // 手动处理事务,因为不能影响调用这个方法的事务
            TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
            try {
                List<CountryLanguage> countryLanguageList = Arrays.stream(CountryLanguageType.values()).flatMap(type -> {
                    // 找到单语言当前国家类型最大的编码
                    Integer maxCode = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                            .eq(singleLanguageFlagFunc, YesOrNoEnum.YES)
                            .eq(typeFunc, type)
                            .orderByDesc(codeIndexFunc), codeIndexFunc
                    );

                    AtomicInteger index = new AtomicInteger(maxCode == null ? 0 : maxCode);
                    return dictList.stream().map(dict -> {
                        int codeIndex = index.incrementAndGet();
                        // 有就更新,没有就new
                        CountryLanguage countryLanguage = languageList.stream().filter(it -> it.getType().equals(type) && it.getLanguageCode().equals(dict.getValue()))
                                .findFirst().orElse(new CountryLanguage());
                        if (StrUtil.isBlank(countryLanguage.getId())) {
                            countryLanguage.setCodeIndex(codeIndex);
                            countryLanguage.setCode(MoreLanguageProperties.languageCodePrefix + MoreLanguageProperties.getLanguageZeroFill(countryLanguage.getCodeIndex()));
                            countryLanguage.setName(dict.getValue() + " - " + dict.getValue()+"号型");
                            countryLanguage.setLanguageCode(dict.getValue());
                            countryLanguage.setModelLanguageCode(dict.getValue());
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
                    countryLanguageList.stream().collect(Collectors.groupingBy(typeFunc)).forEach((type,sameTypeList)-> {
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
        }
        // 创建翻译(使用翻译软件做基础翻译? TODO)
    }

    @Override
    public List<CountryDTO> getAllCountry(String code) {
        List<CountryLanguage> languageList = list(new BaseLambdaQueryWrapper<CountryLanguage>()
                .notEmptyIn(codeFunc, code)
                .eq(singleLanguageFlagFunc, YesOrNoEnum.NO));
        List<CountryDTO> result = new ArrayList<>();
        // 根据编码分组
        languageList.stream().collect(Collectors.groupingBy(codeFunc)).forEach((groupCode,sameCodeList)-> {
            CountryLanguage countryLanguage = sameCodeList.get(0);
            // 再根据类型分组
            result.add(new CountryDTO(groupCode, countryLanguage.getName(),
                    sameCodeList.stream().collect(Collectors.groupingBy(typeFunc,
                            Collectors.mapping(languageCodeFunc, Collectors.toList())))));
        });
        return result;
    }

    @Override
    public long getAllCountrySize() {
        return this.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                        .select(codeFunc)
                .eq(singleLanguageFlagFunc, YesOrNoEnum.NO)
                .groupBy(codeFunc)
        ).size();
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
