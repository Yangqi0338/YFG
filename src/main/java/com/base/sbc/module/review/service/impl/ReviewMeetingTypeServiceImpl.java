/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.review.mapper.ReviewMeetingTypeMapper;
import com.base.sbc.module.review.entity.ReviewMeetingType;
import com.base.sbc.module.review.service.ReviewMeetingTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：评审会-会议类型表 service类
 * @address com.base.sbc.module.review.service.ReviewMeetingTypeService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 10:11:24
 * @version 1.0  
 */
@Service
public class ReviewMeetingTypeServiceImpl extends BaseServiceImpl<ReviewMeetingTypeMapper, ReviewMeetingType> implements ReviewMeetingTypeService {

    /**
     * 新增会议类型
     * @param companyCode 企业编码
     * @param userCompany 用户信息
     * @param reviewMeetingType 会议类型
     * */
    @Transactional(readOnly = false)
    public ApiResult addReviewMeetingType(String companyCode, UserCompany userCompany, ReviewMeetingType reviewMeetingType){
        IdGen idGen = new IdGen();

        QueryWrapper<ReviewMeetingType> qc = new QueryWrapper<>();
        qc.eq("name", reviewMeetingType.getName());
        qc.eq("is_leaf", reviewMeetingType.getIsLeaf());
        List<ReviewMeetingType> reviewMeetingTypeList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewMeetingTypeList)){
            return ApiResult.error("当前层级已用相同名称的会议类型，请修改！", 500);
        }

        String id = idGen.nextIdStr();
        reviewMeetingType.insertInit(userCompany);
        reviewMeetingType.setId(id);
        reviewMeetingType.setCompanyCode(companyCode);
//        reviewMeetingType.setStatus("1");
        reviewMeetingType.setDelFlag("0");

        boolean i = save(reviewMeetingType);
        if(i){
            return ApiResult.success("新增成功！", 200);
        }
        return ApiResult.error("新增失败！", 500);
    }

    /**
     * 修改会议类型
     * @param userCompany 用户信息
     * @param reviewMeetingType 会议类型
     * */
    @Transactional(readOnly = false)
    public ApiResult updateReviewMeetingType(UserCompany userCompany, ReviewMeetingType reviewMeetingType){
        QueryWrapper<ReviewMeetingType> qc = new QueryWrapper<>();
        qc.eq("name", reviewMeetingType.getName());
        qc.eq("is_leaf", reviewMeetingType.getIsLeaf());
        qc.ne("id", reviewMeetingType.getId());
        List<ReviewMeetingType> reviewMeetingTypeList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewMeetingTypeList)){
            return ApiResult.error("当前层级已用相同名称的会议类型，请修改！", 500);
        }

        reviewMeetingType.updateInit(userCompany);
        boolean i = updateById(reviewMeetingType);
        if(i){
            return ApiResult.success("修改成功！", 200);
        }
        return ApiResult.error("修改失败！", 500);
    }

    /**
     * 删除
     * @param userCompany
     * @param ids
     * */
    @Transactional(readOnly = false)
    public ApiResult batchDelete(String userCompany, String ids){
        QueryWrapper<ReviewMeetingType> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany);
        qc.in("id", StringUtils.convertList(ids));
        List<ReviewMeetingType> meetingTypeList = list(qc);

        for(ReviewMeetingType type : meetingTypeList){
            if("1".equals(type.getStatus())){
                return ApiResult.error("不能删除启用状态的会议类型！", 500);
            }
        }

        boolean i = remove(qc);
        if(i){
            return ApiResult.success("删除成功！", 200);
        }
        return ApiResult.error("找不到数据！", 500);
    }
	
}
