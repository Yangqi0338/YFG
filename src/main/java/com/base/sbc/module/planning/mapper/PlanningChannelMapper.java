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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.vo.CountVo;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：企划-渠道 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.dao.PlanningChannelDao
 * @email your email
 * @date 创建时间：2023-7-21 16:00:35
 */
@Mapper
public interface PlanningChannelMapper extends BaseMapper<PlanningChannel> {
// 自定义方法区 不替换的区域【other_start】

    List<PlanningChannelVo> list(@Param(Constants.WRAPPER) BaseQueryWrapper<PlanningChannel> qw);

    /**
     * 获取最大序号
     * @return
     */
    Integer getMaxSort(@Param("planningSeasonId") String planningSeasonId);

    /**
     * 渠道分类下拉框选择
     *
     * @param planningSeasonId
     * @return
     */
    List<SelectOptionsVo> channelClassifSelection(@Param("planningSeasonId") String planningSeasonId);

    List<CountVo> selectIdCount(@Param(Constants.WRAPPER) QueryWrapper qw);


// 自定义方法区 不替换的区域【other_end】
}