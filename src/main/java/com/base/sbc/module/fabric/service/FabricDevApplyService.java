/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricDevApplySaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevApplySearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevApply;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料开发申请（主表） service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevApplyService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:20
 */
public interface FabricDevApplyService extends BaseService<FabricDevApply> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取开发申请列表
     *
     * @param dto
     * @return
     */
    PageInfo<FabricDevApplyListVO> getDevApplyList(FabricDevApplySearchDTO dto);

    /**
     * 保存
     *
     * @param dto
     */
    String devApplySave(FabricDevApplySaveDTO dto);


// 自定义方法区 不替换的区域【other_end】


}
