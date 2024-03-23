package com.base.sbc.module.report.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.report.dto.TechnologyCenterBoardDto;
import com.base.sbc.module.report.vo.TechnologyCenterBoardOverviewDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TechnologyCenterBoardMapper {

    /**
     * 数据总览
     * @return
     */
    TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

}
