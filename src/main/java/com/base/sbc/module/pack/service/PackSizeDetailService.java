/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.entity.PackSizeDetail;

import java.util.List;

/**
 * 类描述：资料包-尺寸表-明细 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeDetailService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-18 10:56:19
 */
public interface PackSizeDetailService extends PackBaseService<PackSizeDetail> {

// 自定义方法区 不替换的区域【other_start】

    void saveSizeDetail(List<PackSizeDetail> sizeDetails);

    void delBypackSizeIds(String id);


// 自定义方法区 不替换的区域【other_end】


}
