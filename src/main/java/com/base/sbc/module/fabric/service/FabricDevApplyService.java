/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricDevApplyAllocationDTO;
import com.base.sbc.module.fabric.dto.FabricDevApplySaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevApply;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.base.sbc.module.fabric.vo.FabricDevApplyVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料开发申请 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevApplyService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:28
 */
public interface FabricDevApplyService extends BaseService<FabricDevApply> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param fabricDevApplySaveDTO
     */
    FabricDevApplyVO devAppSave(FabricDevApplySaveDTO fabricDevApplySaveDTO);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    FabricDevApplyVO getDetail(String id);

    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    PageInfo<FabricDevApplyListVO> getDevApplyList(FabricDevSearchDTO dto);

    /**
     * 分配任务
     *
     * @param fabricDevApplyAssignDTO
     */
    void allocationTasks(FabricDevApplyAllocationDTO fabricDevApplyAssignDTO);

    /**
     * 通过开发申请单号更新分配状态
     *
     * @param devApplyCode
     * @param allocationStatus
     */
    void updateAllocationStatus(String devApplyCode, String allocationStatus);

    /**
     * 通过开发申请单号获取id
     *
     * @param devApplyCode
     * @return
     */
    String getByDevApplyCode(String devApplyCode);


// 自定义方法区 不替换的区域【other_end】


}
