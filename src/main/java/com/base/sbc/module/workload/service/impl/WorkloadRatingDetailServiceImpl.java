/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private StyleService styleService;

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
    public void save(WorkloadRatingDetailDTO workloadRatingDetail) {
        workloadRatingDetail.setId(null);
        List<WorkloadRatingDetailSaveDTO> configList = workloadRatingDetail.getConfigList();
        configList.stream().sorted(Comparator.comparing(WorkloadRatingDetailSaveDTO::getIndex)).forEach(config -> {
            WorkloadRatingCalculateType calculateType = config.getCalculateType();
            if (calculateType != WorkloadRatingCalculateType.APPEND)
                workloadRatingItemService.warnMsg(String.format("未找到%s-%s项,无法计算,请联系管理员添加", config.getConfigName(), config.getItemValue()));
            workloadRatingItemService.defaultValue(new WorkloadRatingItem());
            WorkloadRatingItem ratingItem = workloadRatingItemService.findOne(new LambdaQueryWrapper<WorkloadRatingItem>()
                    .eq(WorkloadRatingItem::getConfigId, config.getConfigId())
                    .eq(WorkloadRatingItem::getItemValue, config.getItemValue())
            );
            config.setScore(ratingItem.getScore());
            config.setItemId(ratingItem.getId());
            workloadRatingDetail.setResult(calculateType.calculate(null, config.getScore()).getKey());
        });

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
        workloadRatingDetail.setId(entity.getId());
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
        configQO.setType(WorkloadRatingType.SAMPLE);
        configQO.setBrand(StrUtil.join(COMMA, list.stream().map(styleFunc).map(Style::getBrand).collect(Collectors.toList())));
        List<WorkloadRatingConfigVO> configVOList = workloadRatingConfigService.queryList(configQO);

        List<WorkloadRatingConfigVO> appendConfigList = CollUtil.removeWithAddIf(configVOList, (it) -> it.getCalculateType() == WorkloadRatingCalculateType.APPEND);

        list.stream().collect(CommonUtils.groupNotBlank(workloadRatingIdFunc)).forEach((isExists, existsOrNotList) -> {
            List<WorkloadRatingDetailDTO> workloadRatingDetailList = new ArrayList<>();
            List<WorkloadRatingItem> workloadRatingItemList = new ArrayList<>();
            if (isExists) {
                WorkloadRatingDetailQO ratingDetailQO = new WorkloadRatingDetailQO();
                ratingDetailQO.setIds(existsOrNotList.stream().map(workloadRatingIdFunc).collect(Collectors.toList()));
                workloadRatingDetailList.addAll(this.queryList(ratingDetailQO));
            } else {
                configVOList.forEach(configVO -> {
                    WorkloadRatingItemQO qo = new WorkloadRatingItemQO();
                    qo.reset2QueryList();
                    qo.setConfigId(configVO.getId());
                    workloadRatingItemList.addAll(workloadRatingItemService.queryPageInfo(qo).getList());
                });
            }
            for (T vo : existsOrNotList) {
                Style style = styleFunc.apply(vo);
                String workloadRatingId = workloadRatingIdFunc.apply(vo);
                if (style == null) continue;

                String brand = style.getBrand();
                List<String> matchItemValueList = new ArrayList<>();
                structureValueList(matchItemValueList, "/", style.getProdCategory1st(), style.getProdCategory(), style.getProdCategory2nd(), style.getProdCategory3rd());

                WorkloadRatingDetailDTO detailDTO = workloadRatingDetailList.stream()
                        .filter(it -> it.getId().equals(workloadRatingId))
                        .findFirst().orElseGet(() -> {
                            WorkloadRatingDetailDTO newDetailDTO = new WorkloadRatingDetailDTO();
                            newDetailDTO.setType(WorkloadRatingType.SAMPLE);
                            newDetailDTO.setBrand(brand);
                            List<WorkloadRatingDetailSaveDTO> saveDTOList = configVOList.stream().map(configVO -> {
                                String itemName = configVO.getItemName();
                                WorkloadRatingDetailSaveDTO detailSaveDTO = new WorkloadRatingDetailSaveDTO().decorateConfig(configVO);
                                List<WorkloadRatingItem> configItemList = workloadRatingItemList.stream()
                                        .filter(it -> it.getConfigName().equals(itemName))
                                        .collect(Collectors.toList());
                                if ("C8_品类".equals(configVO.getTitleDictKey())) {
                                    prodCategoryFunc.accept(vo, itemName);
                                }
                                Optional<String> itemValueOpt = matchItemValueList.stream()
                                        .filter(itemValue -> configItemList.stream().anyMatch(it -> it.getItemValue().equals(itemValue))).findFirst();
                                if (itemValueOpt.isPresent()) {
                                    configItemList.stream().filter(it -> it.getItemValue().equals(itemValueOpt.get())).findFirst().ifPresent(detailSaveDTO::decorateItem);
                                }
                                return detailSaveDTO;
                            }).collect(Collectors.toList());
                            appendConfigList.stream().map(it -> new WorkloadRatingDetailSaveDTO().decorateConfig(it)).forEach(saveDTOList::add);
                            newDetailDTO.setConfigList(saveDTOList);
                            return newDetailDTO;
                        });

                resultKeyFunc.accept(vo, detailDTO);
                resultValueFunc.accept(vo, CommonUtils.listFlatten(
                        configVOList.stream().filter(it -> it.getBrand().equals(brand)).collect(Collectors.toList()),
                        appendConfigList.stream().filter(it -> it.getBrand().equals(brand)).collect(Collectors.toList()))
                );
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
