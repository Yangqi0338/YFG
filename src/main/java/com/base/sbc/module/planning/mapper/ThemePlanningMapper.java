/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planning.dto.ThemePlanningSearchDTO;
import com.base.sbc.module.planning.entity.ThemePlanning;
import com.base.sbc.module.planning.vo.ThemePlanningListVO;
import com.base.sbc.module.planning.vo.ThemePlanningVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：主题企划 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.dao.ThemePlanningDao
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 */
@Mapper
public interface ThemePlanningMapper extends BaseMapper<ThemePlanning> {
    // 自定义方法区 不替换的区域【other_start】

    /**
     * 获取主题企划列表
     *
     * @param dto
     * @return
     */
    List<ThemePlanningListVO> getThemePlanningList(@Param("dto") ThemePlanningSearchDTO dto);

    /**
     * 通过id获取主题企划
     *
     * @param id
     * @return
     */
    ThemePlanningVO getThemePlanningById(@Param("id") String id);

// 自定义方法区 不替换的区域【other_end】
}

