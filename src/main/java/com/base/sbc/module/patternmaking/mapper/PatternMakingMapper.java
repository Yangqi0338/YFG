/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.vo.PatternDesignSampleTypeQtyVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingTaskListVo;
import com.base.sbc.module.patternmaking.vo.SampleBoardVo;
import com.base.sbc.module.patternmaking.vo.TechnologyCenterTaskVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：打版管理 dao类
 * @address com.base.sbc.module.patternmaking.dao.PatternMakingDao
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05 
 * @version 1.0
 */
@Mapper
public interface PatternMakingMapper extends BaseMapper<PatternMaking> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<TechnologyCenterTaskVo> technologyCenterTaskList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PatternDesignSampleTypeQtyVo> getPatternDesignSampleTypeCount(@Param(Constants.WRAPPER) QueryWrapper pmQw);

    List<PatternMakingTaskListVo> patternMakingTaskList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleBoardVo> sampleBoardList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleUserVo> getAllPatternDesignList(@Param("companyCode") String companyCode);


/** 自定义方法区 不替换的区域【other_end】 **/
}

