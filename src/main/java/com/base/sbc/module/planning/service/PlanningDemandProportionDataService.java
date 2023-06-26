/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.SaveUpdateDemandProportionDataDto;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;

/**
 * 类描述：企划-需求维度数据表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandDimensionalityDataService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:22
 */
public interface PlanningDemandProportionDataService extends BaseService<PlanningDemandProportionData> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    ApiResult saveUpdate(SaveUpdateDemandProportionDataDto saveUpdateDemandDimensionalityDataDto);

    ApiResult del(String id);
/** 自定义方法区 不替换的区域【other_end】 **/


}
