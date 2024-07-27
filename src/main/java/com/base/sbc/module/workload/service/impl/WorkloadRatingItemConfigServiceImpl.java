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
import com.base.sbc.module.workload.dto.WorkloadRatingItemConfigDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingItemConfig;
import com.base.sbc.module.workload.mapper.WorkloadRatingItemConfigMapper;
import com.base.sbc.module.workload.service.WorkloadRatingItemConfigService;
import com.base.sbc.module.workload.vo.WorkloadRatingItemConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemConfigVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：工作量评分选项配置 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.service.WorkloadRatingItemConfigService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:44
 */
@Service
public class WorkloadRatingItemConfigServiceImpl extends BaseServiceImpl<WorkloadRatingItemConfigMapper, WorkloadRatingItemConfig> implements WorkloadRatingItemConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<WorkloadRatingItemConfigVO> queryPageInfo(WorkloadRatingItemConfigQO qo) {
        Page<WorkloadRatingItemConfig> page = qo.startPage();
        BaseQueryWrapper<WorkloadRatingItemConfig> qw = buildQueryWrapper(qo);
        List<WorkloadRatingItemConfig> list = this.list(qw);
        return CopyUtil.copy(page.toPageInfo(), BeanUtil.copyToList(list, WorkloadRatingItemConfigVO.class));
    }

    private BaseQueryWrapper<WorkloadRatingItemConfig> buildQueryWrapper(WorkloadRatingItemConfigQO qo) {
        BaseQueryWrapper<WorkloadRatingItemConfig> qw = new BaseQueryWrapper<>();
        return qw;
    }

    @Override
    public WorkloadRatingItemConfigDTO detail(String id) {
        WorkloadRatingItemConfig entity = this.getById(id);
        return BeanUtil.copyProperties(entity, WorkloadRatingItemConfigDTO.class);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public void save(WorkloadRatingItemConfigDTO workloadRatingItemConfig) {
        String id = workloadRatingItemConfig.getId();
        WorkloadRatingItemConfig entity;
        if (StrUtil.isNotBlank(id)) {
            entity = getById(id);
            BeanUtil.copyProperties(workloadRatingItemConfig, entity);
            this.updateById(entity);
        } else {
            entity = BeanUtil.copyProperties(workloadRatingItemConfig, WorkloadRatingItemConfig.class);
            this.save(entity);
        }
        workloadRatingItemConfig.setId(entity.getId());
    }

// 自定义方法区 不替换的区域【other_end】

}
