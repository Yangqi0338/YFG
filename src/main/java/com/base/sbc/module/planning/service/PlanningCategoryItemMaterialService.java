/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.PlanningCategoryItemMaterialSaveDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.sample.dto.StyleSaveDto;
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
public interface PlanningCategoryItemMaterialService extends BaseService<PlanningCategoryItemMaterial> {


 
    /**
     * 根据传入的素材id列表查询对应收藏的数量
     */
    List<Map<String,Integer>> numList(@Param("materialIds")List<String> materialIds);

    /**
     * 保存关联素材库
     *
     * @param dto
     * @return
     */
    boolean savePlanningCategoryItemMaterial(PlanningCategoryItemMaterialSaveDto dto);


    void saveMaterialList(StyleSaveDto dto);

    List<PlanningCategoryItemMaterial> findBySeatIds(List<String> ids);
}
