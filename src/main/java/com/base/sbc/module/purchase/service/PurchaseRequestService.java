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
import com.base.sbc.module.purchase.entity.PurchaseRequest;

/** 
 * 类描述：采购申请单 service类
 * @address com.base.sbc.module.purchase.service.PurchaseRequestService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-18 15:59:21
 * @version 1.0  
 */
public interface PurchaseRequestService extends BaseService<PurchaseRequest>{
    ApiResult addPurchaseRequest(String companyCode, UserCompany userCompany, PurchaseRequest purchaseRequest);

    ApiResult updatePurchaseRequest(String companyCode, UserCompany userCompany, PurchaseRequest purchaseRequest);

    ApiResult cancel(String companyCode, String ids);

    void examinePass(UserCompany userCompany, AnswerDto dto);

    void examineNoPass(UserCompany userCompany, AnswerDto dto);

    void cancelExamine(UserCompany userCompany, AnswerDto dto);
}
