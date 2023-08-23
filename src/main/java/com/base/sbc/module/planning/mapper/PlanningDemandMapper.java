/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.pack.dto.PlanningDemandStatisticsVo;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.vo.PlanningDemandVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：企划-需求维度表 dao类
 * @address com.base.sbc.module.planning.dao.PlanningDemandDimensionalityDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18 
 * @version 1.0
 */
@Mapper
public interface PlanningDemandMapper extends BaseMapper<PlanningDemand> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/


    List<PlanningDemandVo> getDemandDimensionalityById(@Param(Constants.WRAPPER) QueryWrapper<PlanningDemand> wrapper);

    List<PlanningDemandStatisticsVo> queryDemandStatistics(@Param(Constants.WRAPPER) QueryWrapper<PlanningDemand> qw);

/** 自定义方法区 不替换的区域【other_end】 **/
}