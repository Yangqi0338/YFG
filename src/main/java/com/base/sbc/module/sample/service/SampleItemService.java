/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：样衣明细 service类
 *
 * @address com.base.sbc.module.sample.service.SampleItemService
 */
public interface SampleItemService extends BaseService<SampleItem> {

    /**
     * 分页查询
     */
    PageInfo queryPageInfo(SamplePageDto dto);


    /**
     * 调整数量
     *
     * @param id:           样衣明细ID
     * @param type:         类型：1-借，2-还，3-销售，4-调拨(需要给 toPositionId )，5-盘点
     * @param count：        数量
     * @param toPositionId： 调入库的ID
     * @param toPosition：   调入库的名称
     */
    Boolean updateCount(String id, Integer type, Integer count, String toPositionId, String toPosition);


    List<SampleItem> getListBySampleId(SamplePageDto dto);

    /**
     * 获取样衣列表
     *
     * @param dto
     * @return
     */
    List<SampleItemVO> getSampleItemList(SampleSearchDTO dto);

    /**
     * 样衣归还
     *
     * @param id
     */
    void sampleReturnUpdateByIds(List<String> ids);

    /**
     * 校验样衣状态
     *
     * @param ids
     * @return
     */
    void checkSampleStatus(List<String> ids, List<String> checkStatus);
}

