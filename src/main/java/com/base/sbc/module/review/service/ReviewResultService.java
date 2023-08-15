/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.review.entity.ReviewResult;
import com.base.sbc.config.common.base.Page;

/** 
 * 类描述：评审会-评审结果配置 service类
 * @address com.base.sbc.module.review.service.ReviewResultService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 16:00:19
 * @version 1.0  
 */
public interface ReviewResultService extends BaseService<ReviewResult>{

    ApiResult ReviewResultPage(String companyCode, Page page, String typeIds);

    ApiResult addReviewResult(String companyCode, UserCompany userCompany, ReviewResult reviewResult);

    ApiResult updateReviewResult(String companyCode, UserCompany userCompany, ReviewResult reviewResult);

    ApiResult deleteOrder(String userCompany, String ids);
}
