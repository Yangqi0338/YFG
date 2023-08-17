/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchase.entity.WarehousingOrder;

/** 
 * 类描述：入库单 service类
 * @address com.base.sbc.module.purchase.service.WarehousingOrderService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-9 16:21:43
 * @version 1.0  
 */
public interface WarehousingOrderService extends BaseService<WarehousingOrder>{
    ApiResult cancel(String companyCode, String ids) ;

    ApiResult addWarehousing(UserCompany userCompany, String companyCode, WarehousingOrder warehousingOrder);

    ApiResult updateWarehousing(UserCompany userCompany, String companyCode,WarehousingOrder warehousingOrder);

    void examinePass(UserCompany userCompany, AnswerDto dto);

    void examineNoPass(UserCompany userCompany, AnswerDto dto);

    void cancelExamine(UserCompany userCompany, AnswerDto dto);
}
