/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.CheckMutexDto;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.dto.SaveDelDemandDto;
import com.base.sbc.module.planning.dto.SyncToSeatDto;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.vo.PlanningDemandVo;

import java.security.Principal;
import java.util.List;

/** 
 * 类描述：企划-需求维度表 service类
 * @address com.base.sbc.module.planning.service.PlanningDemandDimensionalityService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18
 * @version 1.0  
 */
public interface PlanningDemandService extends BaseService<PlanningDemand> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<PlanningDemandVo> getDemandListById(Principal user, QueryDemandDto queryDemandDimensionalityDto);


    ApiResult getFormDemand(QueryDemandDto queryDemandDimensionalityDto);


    /*新增修改*/
    ApiResult saveDel(List<SaveDelDemandDto> saveDelDemandDtos);

    /**
     * 删除需求占比
     *
     * @param id
     * @return
     */
    Boolean delDemand(String id);

    boolean setImportantFlag(PlanningDemand planningDemand);

    boolean syncToSeat(SyncToSeatDto dto);

    /**
     * 增加检查互斥品类和中类互斥
     */
    String checkMutex(CheckMutexDto checkMutexDto);

/** 自定义方法区 不替换的区域【other_end】 **/


}
