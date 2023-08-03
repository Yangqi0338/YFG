/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleInventoryPageDto;
import com.base.sbc.module.sample.dto.SampleInventorySaveDto;
import com.base.sbc.module.sample.entity.SampleInventory;
import com.base.sbc.module.sample.vo.SampleInventoryVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：样衣盘点 service类
 *
 * @address com.base.sbc.module.sample.service.SampleInventoryService
 */
public interface SampleInventoryService extends BaseService<SampleInventory> {

    /**
     * 分页查询
     */
    PageInfo queryPageInfo(SampleInventoryPageDto dto);

    /**
     * 分页查询-样衣明细维度
     */
    PageInfo getListBySampleItem(SampleInventoryPageDto dto);

    /**
     * 保存盘点及明细
     */
    String save(SampleInventorySaveDto dto);

    /**
     * 开始盘点
     *
     * @param inventoryIds
     */
    void startInventory(List<String> inventoryIds);

    /**
     * 结束盘点
     *
     * @param inventoryIds
     */
    void endInventory(List<String> inventoryIds);

    /**
     * 作废
     *
     * @param inventoryIds
     */
    void cancel(List<String> inventoryIds);

    /**
     * 查询明细数据
     */
    SampleInventoryVo getDetail(String id);

    SampleInventoryVo updateStatus(SampleInventorySaveDto dto);
}

