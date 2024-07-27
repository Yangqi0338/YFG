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
import com.base.sbc.module.workload.vo.WorkloadRatingDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：工作量评分数据计算结果 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.service.WorkloadRatingDetailService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
@Service
public class WorkloadRatingDetailServiceImpl extends BaseServiceImpl<WorkloadRatingDetailMapper, WorkloadRatingDetail> implements WorkloadRatingDetailService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<WorkloadRatingDetailVO> queryPageInfo(WorkloadRatingDetailQO qo) {
        Page<WorkloadRatingDetail> page = qo.startPage();
        BaseQueryWrapper<WorkloadRatingDetail> qw = buildQueryWrapper(qo);
        List<WorkloadRatingDetail> list = this.list(qw);
        return CopyUtil.copy(page.toPageInfo(), BeanUtil.copyToList(list, WorkloadRatingDetailVO.class));
    }

    private BaseQueryWrapper<WorkloadRatingDetail> buildQueryWrapper(WorkloadRatingDetailQO qo) {
        BaseQueryWrapper<WorkloadRatingDetail> qw = new BaseQueryWrapper<>();
        return qw;
    }

    @Override
    public WorkloadRatingDetailDTO detail(String id) {
        WorkloadRatingDetail entity = this.getById(id);
        return BeanUtil.copyProperties(entity, WorkloadRatingDetailDTO.class);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public void save(WorkloadRatingDetailDTO workloadRatingDetail) {
        String id = workloadRatingDetail.getId();
        WorkloadRatingDetail entity;
        if (StrUtil.isNotBlank(id)) {
            entity = getById(id);
            BeanUtil.copyProperties(workloadRatingDetail, entity);
            this.updateById(entity);
        } else {
            entity = BeanUtil.copyProperties(workloadRatingDetail, WorkloadRatingDetail.class);
            this.save(entity);
        }
        workloadRatingDetail.setId(entity.getId());
    }

// 自定义方法区 不替换的区域【other_end】

}
