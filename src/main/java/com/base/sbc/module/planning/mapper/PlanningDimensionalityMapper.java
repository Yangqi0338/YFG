/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：企划-维度表 dao类
 * @address com.base.sbc.module.planning.dao.PlanningDimensionalityDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-4-27 11:15:30 
 * @version 1.0  
 */
@Mapper
public interface PlanningDimensionalityMapper extends BaseMapper<PlanningDimensionality> {
/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 获取系数列表
     * @param qw
     * @return
     */
    List<PlanningDimensionalityVo> getCoefficientList(@Param(Constants.WRAPPER) BaseQueryWrapper<PlanningDimensionality> qw);

    /**
     * 获取系数列表
     * @param qw
     * @return
     */
    List<PlanningDimensionalityVo> getMaterialCoefficient(@Param(Constants.WRAPPER) BaseQueryWrapper<PlanningDimensionality> qw);


/** 自定义方法区 不替换的区域【other_end】 **/
}