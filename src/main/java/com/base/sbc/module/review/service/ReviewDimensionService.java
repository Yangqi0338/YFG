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
import com.base.sbc.module.review.entity.ReviewDimension;

/** 
 * 类描述：评审会-评审维度配置 service类
 * @address com.base.sbc.module.review.service.ReviewDimensionService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 15:40:34
 * @version 1.0  
 */
public interface ReviewDimensionService extends BaseService<ReviewDimension>{

    ApiResult addReviewDimension(String companyCode, UserCompany userCompany, ReviewDimension reviewDimension);

    ApiResult updateReviewDimension(UserCompany userCompany, ReviewDimension reviewDimension);

    ApiResult batchDelete(String userCompany, String ids);
}
