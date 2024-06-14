/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/** 
 * 类描述：基础资料-物料档案-物料颜色 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:22
 * @version 1.0  
 */
public interface BasicsdatumMaterialColorService extends BaseService<BasicsdatumMaterialColor> {

// 自定义方法区 不替换的区域【other_start】
    /**
     * 通过物料编码复制
     * @param materialCode
     * @return
     */
    void copyByMaterialCode(String materialCode, String newMaterialCode);


    /**
     * 更新物料编码
     * @param oldMaterialCode
     * @param newMaterialCode
     */
    void updateMaterialCode(String oldMaterialCode, String newMaterialCode);

    List<BasicsdatumMaterialColor> getBasicsdatumMaterialColorCodeList(String materialCode, String colorCode);

// 自定义方法区 不替换的区域【other_end】

	
}

