/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.entity.PackInfoStatus;

/**
 * 类描述：资料包-状态 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoStatusService
 * @email your email
 * @date 创建时间：2023-7-13 9:17:47
 */
public interface PackInfoStatusService extends PackBaseService<PackInfoStatus> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 资料包状态
     *
     * @param foreignId
     * @param packType
     * @return
     */
    PackInfoStatus newStatus(String foreignId, String packType);

    PackInfoStatus get(String foreignId, String packType);

// 自定义方法区 不替换的区域【other_end】


}

