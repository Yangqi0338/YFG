/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.entity.PlanningDemandProportionSeat;
import com.base.sbc.module.style.vo.DemandOrderSkcVo;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：企划-需求占比坑位表 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandProportionSeatService
 * @email your email
 * @date 创建时间：2023-9-14 11:16:44
 */
public interface PlanningDemandProportionSeatService extends BaseService<PlanningDemandProportionSeat> {

// 自定义方法区 不替换的区域【other_start】

    void createByDemand(List<PlanningDemandProportionData> dataList);

    void delByPid(List<String> pid);

    List<PlanningDemandProportionSeat> findByPid(Collection<String> pdIds);

    boolean restSeatMatch(String id);

    boolean seatMatch(DemandOrderSkcVo bean);
// 自定义方法区 不替换的区域【other_end】


}

