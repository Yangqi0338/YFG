/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricPoolItemSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPoolItem;
import com.base.sbc.module.fabric.vo.FabricPoolItemVO;

import java.util.List;
import java.util.Map;

/**
 * 类描述：面料池明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPoolItemService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:45
 */
public interface FabricPoolItemService extends BaseService<FabricPoolItem> {

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 面料明细保存
     *
     * @param dto
     * @param fabricPoolId
     */
    void saveItem(List<FabricPoolItemSaveDTO> dto, String fabricPoolId);

    /**
     * 通过面料库id获取
     *
     * @param fabricPoolId
     * @return
     */
    List<FabricPoolItemVO> getByFabricPoolId(String fabricPoolId);

    /**
     * 通过面料企划id获取来源id
     * @param fabricPlanningId
     * @return
     */
    Map<String, List<String>> getSourceIdByFabricPlanningId(String fabricPlanningId);

// 自定义方法区 不替换的区域【other_end】


}
