package com.base.sbc.module.planning.service.impl;

import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.entity.PlanningProject;
import com.base.sbc.module.planning.mapper.PlanningProjectMapper;
import com.base.sbc.module.planning.service.PlanningProjectService;
import com.base.sbc.module.planning.vo.PlanningProjectVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class planningProjectServiceImpl extends BaseServiceImpl<PlanningProjectMapper, PlanningProject> implements PlanningProjectService {

    //查询看板规划
    @Override
    public PageInfo<PlanningProjectVo> queryPage(PlanningProjectPageDTO dto) {
        /*分页*/
        PageHelper.startPage(dto);
        List<PlanningProjectVo> planningProjectVos = this.getBaseMapper().getplanningProjectList(dto);
        return new PageInfo<>(planningProjectVos);
    }
}
