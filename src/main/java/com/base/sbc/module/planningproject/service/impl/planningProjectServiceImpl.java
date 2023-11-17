package com.base.sbc.module.planningproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.mapper.PlanningProjectMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectMaxCategoryService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.base.sbc.module.style.entity.StyleColor;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class planningProjectServiceImpl extends BaseServiceImpl<PlanningProjectMapper, PlanningProject> implements PlanningProjectService {
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectMaxCategoryService planningProjectMaxCategoryService;
    private final PlanningProjectPlankService planningProjectPlankService;

    /**
     * 分页查询企划看板规划信息
     * @param dto 查询条件
     * @return 返回的结果
     */
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
        //判断新增或者修改的时候是否存在相同的季度和渠道
        QueryWrapper<PlanningProject> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("season_id",planningProjectSaveDTO.getPlanningProjectName());
        queryWrapper.eq("planning_channel_code",planningProjectSaveDTO.getPlanningChannelCode());
        queryWrapper.ne(StringUtils.isNotBlank(planningProjectSaveDTO.getId()),"id",planningProjectSaveDTO.getId());
        List<PlanningProject> planningProjects = this.list(queryWrapper);
        if (!planningProjects.isEmpty()){
            throw new RuntimeException("该季度已经存在该渠道的企划规划");
        }

        boolean b;
        //新增或者修改操作
        if(StringUtils.isEmpty(planningProjectSaveDTO.getId())){
            super.save(planningProjectSaveDTO);
            b=true;
        }else {
            super.updateById(planningProjectSaveDTO);
            b=false;
        }
        //更新对应关联的数据
        List<PlanningProjectDimension> planningProjectDimensionList = planningProjectSaveDTO.getPlanningProjectDimensionList();
        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
            planningProjectDimension.setPlanningProjectId(planningProjectSaveDTO.getId());
        }
        planningProjectDimensionService.remove(new QueryWrapper<PlanningProjectDimension>().eq("planning_project_id",planningProjectSaveDTO.getId()));
        planningProjectDimensionService.saveBatch(planningProjectSaveDTO.getPlanningProjectDimensionList());

        List<PlanningProjectMaxCategory> planningProjectMaxCategoryList = planningProjectSaveDTO.getPlanningProjectMaxCategoryList();
        for (PlanningProjectMaxCategory planningProjectMaxCategory : planningProjectMaxCategoryList) {
            planningProjectMaxCategory.setPlanningProjectId(planningProjectSaveDTO.getId());
        }
        planningProjectMaxCategoryService.remove(new QueryWrapper<PlanningProjectMaxCategory>().eq("planning_project_id",planningProjectSaveDTO.getId()));
        planningProjectMaxCategoryService.saveBatch(planningProjectSaveDTO.getPlanningProjectMaxCategoryList());

        //新建对应的坑位信息
        if(b){
            for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
                PlanningProjectPlank planningProjectPlank =new PlanningProjectPlank();
                planningProjectPlank.setPlanningProjectId(planningProjectSaveDTO.getId());
                planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
                planningProjectPlank.setBandName(planningProjectDimension.getBandName());
                planningProjectPlank.setSilhouetteName(planningProjectDimension.getDimensionName());
                planningProjectPlank.setSilhouetteValue(planningProjectDimension.getDimensionValue());
                planningProjectPlank.setMatchingStyleStatus("0");
                //匹配规则 产品季  大类 品类 (中类有就匹配)  波段 廓形  这5匹配
                QueryWrapper<StyleColor> styleColorQueryWrapper =new QueryWrapper<>();
                styleColorQueryWrapper.eq("season_id",planningProjectSaveDTO.getPlanningProjectName());
            }
        }else {

        }




          return false;
    }
}
