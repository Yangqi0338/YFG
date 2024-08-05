/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.mapper.WorkloadRatingDetailMapper;
import com.base.sbc.module.workload.service.WorkloadRatingDetailService;
import com.base.sbc.module.workload.vo.WorkloadRatingDetailQO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

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

    @Override
    public List<WorkloadRatingDetailDTO> queryList(WorkloadRatingDetailQO qo) {
        BaseQueryWrapper<WorkloadRatingDetail> qw = buildQueryWrapper(qo);
        List<WorkloadRatingDetail> list = this.list(qw);
        return WORKLOAD_CV.copy2DetailDTO(list);
    }

    private BaseQueryWrapper<WorkloadRatingDetail> buildQueryWrapper(WorkloadRatingDetailQO qo) {
        BaseQueryWrapper<WorkloadRatingDetail> qw = new BaseQueryWrapper<>();
        qw.notEmptyIn(WorkloadRatingDetail::getId, qo.getIds());
        return qw;
    }

    @Override
    public void save(WorkloadRatingDetailDTO workloadRatingDetail) {
        String id = workloadRatingDetail.getId();
        WorkloadRatingDetail entity;
        if (StrUtil.isNotBlank(id)) {
            entity = getById(id);
            WORKLOAD_CV.copy(entity, workloadRatingDetail);
            this.updateById(entity);
        } else {
            entity = WORKLOAD_CV.copy2Entity(workloadRatingDetail);
            this.save(entity);
        }
        workloadRatingDetail.setId(entity.getId());
    }

// 自定义方法区 不替换的区域【other_end】

}
