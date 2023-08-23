/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricDevApplyAllocationDTO;
import com.base.sbc.module.fabric.dto.FabricDevMainSaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevMainInfo;
import com.base.sbc.module.fabric.vo.FabricDevApplyVO;
import com.base.sbc.module.fabric.vo.FabricDevMainListVO;
import com.base.sbc.module.fabric.vo.FabricDevMainVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料开发主信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevMainInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:58:04
 */
public interface FabricDevMainInfoService extends BaseService<FabricDevMainInfo> {
    // 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param fabricDevApplyVO
     * @param fabricDevApplyAllocationDTO
     */
    void saveDevMainInfo(FabricDevApplyVO fabricDevApplyVO, FabricDevApplyAllocationDTO fabricDevApplyAllocationDTO);


    /**
     * 获取开发列表信息
     *
     * @param dto
     * @return
     */
    PageInfo<FabricDevMainListVO> getDevList(FabricDevSearchDTO dto);


    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    FabricDevMainVO getDetail(String id);

    /**
     * 修改
     *
     * @param fabricDevMainSaveDTO
     */
    void update(FabricDevMainSaveDTO fabricDevMainSaveDTO);

    /**
     * 更新开发状态
     *
     * @param id
     * @param status
     */
    void updateDevStatus(String id, String devId, String status);


// 自定义方法区 不替换的区域【other_end】


}
