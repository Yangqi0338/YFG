/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.vo.ReplayRatingFabricVO;
import com.base.sbc.module.replay.vo.ReplayRatingPatternVO;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingStyleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类描述：基础资料-复盘评分 dao类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.dao.ReplayRatingDao
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Mapper
public interface ReplayRatingMapper extends BaseMapper<ReplayRating> {
// 自定义方法区 不替换的区域【other_start】

    List<ReplayRatingStyleVO> queryStyleList(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

    List<ReplayRatingStyleVO> queryStyleList_COUNT(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

    List<ReplayRatingPatternVO> queryPatternList(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

    List<Map<String, Object>> queryPatternList_COUNT(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

    List<ReplayRatingFabricVO> queryFabricList(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

    List<Map<String, Object>> queryFabricList_COUNT(@Param(Constants.WRAPPER) BaseQueryWrapper qw, @Param("qo") ReplayRatingQO qo);

// 自定义方法区 不替换的区域【other_end】
}