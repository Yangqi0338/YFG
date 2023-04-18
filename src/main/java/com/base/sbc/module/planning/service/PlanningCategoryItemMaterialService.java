/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.vo.PlanningSeasonBandVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类描述：企划-坑位关联的素材库表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:47:43
 */
public interface PlanningCategoryItemMaterialService extends IServicePlus<PlanningCategoryItemMaterial> {


 
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     */
    List<Map<String,Integer>> numList(@Param("materialIds")List<String> materialIds);
}
