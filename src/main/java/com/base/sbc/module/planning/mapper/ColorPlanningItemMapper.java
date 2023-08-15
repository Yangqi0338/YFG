/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planning.entity.ColorPlanningItem;
import com.base.sbc.module.planning.vo.ColorPlanningItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：颜色企划明细 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.dao.ColorPlanningItemDao
 * @email your email
 * @date 创建时间：2023-8-15 13:58:55
 */
@Mapper
public interface ColorPlanningItemMapper extends BaseMapper<ColorPlanningItem> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过颜色企划id获取
     *
     * @param colorPlanningId
     * @return
     */
    List<ColorPlanningItemVO> getBYColorPlanningId(@Param("colorPlanningId") String colorPlanningId);


// 自定义方法区 不替换的区域【other_end】
}

