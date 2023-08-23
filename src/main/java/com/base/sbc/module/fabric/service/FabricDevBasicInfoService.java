/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricDevBasicInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevBasicInfo;
import com.base.sbc.module.fabric.vo.FabricDevBasicInfoVO;

/**
 * 类描述：面料开发基本信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevBasicInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:34
 */
public interface FabricDevBasicInfoService extends BaseService<FabricDevBasicInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param fabricDevBasicInfoSaveDTO
     * @param bizId
     * @return
     */
    FabricDevBasicInfoVO saveBasicInfo(FabricDevBasicInfoSaveDTO fabricDevBasicInfoSaveDTO, String bizId);

    /**
     * 通过业务id查询
     *
     * @param bizId
     * @return
     */
    FabricDevBasicInfoVO getByBizId(String bizId);

    /**
     * 同步至物料档案修改
     *
     * @param bizId
     * @param devId
     * @param devApplyId
     * @param toMaterialId
     * @param toMaterialFlag
     */
    void synchMaterialUpdate(String bizId, String devId, String devApplyId, String toMaterialId, String toMaterialFlag);


// 自定义方法区 不替换的区域【other_end】


}
