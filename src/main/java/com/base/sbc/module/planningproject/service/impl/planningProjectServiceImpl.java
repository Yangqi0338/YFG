package com.base.sbc.module.planningproject.service.impl;

import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.mapper.PlanningProjectMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 保存企划看板规划信息
     *
     * @param planningProjectSaveDTO 实体对象
     * @return 返回的结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(PlanningProjectSaveDTO planningProjectSaveDTO) {
        if(StringUtils.isEmpty(planningProjectSaveDTO.getId())){
            super.save(planningProjectSaveDTO);
            //新建对应的坑位信息


            return true;
        }

        return false;
    }
}
