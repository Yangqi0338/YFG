/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.service;

import com.base.sbc.module.column.dto.ColumnGroupDefineDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.entity.ColumnGroupDefine;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/** 
 * 类描述： service类
 * @address com.base.sbc.module.column.service.ColumnGroupDefineService
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-11 10:55:06
 * @version 1.0  
 */
public interface ColumnGroupDefineService extends BaseService<ColumnGroupDefine>{

    List<ColumnDefine> findDetail(String tableCode, String userGroupId);

    List<ColumnDefine> findDetailByJoblist(String tableCode, List<String> userGroupId);

    void saveMain(ColumnGroupDefineDto dto);

    List<ColumnDefine> findDetail(String tableCode, String columnCode, String userGroupId);

}
