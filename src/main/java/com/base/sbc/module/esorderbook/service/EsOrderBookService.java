/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.esorderbook.dto.EsOrderBookQueryDto;
import com.base.sbc.module.esorderbook.entity.EsOrderBook;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：ES订货本 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.service.EsOrderBookService
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
public interface EsOrderBookService extends BaseService<EsOrderBook> {

// 自定义方法区 不替换的区域【other_start】

    PageInfo<EsOrderBookItemVo> findPage(EsOrderBookQueryDto dto);

// 自定义方法区 不替换的区域【other_end】


}