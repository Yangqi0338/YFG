/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 类描述：样衣管理 service类
 *
 * @address com.base.sbc.module.sample.service.SampleService
 */
public interface SampleService extends BaseService<Sample> {

    /**
     * 分页查询
     */
    PageInfo queryPageInfo(SamplePageDto dto);

    /**
     * 保存样衣及明细
     */
    SampleVo save(SampleSaveDto dto);

    /**
     * 查询明细数据
     */
    SampleVo getDetail(String id);

    Boolean importExcel(MultipartFile file) throws Exception;

    /**
     * 修改状态
     */
    SampleVo updateStatus(SampleSaveDto dto);


    /**
     * 获取样衣列表
     *
     * @param sampleSearchDTO
     * @return
     */
    PageInfo<SampleItemVO> getSampleItemList(SampleSearchDTO sampleSearchDTO);

    /**
     * 样衣借出
     *
     * @param sampleItems
     */
    void sampleBatchBorrow(List<SampleItem> sampleItems);


    /**
     * 样衣归还
     *
     * @param sampleItemIds
     * @param sampleIdMap
     */
    void sampleReturn(List<String> sampleItemIds, Map<String, Integer> sampleIdMap);


    /**
     * 样衣销售
     *
     * @param sampleItemId
     */
    void sampleSale(String sampleItemId);

    /**
     * 样衣调拨
     *
     * @param sampleItemId
     * @param position
     * @param positionId
     */
    void sampleAllocate(List<String> sampleItemId, String position, String positionId);

    /**
     * 样衣盘点
     *
     * @param sampleItemMap
     */
    void sampleInventory(Map<String, Integer> sampleItemMap);


}

