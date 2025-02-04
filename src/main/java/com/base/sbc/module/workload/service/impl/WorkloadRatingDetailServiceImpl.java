/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingTitleFieldDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.mapper.WorkloadRatingDetailMapper;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.service.WorkloadRatingDetailService;
import com.base.sbc.module.workload.service.WorkloadRatingItemService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailQO;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailSaveDTO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.WORKLOAD_CV;

/**
 * 类描述：工作量评分数据计算结果 service类
 * @address com.base.sbc.module.workload.service.WorkloadRatingDetailService
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Service
public class WorkloadRatingDetailServiceImpl extends BaseServiceImpl<WorkloadRatingDetailMapper, WorkloadRatingDetail> implements WorkloadRatingDetailService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private WorkloadRatingItemService workloadRatingItemService;

    @Autowired
    private WorkloadRatingConfigService workloadRatingConfigService;

    private static String proxyField = "[proxy]";

    @Override
    public List<WorkloadRatingDetailDTO> queryList(WorkloadRatingDetailQO qo) {
        BaseLambdaQueryWrapper<WorkloadRatingDetail> qw = buildQueryWrapper(qo);
        List<WorkloadRatingDetail> list = this.list(qw);
        return WORKLOAD_CV.copy2DetailDTO(list);
    }

    private BaseLambdaQueryWrapper<WorkloadRatingDetail> buildQueryWrapper(WorkloadRatingDetailQO qo) {
        BaseLambdaQueryWrapper<WorkloadRatingDetail> qw = new BaseLambdaQueryWrapper<>();
        qw.notEmptyIn(WorkloadRatingDetail::getId, qo.getIds());
        return qw;
    }

    @Override
    public void save(@NotNull WorkloadRatingDetailDTO workloadRatingDetail) {
        workloadRatingDetail.setId(null);
        workloadRatingDetail.setResult(BigDecimal.ZERO);

        WorkloadRatingConfigQO configQO = new WorkloadRatingConfigQO();
        configQO.setSearchValue(false);
        configQO.setType(WorkloadRatingType.SAMPLE);
        configQO.setBrand(workloadRatingDetail.getBrand());
        configQO.setIsConfigShow(YesOrNoEnum.YES);
        List<WorkloadRatingConfigVO> configVOList = workloadRatingConfigService.queryList(configQO);

        for (WorkloadRatingCalculateType calculateType : WorkloadRatingCalculateType.values()) {
            configVOList.stream().sorted(Comparator.comparing(WorkloadRatingConfigVO::getIndex)).forEach(configVO -> {
                List<WorkloadRatingTitleFieldDTO> configTitleFieldList = configVO.findConfigTitleFieldList();

                List<WorkloadRatingDetailSaveDTO> configList = workloadRatingDetail.getConfigList().stream()
                        .filter(it -> configTitleFieldList.stream().anyMatch(titleFieldDTO -> titleFieldDTO.getConfigId().equals(it.getConfigId())))
                        .collect(Collectors.toList());

                String itemValue = configList.stream().filter(it -> it.getConfigId().equals(configVO.getId()))
                        .findFirst().map(WorkloadRatingDetailSaveDTO::getItemValue)
                        .orElseThrow(() -> new OtherException("!基础项数据有误,请刷新页面重试!"));

                boolean proxy = (configVO.getCalculateType() == WorkloadRatingCalculateType.BASE && StrUtil.isNotBlank(workloadRatingDetail.getProxyKey()));
                configList.stream().filter(it -> it.getCalculateType().equals(calculateType)).forEach(config -> {
                    List<String> itemValueList = StrUtil.split(itemValue, COMMA);
                    List<WorkloadRatingItem> ratingItemList = workloadRatingItemService.list(new LambdaQueryWrapper<WorkloadRatingItem>()
                            .eq(WorkloadRatingItem::getConfigId, config.getConfigId())
                            .in(WorkloadRatingItem::getItemValue, itemValueList)
                    );
                    Collection<String> disjunctionItemValueList = CollUtil.disjunction(ratingItemList.stream().map(WorkloadRatingItem::getItemValue).distinct().collect(Collectors.toList()), itemValueList);
                    if (calculateType != WorkloadRatingCalculateType.APPEND && CollUtil.isNotEmpty(disjunctionItemValueList))
                        throw new OtherException(String.format("%s-%s项未全部找到,无法计算,请联系管理员添加", config.getConfigName(), config.getItemName()));
                    config.setItemValue(itemValue);
                    if (proxy && BigDecimalUtil.biggerThenZero(config.getScore())) {
                        ratingItemList.forEach(it -> it.setScore(config.getScore()));
                        workloadRatingDetail.getExtend().put(config.getConfigId() + proxyField, config.getScore());
                    }
                    BigDecimal score = BigDecimal.ZERO;
                    for (WorkloadRatingItem ratingItem : ratingItemList) {
                        if (config.getEnableFlag() == YesOrNoEnum.YES) {
                            Pair<BigDecimal, BigDecimal> calculatePair = calculateType.calculate(workloadRatingDetail.getResult(), ratingItem.getScore());
                            score = score.add(calculatePair.getValue());
                            workloadRatingDetail.setResult(calculatePair.getKey());
                        }
                    }
                    if("其他分".equals(config.getConfigName()) || "配饰分".equals(config.getConfigName())){
                        score = config.getScore();
                        config.setCalculateType(WorkloadRatingCalculateType.APPEND);
                        workloadRatingDetail.setResult(workloadRatingDetail.getResult().add(score));
                    }
                    config.setScore(score);
                    workloadRatingDetail.getExtend().put(config.getConfigName(), config.getScore());
                    config.setItemId(ratingItemList.stream().map(WorkloadRatingItem::getId).collect(Collectors.joining(COMMA)));
                });
            });
        }
        workloadRatingDetail.getExtend().put("workloadRatingRemark", workloadRatingDetail.getWorkloadRatingRemark());

        String itemValue = workloadRatingDetail.getItemValue();
        // type brand itemValue 唯一
        this.defaultValue(WORKLOAD_CV.copy2Entity(workloadRatingDetail));
        WorkloadRatingDetail entity = this.findOne(new LambdaQueryWrapper<WorkloadRatingDetail>()
                .eq(WorkloadRatingDetail::getType, workloadRatingDetail.getType())
                .eq(WorkloadRatingDetail::getBrand, workloadRatingDetail.getBrand())
                .eq(WorkloadRatingDetail::getItemValue, itemValue)
        );
        WORKLOAD_CV.copy(entity, workloadRatingDetail);
        this.saveOrUpdate(entity);
        WORKLOAD_CV.copy(workloadRatingDetail, entity);
    }

    @Override
    public <T extends BaseEntity> void decorateWorkloadRating(List<T> list,
                                                              Function<T, Style> styleFunc,
                                                              Function<T, String> workloadRatingIdFunc,
                                                              BiConsumer<T, String> prodCategoryFunc,
                                                              BiConsumer<T, WorkloadRatingDetailDTO> resultKeyFunc,
                                                              BiConsumer<T, List<WorkloadRatingConfigVO>> resultValueFunc
    ) {
        if (CollUtil.isEmpty(list)) return;

        WorkloadRatingConfigQO configQO = new WorkloadRatingConfigQO();
        configQO.setNotSearch(Collections.singletonList(WorkloadRatingItemType.STRUCTURE));
        configQO.setIsConfigShow(YesOrNoEnum.YES);
        configQO.setType(WorkloadRatingType.SAMPLE);
        configQO.setBrand(StrUtil.join(COMMA, list.stream().map(styleFunc).map(Style::getBrand).collect(Collectors.toList())));
        List<WorkloadRatingConfigVO> configVOList = workloadRatingConfigService.queryList(configQO);

        // 根据是否有样衣工工作量来分组
        list.stream().collect(CommonUtils.groupNotBlank(workloadRatingIdFunc)).forEach((isExists, existsOrNotList) -> {
            List<WorkloadRatingDetailDTO> workloadRatingDetailList = new ArrayList<>();
            List<WorkloadRatingItemVO> workloadRatingItemList = new ArrayList<>();
            if (isExists) {
                WorkloadRatingDetailQO ratingDetailQO = new WorkloadRatingDetailQO();
                ratingDetailQO.setIds(existsOrNotList.stream().map(workloadRatingIdFunc).collect(Collectors.toList()));
                List<WorkloadRatingDetailDTO> ratingDetailDTOS = this.queryList(ratingDetailQO);
                // 手动构建ratingItemList
                ratingDetailDTOS.forEach(ratingDetail -> {
                    List<WorkloadRatingItemVO> itemList = WORKLOAD_CV.copy2ItemVO(workloadRatingItemService.listByIds(StrUtil.split(ratingDetail.getOriginItemId(), COMMA)));
                    List<String> itemValueList = StrUtil.split(ratingDetail.getOriginItemValue(), "-");
                    itemValueList.forEach(itemValue-> {
                        // 找config
                        List<String> keyList = StrUtil.split(itemValue, "#");
                        itemList.stream().filter(it-> it.getConfigName().equals(keyList.get(0))).forEach(it-> {
                            YesOrNoEnum enableFlag = null;
                            if (keyList.size() >= 2) {
                                enableFlag = YesOrNoEnum.findByValue(keyList.get(2));
                            }
                            it.setEnableFlag(Opt.ofNullable(enableFlag).orElse(YesOrNoEnum.YES));
                        });
                    });
                    configVOList.forEach(configVO -> {
                        workloadRatingItemList.addAll(itemList.stream().filter(it -> it.getConfigId().equals(configVO.getId())).peek(item -> {
                            item.setItemList(configVO.getTitleFieldDTOList().stream().flatMap(configTitle ->
                                    itemList.stream().filter(it -> it.getConfigId().equals(configTitle.getConfigId())).peek(it -> {
                                        String proxyFieldKey = it.getConfigId() + proxyField;
                                        if (ratingDetail.getExtend().containsKey(proxyFieldKey)) {
                                            it.setScore(new BigDecimal(ratingDetail.getExtend().get(proxyFieldKey).toString()));
                                        }
                                    })).collect(Collectors.toList()));
                        }).collect(Collectors.toList()));
                    });
                });
                workloadRatingDetailList.addAll(ratingDetailDTOS);
            } else {
                configVOList.stream().filter(it-> it.getCalculateType() == WorkloadRatingCalculateType.BASE).forEach(configVO -> {
                    WorkloadRatingItemQO qo = new WorkloadRatingItemQO();
                    qo.reset2QueryList();
                    qo.setConfigId(configVO.getId());
                    workloadRatingItemList.addAll(workloadRatingItemService.queryPageInfo(qo).getList());
                });
            }
            workloadRatingItemList.stream().filter(it -> it.getCalculateType() == WorkloadRatingCalculateType.BASE).forEach(it -> it.setEnableFlag(YesOrNoEnum.YES));
            for (T vo : existsOrNotList) {
                Style style = styleFunc.apply(vo);
                String workloadRatingId = workloadRatingIdFunc.apply(vo);
                if (style == null) continue;

                String brand = style.getBrand();
                List<String> matchItemValueList = new ArrayList<>();
                structureValueList(matchItemValueList, "/", style.getProdCategory1st(), style.getProdCategory(), style.getProdCategory2nd(), style.getProdCategory3rd());
                List<WorkloadRatingConfigVO> brandConfigVOList = configVOList.stream().filter(it -> it.getBrand().equals(brand)).collect(Collectors.toList());

                WorkloadRatingDetailDTO detailDTO = workloadRatingDetailList.stream()
                        .filter(it -> it.getId().equals(workloadRatingId))
                        .findFirst().orElseGet(() -> {
                            WorkloadRatingDetailDTO newDetailDTO = new WorkloadRatingDetailDTO();
                            newDetailDTO.setType(WorkloadRatingType.SAMPLE);
                            newDetailDTO.setBrand(brand);
                            return newDetailDTO;
                        });

                List<WorkloadRatingDetailSaveDTO> saveDTOList = brandConfigVOList.stream().flatMap(configVO -> {
                    List<WorkloadRatingItemVO> configItemList = workloadRatingItemList.stream()
                            .filter(it -> it.getConfigName().equals(configVO.getItemName()))
                            .filter(it -> it.getBrand().equals(configVO.getBrand()))
                            .filter(it -> it.getCalculateType().equals(configVO.getCalculateType()))
                            .collect(Collectors.toList());

                    Optional<String> itemValueOpt = matchItemValueList.stream()
                            .filter(itemValue -> configItemList.stream().anyMatch(it -> it.getItemValue().equals(itemValue))).findFirst();

                    return configVO.findConfigTitleFieldList().stream().map(configTitleField-> {
                        WorkloadRatingDetailSaveDTO detailSaveDTO = new WorkloadRatingDetailSaveDTO().decorateConfig(configTitleField);
                        String detailConfigId = detailSaveDTO.getConfigId();
                        detailSaveDTO.setEnableFlag(YesOrNoEnum.YES);
                        if (configVO.getCalculateType() != WorkloadRatingCalculateType.BASE || itemValueOpt.isPresent()) {
                            List<WorkloadRatingItemVO> configFieldItemList = configItemList.stream()
                                    .filter(it -> !itemValueOpt.isPresent() || it.getItemValue().equals(itemValueOpt.get()))
                                    .collect(Collectors.toList());

                            if (!configVO.getId().equals(detailConfigId)) {
                                configFieldItemList = configFieldItemList.stream()
                                        .flatMap(it -> it.getItemList().stream())
                                        .filter(it -> it.getConfigId().equals(detailConfigId))
                                        // 新增时设置成null
                                        .peek(it -> {
                                            if (!isExists) it.setEnableFlag(null);
                                        })
                                        .collect(Collectors.toList());
                            }
                            configFieldItemList.forEach(detailSaveDTO::decorateItem);
                        }
                        return detailSaveDTO;
                    });
                }).collect(Collectors.toList());

                String prodCategory = saveDTOList.stream()
                        .filter(it-> "品类".equals(it.getConfigName()) && StrUtil.isNotBlank(it.getItemId()))
                        .findFirst().map(WorkloadRatingDetailSaveDTO::getItemName)
                        .orElse(String.format("未找到当前品类%s的评分数据", CommonUtils.strJoin("/",
                                style.getProdCategory1stName(), style.getProdCategoryName(), style.getProdCategory2ndName(), style.getProdCategory3rdName())));
                prodCategoryFunc.accept(vo, prodCategory);
                detailDTO.setConfigList(saveDTOList);

                resultKeyFunc.accept(vo, detailDTO);
                resultValueFunc.accept(vo, brandConfigVOList);
            }
        });
    }

    private void structureValueList(List<String> resultList, String delimiter, String... args) {
        if (args.length == 0) return;
        resultList.add(StrUtil.join(delimiter, args));
        structureValueList(resultList, delimiter, ArrayUtil.remove(args, args.length - 1));
    }

// 自定义方法区 不替换的区域【other_end】

}
