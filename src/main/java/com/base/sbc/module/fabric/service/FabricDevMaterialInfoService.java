/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricDevMaterialInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevMaterialInfo;

/**
 * 类描述：面料开发物料信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevMaterialInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:46
 */
public interface FabricDevMaterialInfoService extends BaseService<FabricDevMaterialInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 开发物料信息保存
     *
     * @param fabricDevMaterialInfoSave
     * @param bizId
     * @param companyCode
     */
    void devMaterialInfoSave(FabricDevMaterialInfoSaveDTO fabricDevMaterialInfoSave, String bizId, String companyCode);


// 自定义方法区 不替换的区域【other_end】


}
