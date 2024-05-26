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
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.entity.PlanningSeason;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：企划-产品季 dao类
 * @address com.base.sbc.module.planning.dao.PlanningSeasonDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08 
 * @version 1.0
 */
@Mapper
public interface PlanningSeasonMapper extends BaseMapper<PlanningSeason> {

    List<PlanningSeason> list(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SelectOptionsVo> getPlanningSeasonOptions(@Param(Constants.WRAPPER) QueryWrapper qw);

}
