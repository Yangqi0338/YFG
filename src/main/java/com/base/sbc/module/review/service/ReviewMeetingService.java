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
import com.base.sbc.module.review.entity.ReviewMeeting;

/** 
 * 类描述：评审会 service类
 * @address com.base.sbc.module.review.service.ReviewMeetingService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 17:06:30
 * @version 1.0  
 */
public interface ReviewMeetingService extends BaseService<ReviewMeeting>{

    ApiResult reviewMeetingHandleData(String companyCode, String id);

    ApiResult addReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeeting, Boolean addOrUpdate);

    ApiResult updateReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeeting, Boolean addOrUpdate);

    ApiResult deleteOrder(String userCompany, String ids);

    ApiResult batchAddReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeeting);
}
