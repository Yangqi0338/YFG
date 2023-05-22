/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.fieldManagement.dto.QueryFieldManagementDto;
import com.base.sbc.module.fieldManagement.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.fieldManagement.entity.FieldManagement;
import com.base.sbc.module.fieldManagement.vo.FieldManagementVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：字段管理表 service类
 * @address com.base.sbc.module.fieldManagement.service.FieldManagementService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:51
 * @version 1.0  
 */
public interface FieldManagementService extends IServicePlus<FieldManagement> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    ApiResult saveUpdateField(SaveUpdateFieldManagementDto saveUpdateFieldManagementDto);


    PageInfo<FieldManagementVo> getFieldManagementList(QueryFieldManagementDto queryFieldManagementDto);


    ApiResult adjustmentOrder(QueryFieldManagementDto queryFieldManagementDto);

    List<FieldManagementVo> getFieldManagementListByIds(List<String> ids);

/** 自定义方法区 不替换的区域【other_end】 **/


}
