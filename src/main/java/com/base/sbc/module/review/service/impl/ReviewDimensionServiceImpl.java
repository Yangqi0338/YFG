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
import com.base.sbc.module.review.mapper.ReviewDimensionMapper;
import com.base.sbc.module.review.entity.ReviewDimension;
import com.base.sbc.module.review.service.ReviewDimensionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：评审会-评审维度配置 service类
 * @address com.base.sbc.module.review.service.ReviewDimensionService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 15:40:34
 * @version 1.0  
 */
@Service
public class ReviewDimensionServiceImpl extends BaseServiceImpl<ReviewDimensionMapper, ReviewDimension> implements ReviewDimensionService {

    /**
     * 新增评审维度配置
     * @param companyCode 企业编码
     * @param userCompany 用户信息
     * @param reviewDimension 评审维度配置
     * */
    @Transactional(readOnly = false)
    public ApiResult addReviewDimension(String companyCode, UserCompany userCompany, ReviewDimension reviewDimension){
        QueryWrapper<ReviewDimension> qc = new QueryWrapper<>();
        qc.eq("company_code", companyCode);
        qc.eq("department_id", reviewDimension.getDepartmentId());
        qc.eq("review_dimension", reviewDimension.getReviewDimension());
        List<ReviewDimension> reviewDimensionList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewDimensionList)){
            return ApiResult.error("该部门有相同的评审维度配置，请修改！", 500);
        }

        IdGen idGen = new IdGen();

        String id = idGen.nextIdStr();
        reviewDimension.insertInit(userCompany);
        reviewDimension.setId(id);
        reviewDimension.setCompanyCode(companyCode);
        reviewDimension.setStatus("0");
        reviewDimension.setDelFlag("0");

        boolean i = save(reviewDimension);
        if(i){
            return ApiResult.success("新增成功！", 200);
        }
        return ApiResult.error("新增失败！", 500);
    }

    /**
     * 修改评审维度配置
     * @param userCompany 用户信息
     * @param reviewDimension 评审维度配置
     * */
    @Transactional(readOnly = false)
    public ApiResult updateReviewDimension(UserCompany userCompany, ReviewDimension reviewDimension){
        QueryWrapper<ReviewDimension> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany.getCompanyCode());
        qc.eq("department_id", reviewDimension.getDepartmentId());
        qc.eq("review_dimension", reviewDimension.getReviewDimension());
        qc.ne("id", reviewDimension.getId());
        List<ReviewDimension> reviewDimensionList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewDimensionList)){
            return ApiResult.error("该部门有相同的评审维度配置，请修改！", 500);
        }

        reviewDimension.updateInit(userCompany);
        boolean i = updateById(reviewDimension);
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
        QueryWrapper<ReviewDimension> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany);
        qc.in("id", StringUtils.convertList(ids));
        boolean i = remove(qc);
        if(i){
            return ApiResult.success("删除成功！", 500);
        }
        return ApiResult.error("找不到数据！", 500);
    }
}
