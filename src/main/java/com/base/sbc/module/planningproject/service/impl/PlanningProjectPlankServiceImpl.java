package com.base.sbc.module.planningproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.mapper.PlanningProjectPlankMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:19
 * @mail 247967116@qq.com
 */
@Service
public class PlanningProjectPlankServiceImpl extends BaseServiceImpl<PlanningProjectPlankMapper, PlanningProjectPlank> implements PlanningProjectPlankService {
    @Override
    public PageInfo<PlanningProjectPlank> queryPage(PlanningProjectPlankPageDto dto) {
        PageHelper.startPage(dto);
        QueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();

        List<PlanningProjectPlank> list = this.list(queryWrapper);

        return new PageInfo<>(list);
    }
}
