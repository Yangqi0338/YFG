/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingTitleFieldDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingConfig;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.mapper.WorkloadRatingItemMapper;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.service.WorkloadRatingItemService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.WORKLOAD_CV;

/**
 * 类描述：工作量评分配置 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.service.WorkloadRatingItemService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 */
@Service
public class WorkloadRatingItemServiceImpl extends BaseServiceImpl<WorkloadRatingItemMapper, WorkloadRatingItem> implements WorkloadRatingItemService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private WorkloadRatingConfigService workloadRatingConfigService;

    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    public PageInfo<WorkloadRatingItemVO> queryPageInfo(WorkloadRatingItemQO qo) {
        WorkloadRatingConfigVO config = findConfig(qo);
        qo.setConfigId(config.getId());
        BaseLambdaQueryWrapper<WorkloadRatingItem> qw = buildQueryWrapper(qo);
        Page<WorkloadRatingItem> page = qo.startPage();

        List<WorkloadRatingItemVO> resultList = WORKLOAD_CV.copy2ItemVO(this.list(qw));

        List<WorkloadRatingConfigVO> extendConfigList = findExtendConfigList(config);
        qo.setConfigId(extendConfigList.stream().map(WorkloadRatingConfigVO::getId).collect(Collectors.joining(COMMA)));
        List<WorkloadRatingItem> extendList = this.list(buildQueryWrapper(qo));

        resultList.forEach(result -> {
            List<WorkloadRatingItem> list = extendList.stream().filter(it -> it.getItemValue().equals(result.getItemValue())).collect(Collectors.toList());
            result.setItemList(list);
            result.setTitleFieldList(config.getTitleFieldDTOList());
        });
        return CopyUtil.copy(page.toPageInfo(), resultList);
    }

    private List<WorkloadRatingConfigVO> findExtendConfigList(WorkloadRatingConfig config) {
        WorkloadRatingConfigQO configQO = new WorkloadRatingConfigQO();
        configQO.setType(config.getType());
        configQO.setBrand(config.getBrand());
        configQO.setItemType(config.getItemType());
        configQO.setItemValue(config.getItemValue());
        configQO.setIsConfigShow(YesOrNoEnum.NO);
        configQO.setSearchValue(false);
        configQO.setBuildTitleField(false);
        return workloadRatingConfigService.queryList(configQO);
    }

    private WorkloadRatingConfigVO findConfig(WorkloadRatingItemQO qo) {
        WorkloadRatingConfigQO configQO = WORKLOAD_CV.copy2ConfigQO(qo);
        configQO.setIsConfigShow(YesOrNoEnum.YES);
        configQO.setSearchValue(false);
        configQO.reset2QueryFirst();
        List<WorkloadRatingConfigVO> list = workloadRatingConfigService.queryList(configQO);
        if (CollUtil.isEmpty(list)) throw new OtherException("错误的配置项");
        return list.get(0);
    }

    private BaseLambdaQueryWrapper<WorkloadRatingItem> buildQueryWrapper(WorkloadRatingItemQO qo) {
        BaseLambdaQueryWrapper<WorkloadRatingItem> qw = new BaseLambdaQueryWrapper<WorkloadRatingItem>()
                .notEmptyIn(WorkloadRatingItem::getConfigId, qo.getConfigId())
                .notEmptyEq(WorkloadRatingItem::getItemValue, qo.getItemValue())
                .notEmptyEq(WorkloadRatingItem::getId, qo.getId());
        return qw;
    }

    @Override
    public WorkloadRatingItemDTO detail(WorkloadRatingItemQO qo) {
        WorkloadRatingConfig config = findConfig(qo);
        String itemValue = qo.getItemValue();
        WorkloadRatingItem entity = this.getById(config.getId());
        return BeanUtil.copyProperties(entity, WorkloadRatingItemDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delByIds(List<String> ids) {
        if (CollUtil.isEmpty(ids)) return true;
        List<WorkloadRatingItem> list = this.listByIds(ids);
        WorkloadRatingItemQO qo = new WorkloadRatingItemQO();
        qo.setConfigId(list.get(0).getConfigId());
        WorkloadRatingConfigVO config = findConfig(qo);
        List<String> configList = config.findConfigTitleFieldList().stream().map(WorkloadRatingTitleFieldDTO::getConfigId).collect(Collectors.toList());
        // 查detail
        return this.remove(new LambdaQueryWrapper<WorkloadRatingItem>()
                .eq(WorkloadRatingItem::getType, config.getType())
                .eq(WorkloadRatingItem::getBrand, config.getBrand())
                .in(WorkloadRatingItem::getConfigId, configList)
                .in(WorkloadRatingItem::getItemValue, list.stream().map(WorkloadRatingItem::getItemValue).collect(Collectors.toList()))
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<WorkloadRatingItemDTO> workloadRatingItemList) {
        if (CollUtil.isEmpty(workloadRatingItemList)) return;
        // 查config
        List<String> configIdList = workloadRatingItemList.stream().map(WorkloadRatingItem::getConfigId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(configIdList) || configIdList.size() > 1) throw new OtherException("错误的配置项");
        WorkloadRatingItemQO qo = new WorkloadRatingItemQO();
        qo.setConfigId(configIdList.get(0));
        WorkloadRatingConfigVO config = findConfig(qo);

        // 获取全部配置
        List<String> brandList = CollUtil.newArrayList(config.getBrand());
        if (workloadRatingItemList.stream().anyMatch(it -> BooleanUtil.isTrue(it.getApplyAll()))) {
            List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList("C8_Brand");
            brandList.addAll(dictList.stream().map(BasicBaseDict::getValue).collect(Collectors.toList()));
        }
        WorkloadRatingConfigQO configQO = new WorkloadRatingConfigQO();
        configQO.setBrand(String.join(COMMA, brandList));
        configQO.setType(config.getType());
        configQO.setSearchValue(false);
        configQO.setBuildTitleField(false);
        configQO.reset2QueryList();
        List<WorkloadRatingConfigVO> configVOList = workloadRatingConfigService.queryList(configQO);

        List<WorkloadRatingTitleFieldDTO> configTitleFieldList = config.findConfigTitleFieldList();
        List<WorkloadRatingTitleFieldDTO> titleFieldDTOList = config.getTitleFieldDTOList();

        workloadRatingItemList.forEach(saveDTO -> {
            WorkloadRatingItem originEntity = this.findOne(saveDTO.getId());
            // 检查能否更新
            String itemValue = CommonUtils.removeSuffix(saveDTO.getItemValue(), "/");
            saveDTO.setItemValue(itemValue);
            boolean exists = this.exists(new BaseLambdaQueryWrapper<WorkloadRatingItem>()
                    .notNullNe(WorkloadRatingItem::getId, saveDTO.getId())
                    .eq(WorkloadRatingItem::getConfigId, saveDTO.getConfigId())
                    .eq(WorkloadRatingItem::getItemValue, itemValue)
            );
            if (exists) throw new OtherException("存在相同的评分项");
            String[] itemSubValue = itemValue.split("/");
            int length = itemSubValue.length;
            if (length > 1) {
                if (this.exists(new BaseLambdaQueryWrapper<WorkloadRatingItem>()
                        .notNullNe(WorkloadRatingItem::getId, saveDTO.getId())
                        .eq(WorkloadRatingItem::getConfigId, saveDTO.getConfigId())
                        .likeRight(WorkloadRatingItem::getItemValue, itemSubValue[0])
                        .apply("(LENGTH(item_value) - LENGTH(REPLACE(item_value, '/', ''))) < {0}", length - 1)
                )) throw new OtherException("已经存在上一级的评分项, 请删除后添加");
            }

            List<WorkloadRatingItem> entityList = configTitleFieldList.stream().flatMap(configTitle -> {
                WorkloadRatingConfigVO currentConfigVO = configVOList.stream().filter(it -> it.getId().equals(configTitle.getConfigId()))
                        .findFirst().orElseThrow(() -> new OtherException("无效的配置项"));
                List<WorkloadRatingConfigVO> applyConfigVoList = new ArrayList<>();
                if (Boolean.TRUE.equals(saveDTO.getApplyAll())) {
                    applyConfigVoList.addAll(configVOList.stream().filter(it -> it.getItemName().equals(currentConfigVO.getItemName())).collect(Collectors.toList()));
                } else {
                    applyConfigVoList.add(currentConfigVO);
                }
                return applyConfigVoList.stream().map(configVO -> {
                    WorkloadRatingItemDTO dto = BeanUtil.copyProperties(saveDTO, WorkloadRatingItemDTO.class);
                    dto.setType(configVO.getType());
                    dto.setBrand(configVO.getBrand());
                    dto.setItemType(configVO.getItemType());
                    dto.setItemName(CommonUtils.removeSuffix(Opt.ofBlankAble(titleFieldDTOList.stream()
                            .map(it -> StrUtil.toStringOrNull(dto.getExtend().getOrDefault(it.getCode(), null)))
                            .filter(StrUtil::isNotBlank).collect(Collectors.joining("/"))).orElse(dto.getItemName()), "/"));
                    dto.setConfigId(configVO.getId());
                    dto.setConfigName(configVO.getItemName());
                    dto.setCalculateType(configVO.getCalculateType());
                    dto.setScore(new BigDecimal(dto.getExtend().getOrDefault(configTitle.getCode(), dto.getScore()).toString()));

                    WorkloadRatingItem entity = new WorkloadRatingItem();
                    if (originEntity !=null ) {
                        dto.setId(null);
                        this.defaultValue(new WorkloadRatingItem());
                        entity = this.findOne(new LambdaQueryWrapper<WorkloadRatingItem>()
                                .eq(WorkloadRatingItem::getConfigId, dto.getConfigId())
                                .eq(WorkloadRatingItem::getItemValue, originEntity.getItemValue())
                        );
                    }

                    // 修改detail
//                StrJoiner detailItemValue = StrJoiner.of("#").append(dto.getConfigName()).append(entity.getItemValue());
//                detailList.addAll(WORKLOAD_CV.copy2DTO(
//                        workloadRatingDetailService.list(new BaseLambdaQueryWrapper<WorkloadRatingDetail>()
//                                .notEmptyNotIn(WorkloadRatingDetail::getId, detailList.stream().map(BaseEntity::getId).collect(Collectors.toList()))
//                                .eq(WorkloadRatingDetail::getType, dto.getType())
//                                .eq(WorkloadRatingDetail::getBrand, dto.getBrand())
//                                .like(WorkloadRatingDetail::getItemValue, detailItemValue.append(YesOrNoEnum.YES.getValueStr()).toString()))
//                ));

                    WORKLOAD_CV.copy(entity, dto);
                    return entity;
                });
            }).collect(Collectors.toList());
            this.saveOrUpdateBatch(entityList);

//            List<WorkloadRatingDetail> valueList = findPerhapsItemValueList(configVOList, otherWorkloadRatingItemList, itemList, 0);

//            for (WorkloadRatingDetail detail : valueList) {
//                Optional<WorkloadRatingDetailDTO> detailOpt = detailList.stream().filter(it -> it.getItemId().equals(detail.getItemId())).findFirst();
//                if (detailOpt.isPresent()) {
//                    WorkloadRatingDetailDTO existsDetail = detailOpt.get();
//                    detail.setId(existsDetail.getId());
//                    detailList.remove(existsDetail);
//                }
//            }
//            if (CollUtil.isNotEmpty(detailList)) {
//                workloadRatingDetailService.removeByIds(detailList);
//            }
//            valueList.forEach(value -> {
//                for (WorkloadRatingCalculateType calculateType : WorkloadRatingCalculateType.values()) {
//                    List<String> keyList = (List<String>) value.getExtend().getOrDefault(calculateType.getCode(), new ArrayList<>());
//                    BigDecimal increment = BigDecimal.ZERO;
//                    for (String key : keyList) {
//                        BigDecimal score = new BigDecimal(value.getExtend().getOrDefault(key, calculateType.getDefaultValue()).toString());
//                        Pair<BigDecimal, BigDecimal> pair = calculateType.calculate(value.getResult(), score);
//                        increment = increment.add(pair.getValue());
//                        value.setResult(pair.getKey());
//                    }
//                    value.getExtend().put(calculateType.getCode() + "Total", increment);
//                }
//            });
//            workloadRatingDetailService.saveOrUpdateBatch(valueList);
        });
    }

//    private List<WorkloadRatingDetail> findPerhapsItemValueList(List<WorkloadRatingConfigVO> configVOList,
//                                                                List<WorkloadRatingItem> otherWorkloadRatingItemList,
//                                                                List<WorkloadRatingItem> itemList, Integer sort) {
//        List<WorkloadRatingDetail> result = new ArrayList<>();
//        configVOList.stream().filter(it -> it.getSort().equals(sort)).forEach(configVO -> {
//            String configId = configVO.getId();
//            List<WorkloadRatingDetail> valueList = findPerhapsItemValueList(configVOList, otherWorkloadRatingItemList, itemList, sort + 1);
//            List<WorkloadRatingDetail> addValueList = new ArrayList<>();
//            if (CollUtil.isEmpty(valueList)) {
//                WorkloadRatingDetail detail = new WorkloadRatingDetail();
//                detail.setType(configVO.getType());
//                detail.setBrand(configVO.getBrand());
//                detail.setResult(BigDecimal.ZERO);
//                valueList.add(detail);
//            }
//            Optional<WorkloadRatingItem> itemOpt = itemList.stream().filter(it -> it.getConfigId().equals(configId)).findFirst();
//            if (itemOpt.isPresent()) {
//                WorkloadRatingItem currentItem = itemOpt.get();
//                valueList.forEach(it -> {
//                    it.setItemId(StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.IGNORE).append(currentItem.getId()).append(it.getItemId()).toString());
//                    String itemValue = currentItem.getConfigName() + "#" + currentItem.getItemValue();
//                    for (YesOrNoEnum enableFlag : YesOrNoEnum.values()) {
//                        if (configVO.getIsConfigShow() == YesOrNoEnum.NO || configVO.getIsConfigShow() == enableFlag) {
//                            WorkloadRatingDetail newDetail = BeanUtil.copyProperties(it, WorkloadRatingDetail.class);
//                            String newItemValue = itemValue + "#" + enableFlag.getValueStr();
//                            newDetail.getExtend().put(newItemValue, enableFlag == YesOrNoEnum.NO ? currentItem.getCalculateType().getDefaultValue() : currentItem.getScore());
//                            List<String> keyList = (List<String>) newDetail.getExtend().getOrDefault(currentItem.getCalculateType().getCode(), new ArrayList<>());
//                            keyList.add(newItemValue);
//                            newDetail.getExtend().put(currentItem.getCalculateType().getCode(), keyList);
//                            newDetail.setItemValue(StrJoiner.of("-").setNullMode(StrJoiner.NullMode.IGNORE).append(newItemValue).append(newDetail.getItemValue()).toString());
//                            addValueList.add(newDetail);
//                        }
//                    }
//                });
//            } else {
//                otherWorkloadRatingItemList.stream().filter(it -> it.getConfigId().equals(configId)).forEach(otherItem -> {
//                    String itemValue = otherItem.getConfigName() + "#" + otherItem.getItemValue();
//                    valueList.forEach(value -> {
//                        for (YesOrNoEnum enableFlag : YesOrNoEnum.values()) {
//                            if (configVO.getIsConfigShow() == YesOrNoEnum.NO || configVO.getIsConfigShow() == enableFlag) {
//                                WorkloadRatingDetail newDetail = BeanUtil.copyProperties(value, WorkloadRatingDetail.class);
//                                newDetail.setType(configVO.getType());
//                                newDetail.setBrand(configVO.getBrand());
//                                newDetail.setItemId(StrJoiner.of(COMMA).setNullMode(StrJoiner.NullMode.IGNORE).append(otherItem.getId()).append(value.getItemId()).toString());
//                                String newItemValue = itemValue + "#" + enableFlag.getValueStr();
//                                newDetail.getExtend().put(newItemValue, otherItem.getScore());
//                                List<String> keyList = (List<String>) newDetail.getExtend().getOrDefault(otherItem.getCalculateType().getCode(), new ArrayList<>());
//                                keyList.add(newItemValue);
//                                newDetail.getExtend().put(otherItem.getCalculateType().getCode(), keyList);
//                                newDetail.setItemValue(StrJoiner.of("-").setNullMode(StrJoiner.NullMode.IGNORE).append(newItemValue).append(newDetail.getItemValue()).toString());
//                                addValueList.add(newDetail);
//                            }
//                        }
//                    });
//                });
//            }
//            if (CollUtil.isEmpty(addValueList) && CollUtil.isNotEmpty(valueList)) {
//                addValueList.addAll(valueList);
//            }
//            result.addAll(addValueList);
//        });
//        return result;
//    }

// 自定义方法区 不替换的区域【other_end】

}
