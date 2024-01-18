/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service;

import com.base.sbc.module.column.entity.ColumnUserDefineItem;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/**
 * 类描述：用户级列自定义行 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.service.ColumnUserDefineItemService
 * @email your email
 * @date 创建时间：2023-12-6 17:33:01
 */
public interface ColumnUserDefineItemService extends BaseService<ColumnUserDefineItem> {

    List<ColumnUserDefineItem> findListByHeadId(String tableCode, String id);


}
