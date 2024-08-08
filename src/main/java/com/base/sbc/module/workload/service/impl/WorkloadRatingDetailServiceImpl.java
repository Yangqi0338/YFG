/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.workload.WorkloadRatingCalculateType;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.mapper.WorkloadRatingDetailMapper;
import com.base.sbc.module.workload.service.WorkloadRatingDetailService;
import com.base.sbc.module.workload.service.WorkloadRatingItemService;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailQO;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailSaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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

// 自定义方法区 不替换的区域【other_end】

}
