/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricPlanningItemSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPlanningItem;
import com.base.sbc.module.fabric.vo.FabricPlanningItemVO;

import java.util.List;

/**
 * 类描述：面料企划明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPlanningItemService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:55
 */
public interface FabricPlanningItemService extends BaseService<FabricPlanningItem> {
    // 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param dto
     * @param fabricPlanningId
     */
    void saveItem(List<FabricPlanningItemSaveDTO> dto, String fabricPlanningId);

    /**
     * 通过面料企划id获取
     *
     * @param fabricPlanningId
     * @return
     */
    List<FabricPlanningItemVO> getByFabricPlanningId(String fabricPlanningId, String materialFlag);


// 自定义方法区 不替换的区域【other_end】


}
