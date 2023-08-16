/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.ThemePlanningSaveDTO;
import com.base.sbc.module.planning.dto.ThemePlanningSearchDTO;
import com.base.sbc.module.planning.entity.ThemePlanning;
import com.base.sbc.module.planning.vo.ThemePlanningListVO;
import com.base.sbc.module.planning.vo.ThemePlanningVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：主题企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ThemePlanningService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 */
public interface ThemePlanningService extends BaseService<ThemePlanning> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取主题企划列表
     *
     * @param dto
     * @return
     */
    PageInfo<ThemePlanningListVO> getThemePlanningList(ThemePlanningSearchDTO dto);

    /**
     * 获取主题企划详情
     *
     * @param id
     * @return
     */
    ThemePlanningVO getThemePlanningById(String id);

    /**
     * 保存
     *
     * @param dto
     */
    String themePlanningSave(ThemePlanningSaveDTO dto);


    /**
     * 通过产品季id统计
     *
     * @param planningSeasonId
     * @return
     */
    Long getThemePlanningCount(String planningSeasonId);

    /**
     * 通过产品季id获取
     *
     * @param planningSeasonId
     * @return
     */
    List<ThemePlanningListVO> getThemeListByPlanningSeasonId(String planningSeasonId);

// 自定义方法区 不替换的区域【other_end】


}

