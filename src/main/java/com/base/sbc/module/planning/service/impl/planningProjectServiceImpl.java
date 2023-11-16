package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.PlanningProjectDTO;
import com.base.sbc.module.planning.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.entity.PlanningProject;
import com.base.sbc.module.planning.mapper.PlanningProjectMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningProjectService;
import com.base.sbc.module.planning.vo.PlanningProjectVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class planningProjectServiceImpl extends BaseServiceImpl<PlanningProjectMapper, PlanningProject> implements PlanningProjectService {

    @Autowired
    PlanningCategoryItemService planningCategoryItemService;


    //查询看板规划
    @Override
    public PageInfo<PlanningProjectVo> planningProject(PlanningProjectPageDTO dto) {
        /*分页*/
        Page<PlanningProjectVo> page = PageHelper.startPage(dto);
        super.getBaseMapper().getplanningProjectList(dto);
        return CopyUtil.copy(page.toPageInfo(), PlanningProjectVo.class);
    }

    //新增、修改
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String planningProjectAdd(PlanningProjectDTO dto) {
        PlanningProject planningProject = CopyUtil.copy(dto, PlanningProject.class);
        Date date = new Date();
        BeanUtil.copyProperties(dto, planningProject);
        String prodCategory = dto.getProdCategory().stream().map(String::valueOf).collect(Collectors.joining(","));
        String prodCategoryName = dto.getProdCategoryName().stream().map(String::valueOf).collect(Collectors.joining(","));
        String prodCategory1st = dto.getProdCategory1st().stream().map(String::valueOf).collect(Collectors.joining(","));
        String prodCategory1stName = dto.getProdCategory1stName().stream().map(String::valueOf).collect(Collectors.joining(","));
        String prodCategory2nd = dto.getProdCategory2nd().stream().map(String::valueOf).collect(Collectors.joining(","));
        String prodCategory2ndName = dto.getProdCategory2ndName().stream().map(String::valueOf).collect(Collectors.joining(","));
        planningProject.setProdCategory(prodCategory);
        planningProject.setProdCategoryName(prodCategoryName);
        planningProject.setProdCategory1st(prodCategory1st);
        planningProject.setProdCategory1stName(prodCategory1stName);
        planningProject.setProdCategory2nd(prodCategory2nd);
        planningProject.setProdCategory2ndName(prodCategory2ndName);

        planningProject.setCreateDate(date);
        save(planningProject);
        return planningProject.getId();


    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String planningProjectUpdate(PlanningProjectDTO dto) {
        PlanningProject planningProject = CopyUtil.copy(dto, PlanningProject.class);
        Date date = new Date();
        BeanUtil.copyProperties(dto, planningProject);
        planningProject.setUpdateDate(date);
        updateById(planningProject);
        return planningProject.getId();

    }
    //删除看板规划
    @Override
    public boolean planningProjectDel(String ids) {
        if (StringUtils.isEmpty(ids)){
            throw new OtherException("id不能为空");
        }
        return this.removeByIds(Arrays.asList(ids.split(",")));
    }

}
