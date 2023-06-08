/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.service;

import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.fieldManagement.entity.FieldVal;

import java.util.List;

/**
 * 类描述：字段值 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.fieldManagement.service.FieldValService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-22 19:41:56
 */
public interface FieldValService extends IServicePlus<FieldVal> {

/** 自定义方法区 不替换的区域【other_start】 **/


    /**
     * 通过fid 和dataGroup查找
     *
     * @param fid
     * @param dataGroup
     * @return
     */
    List<FieldVal> list(String fid, String dataGroup);

    /**
     * 保存
     *
     * @param fid
     * @param dataGroup
     * @param fieldVals
     * @return
     */
    boolean save(String fid, String dataGroup, List<FieldVal> fieldVals);


/** 自定义方法区 不替换的区域【other_end】 **/


}
