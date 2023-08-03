/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.entity.SampleAllocateItem;
import com.base.sbc.module.sample.vo.SampleAllocateItemVo;

import java.util.List;

/**
 * 类描述：样衣销售明细 service类
 */
public interface SampleAllocateItemService extends BaseService<SampleAllocateItem> {
    List<SampleAllocateItemVo> getList(SampleAllocatePageDto dto);

    /**
     * 保存样衣及明细
     */
    void save(List<SampleAllocateItem> sampleAllocateItems, String sampleAllocateId,
              String sampleAllocateCode, String position, String positionId);

}

