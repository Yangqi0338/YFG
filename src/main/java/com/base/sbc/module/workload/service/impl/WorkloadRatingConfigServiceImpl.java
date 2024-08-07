/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.SelectOptionsChildrenVo;
import com.base.sbc.module.workload.dto.WorkloadRatingConfigDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingTitleFieldDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingConfig;
import com.base.sbc.module.workload.mapper.WorkloadRatingConfigMapper;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：工作量评分选项配置 service类
 * @address com.base.sbc.module.workload.service.WorkloadRatingConfigService
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Service
public class WorkloadRatingConfigServiceImpl extends BaseServiceImpl<WorkloadRatingConfigMapper, WorkloadRatingConfig> implements WorkloadRatingConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WorkloadRatingConfigVO> queryList(WorkloadRatingConfigQO qo) {
        // 检查当前brand是否存在 没有就copy all数据
        initDataList(qo);
        List<WorkloadRatingItemType> notSearch = qo.getNotSearch();
        BaseLambdaQueryWrapper<WorkloadRatingConfig> qw = buildQueryWrapper(qo);
        List<WorkloadRatingConfigVO> resultList = BeanUtil.copyToList(this.list(qw), WorkloadRatingConfigVO.class);

        resultList.stream().collect(CommonUtils.toMap(it -> Pair.of(it.getItemType(), it.getItemValue())))
                .forEach((key, result) -> {
                    WorkloadRatingItemType itemType = key.getKey();
                    String valueKey = key.getValue();

                    if (qo.isSearchValue() && (CollUtil.isEmpty(notSearch) || !notSearch.contains(itemType))) {
                        List<SelectOptionsChildrenVo> valueList = itemType.findSelectOptionsByKey(valueKey);
                        result.setOptionsList(valueList);
                    }

                    if (qo.isBuildTitleField()) {
                        List<WorkloadRatingTitleFieldDTO> titleFieldDTOList = new ArrayList<>();
                        String titleDictKey = result.getTitleDictKey();
                        if (StrUtil.isNotBlank(titleDictKey)) {
                            titleFieldDTOList.addAll(ccmFeignService.getDictInfoToList(titleDictKey).stream().map(it ->
                                    new WorkloadRatingTitleFieldDTO(it.getDescription(), it.getName(), it.getSort().intValue())
                            ).collect(Collectors.toList()));
                        } else if (Arrays.asList(WorkloadRatingItemType.ARRAY, WorkloadRatingItemType.ENUM).contains(itemType)) {
                            titleFieldDTOList.add(new WorkloadRatingTitleFieldDTO("itemName", result.getItemName(), 0));
                        } else {
                            List<BasicBaseDict> parentDictList = ccmFeignService.getDictParentByType(valueKey);
                            parentDictList.stream().findFirst().ifPresent(parentDict -> {
                                titleFieldDTOList.add(new WorkloadRatingTitleFieldDTO("itemName", parentDict.getName(), 0));
                            });
                        }
                        Class<?> titleClass = itemType.getTitleClass();
                        if (titleClass != null) {
                            List<String> fieldNameList = Arrays.stream(titleClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
                            List<String> codeList = titleFieldDTOList.stream().map(WorkloadRatingTitleFieldDTO::getCode).collect(Collectors.toList());
                            boolean isIntersection = CollUtil.containsAny(codeList, fieldNameList);
                            if (isIntersection) {
                                titleFieldDTOList.removeIf(it -> !fieldNameList.contains(it.getCode()));
                            }
                        }
                        int maxValue = titleFieldDTOList.stream().mapToInt(WorkloadRatingTitleFieldDTO::getIndex).max().orElse(-1);
                        titleFieldDTOList.add(new WorkloadRatingTitleFieldDTO("score", result.getCalculateType().getText(), maxValue + 1, result.getItemName()));
                        // 查询相同的show
                        WorkloadRatingConfigQO notShowQo = new WorkloadRatingConfigQO();
                        notShowQo.setType(result.getType());
                        notShowQo.setBrand(result.getBrand());
                        notShowQo.setItemType(result.getItemType());
                        notShowQo.setItemValue(result.getItemValue());
                        notShowQo.setIsConfigShow(YesOrNoEnum.NO);
                        List<WorkloadRatingConfig> notShowList = this.list(buildQueryWrapper(notShowQo));
                        for (int i = 1; i <= notShowList.size(); i++) {
                            WorkloadRatingConfig notShowConfig = notShowList.get(i - 1);
                            titleFieldDTOList.add(new WorkloadRatingTitleFieldDTO("score" + i, notShowConfig.getItemName(), maxValue + 1 + i, notShowConfig.getItemName()));
                        }
                        result.setTitleField(JSONUtil.toJsonStr(titleFieldDTOList));
                    }
                });

        return resultList;
    }

    private BaseLambdaQueryWrapper<WorkloadRatingConfig> buildQueryWrapper(WorkloadRatingConfigQO qo) {
        BaseLambdaQueryWrapper<WorkloadRatingConfig> qw = new BaseLambdaQueryWrapper<WorkloadRatingConfig>()
                .notEmptyIn(WorkloadRatingConfig::getId, qo.getId())
                .notEmptyEq(WorkloadRatingConfig::getItemName, qo.getItemName())
                .notNullEq(WorkloadRatingConfig::getItemType, qo.getItemType())
                .notEmptyEq(WorkloadRatingConfig::getItemValue, qo.getItemValue())
                .notNullEq(WorkloadRatingConfig::getIsConfigShow, qo.getIsConfigShow())
                .notNullEq(WorkloadRatingConfig::getType, qo.getType())
                .notEmptyIn(WorkloadRatingConfig::getBrand, qo.getBrand());
        qw.orderByAsc(WorkloadRatingConfig::getSort);
        return qw;
    }

    public void initDataList(WorkloadRatingConfigQO qo) {
        String originBrand = qo.getBrand();
        WorkloadRatingConfigQO existsQo = new WorkloadRatingConfigQO();
        existsQo.setType(qo.getType());
        existsQo.setBrand(originBrand);
        if (!this.exists(buildQueryWrapper(existsQo))) {
            existsQo.setBrand("ALL");
            List<WorkloadRatingConfig> list = this.list(buildQueryWrapper(existsQo));
            list.forEach(it -> {
                it.setBrand(originBrand);
                it.preInsert(null);
                save(BeanUtil.copyProperties(it, WorkloadRatingConfigDTO.class));
            });
        }
    }

    @Override
    public WorkloadRatingConfigDTO detail(String id) {
        WorkloadRatingConfig entity = this.getById(id);
        return BeanUtil.copyProperties(entity, WorkloadRatingConfigDTO.class);
    }

    @Override
    public void save(WorkloadRatingConfigDTO workloadRatingConfig) {
        String id = workloadRatingConfig.getId();
        WorkloadRatingConfig entity;
        if (StrUtil.isNotBlank(id)) {
            entity = getById(id);
            BeanUtil.copyProperties(workloadRatingConfig, entity);
            this.updateById(entity);
        } else {
            entity = BeanUtil.copyProperties(workloadRatingConfig, WorkloadRatingConfig.class);
            this.save(entity);
        }
        workloadRatingConfig.setId(entity.getId());
    }

// 自定义方法区 不替换的区域【other_end】

}
