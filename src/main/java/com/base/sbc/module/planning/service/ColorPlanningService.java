/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.ColorPlanningSaveDTO;
import com.base.sbc.module.planning.dto.ColorPlanningSearchDTO;
import com.base.sbc.module.planning.entity.ColorPlanning;
import com.base.sbc.module.planning.vo.ColorPlanningListVO;
import com.base.sbc.module.planning.vo.ColorPlanningVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：颜色企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ColorPlanningService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:50
 */
public interface ColorPlanningService extends BaseService<ColorPlanning> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取颜色企划列表
     *
     * @param colorPlanningSearchDTO
     * @return
     */
    PageInfo<ColorPlanningListVO> getColorPlanningList(ColorPlanningSearchDTO colorPlanningSearchDTO);

    /**
     * 保存
     *
     * @param colorPlanningSaveDTO
     */
    String colorPlanningSave(ColorPlanningSaveDTO colorPlanningSaveDTO);

    /**
     * 详情
     *
     * @param id
     */
    ColorPlanningVO getDetailById(String id);

    /**
     * 通过产品季id统计
     *
     * @param planningSeasonId
     * @return
     */
    Long getColorPlanningCount(String planningSeasonId);

    /**
     * 通过产品季id获取
     *
     * @param planningSeasonId
     * @return
     */
    List<ColorPlanningListVO> getListByPlanningSeasonId(String planningSeasonId);


// 自定义方法区 不替换的区域【other_end】


}

