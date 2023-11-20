package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.github.pagehelper.PageInfo;

/**
 * @author 卞康
 * @date 2023/11/17 9:44:54
 * @mail 247967116@qq.com
 */
public interface PlanningProjectPlankService extends BaseService<PlanningProjectPlank> {
    PageInfo<PlanningProjectPlankVo> queryPage(PlanningProjectPlankPageDto dto);
}
