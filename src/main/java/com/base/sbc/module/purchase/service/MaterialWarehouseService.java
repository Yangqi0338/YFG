/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchase.entity.MaterialWarehouse;

/** 
 * 类描述：物料仓库 service类
 * @address com.base.sbc.module.purchase.service.MaterialWarehouseService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 14:46:11
 * @version 1.0  
 */
public interface MaterialWarehouseService extends BaseService<MaterialWarehouse>{
    ApiResult addWarehouse(UserCompany userCompany, String companyCode, MaterialWarehouse materialWarehouse);

    ApiResult updateWarehouse(UserCompany userCompany, String companyCode, MaterialWarehouse materialWarehouse);
}

