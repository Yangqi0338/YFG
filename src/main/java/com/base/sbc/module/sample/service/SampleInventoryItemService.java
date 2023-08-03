/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.entity.SampleInventoryItem;
import com.base.sbc.module.sample.vo.SampleInventoryItemVo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：样衣盘点明细 service类
 */
public interface SampleInventoryItemService extends BaseService<SampleInventoryItem> {

    List<SampleInventoryItemVo> getList(QueryWrapper qw);

    /**
     * 保存明细
     *
     * @param sampleInventoryItems
     * @param inventoryId
     * @param inventoryCode
     */
    void save(List<SampleInventoryItem> sampleInventoryItems, String inventoryId, String inventoryCode);

    /**
     * 批量获取样衣明细
     *
     * @param inventoryIds
     * @return
     */
    Map<String, Integer> getSampleItemIdsByInventoryIds(List<String> inventoryIds);
}

