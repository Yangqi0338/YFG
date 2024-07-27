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
import com.base.sbc.module.workload.dto.WorkloadRatingConfigDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingConfig;
import com.base.sbc.module.workload.mapper.WorkloadRatingConfigMapper;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：工作量评分配置 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.service.WorkloadRatingConfigService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
@Service
public class WorkloadRatingConfigServiceImpl extends BaseServiceImpl<WorkloadRatingConfigMapper, WorkloadRatingConfig> implements WorkloadRatingConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<WorkloadRatingConfigVO> queryPageInfo(WorkloadRatingConfigQO qo) {
        Page<WorkloadRatingConfig> page = qo.startPage();
        BaseQueryWrapper<WorkloadRatingConfig> qw = buildQueryWrapper(qo);
        List<WorkloadRatingConfig> list = this.list(qw);
        return CopyUtil.copy(page.toPageInfo(), BeanUtil.copyToList(list, WorkloadRatingConfigVO.class));
    }

    private BaseQueryWrapper<WorkloadRatingConfig> buildQueryWrapper(WorkloadRatingConfigQO qo) {
        BaseQueryWrapper<WorkloadRatingConfig> qw = new BaseQueryWrapper<>();
        return qw;
    }

    @Override
    public WorkloadRatingConfigDTO detail(String id) {
        WorkloadRatingConfig entity = this.getById(id);
        return BeanUtil.copyProperties(entity, WorkloadRatingConfigDTO.class);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        return this.removeByIds(ids);
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
