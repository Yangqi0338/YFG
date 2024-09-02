/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskPageSumVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：产前样-任务 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.dao.PreProductionSampleTaskDao
 * @email your email
 * @date 创建时间：2023-7-18 11:04:08
 */
@Mapper
public interface PreProductionSampleTaskMapper extends BaseMapper<PreProductionSampleTask> {
// 自定义方法区 不替换的区域【other_start】


    /**
     * 任务列表
     *
     * @param qw
     * @return
     */
    List<PreProductionSampleTaskVo> taskList(@Param(Constants.WRAPPER) QueryWrapper<PreProductionSampleTask> qw);

    PreProductionSampleTaskPageSumVo taskListSum(@Param(Constants.WRAPPER) QueryWrapper<PreProductionSampleTask> qw);

    long countByQw(@Param(Constants.WRAPPER) QueryWrapper<PreProductionSampleTask> countQc);

    List<String> stitcherList(@Param(Constants.WRAPPER) BaseQueryWrapper<PreProductionSampleTask> qw);

// 自定义方法区 不替换的区域【other_end】
}