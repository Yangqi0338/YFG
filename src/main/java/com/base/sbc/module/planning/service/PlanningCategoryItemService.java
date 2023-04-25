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
import com.base.sbc.module.planning.entity.PlanningCategory;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;

import java.util.*;

/**
 * 类描述：企划-坑位信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */
public interface PlanningCategoryItemService extends IServicePlus<PlanningCategoryItem>{
    public int saveCategoryItem(PlanningBand band, PlanningCategory category,List<PlanningCategoryItem> dbCategoryItemList);
    @Transactional(readOnly = false)
    public boolean delByPlanningCategory(String companyCode,List<String> ids);
    public boolean delByPlanningBand(String userCompany, String id);

    String selectMaxDesignNo(QueryWrapper qc);

    List<String> selectCategoryIdsByBand(QueryWrapper qw);
}
