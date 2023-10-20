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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.review.entity.ReviewResult;
import com.base.sbc.module.review.entity.ReviewResultDetail;
import com.base.sbc.module.review.mapper.ReviewResultMapper;
import com.base.sbc.module.review.service.ReviewResultDetailService;
import com.base.sbc.module.review.service.ReviewResultService;
import com.base.sbc.module.review.vo.ReviewResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：评审会-评审结果配置 service类
 * @address com.base.sbc.module.review.service.ReviewResultService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 16:00:19
 * @version 1.0  
 */
@Service
public class ReviewResultServiceImpl extends BaseServiceImpl<ReviewResultMapper, ReviewResult> implements ReviewResultService {

    @Autowired
    private ReviewResultMapper reviewResultMapper;

    @Autowired
    private ReviewResultDetailService resultDetailService;

    @Override
    public ApiResult ReviewResultPage(String companyCode, Page page, String typeIds) {
        com.github.pagehelper.Page<ReviewResultVo> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
        BaseQueryWrapper<ReviewResult> qc = new BaseQueryWrapper<>();
        qc.eq("r.company_code", companyCode);
        if(StringUtils.isNoneBlank(typeIds)) {
            qc.in("r.meeting_type", StringUtils.convertList(typeIds));
        }

        if (!StringUtils.isEmpty(page.getOrderBy())){
            qc.orderByAsc(page.getOrderBy());
        }else {
            qc.orderByDesc("r.create_date");
        }

        reviewResultMapper.selectResultVo(qc);
        PageInfo<ReviewResultVo> pageList = pages.toPageInfo();
        if (CollectionUtil.isNotEmpty(pageList.getList())) {
            List<ReviewResultVo> list = pageList.getList();
            for(ReviewResultVo reviewResultVo : list){
                QueryWrapper<ReviewResultDetail> detailQc = new QueryWrapper<>();
                detailQc.eq("company_code", companyCode);
                detailQc.eq("result_id", reviewResultVo.getId());
                List<ReviewResultDetail> detailList = resultDetailService.list(detailQc);

                if(CollectionUtil.isNotEmpty(detailList)) {
                    StringBuilder passResult = new StringBuilder();
                    StringBuilder noPassResult = new StringBuilder();
                    for (ReviewResultDetail reviewResultDetail : detailList) {
                        if ("1".equals(reviewResultDetail.getReviewResult())) {
                            passResult.append(reviewResultDetail.getMeetingResult()).append(",");
                        } else {
                            noPassResult.append(reviewResultDetail.getMeetingResult()).append(",");
                        }
                    }
                    if(passResult.length() > 0) {
                        passResult.deleteCharAt(passResult.length() - 1);
                    }
                    if(noPassResult.length() > 0) {
                        noPassResult.deleteCharAt(noPassResult.length() - 1);
                    }

                    reviewResultVo.setPassResult(passResult.toString());
                    reviewResultVo.setNoPassResult(noPassResult.toString());
                }

                reviewResultVo.setPassThrough("是");
                reviewResultVo.setNoPass("否");
            }
            pageList.setList(list);
            return ApiResult.success("查询成功！",pageList);
        }
        return ApiResult.error("找不到数据！", 404);
    }

    /**
     * 新增评审结果配置
     * @param companyCode 企业编码
     * @param userCompany 用户信息
     * @param reviewResult 评审结果配置
     * */
    @Transactional(readOnly = false)
    public ApiResult addReviewResult(String companyCode, UserCompany userCompany, ReviewResult reviewResult){
        IdGen idGen = new IdGen();

        QueryWrapper<ReviewResult> qc = new QueryWrapper<>();
        qc.eq("company_code", companyCode);
        qc.eq("meeting_type", reviewResult.getMeetingType());
        List<ReviewResult> reviewResultList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewResultList)){
            return ApiResult.error("已用相同会议类型的评审结果配置，请修改！", 500);
        }

        String id = idGen.nextIdStr();
        reviewResult.insertInit(userCompany);
        reviewResult.setId(id);
        reviewResult.setCompanyCode(companyCode);
        reviewResult.setStatus("0");

        List<ReviewResultDetail> resultDetailList = reviewResult.getReviewResultDetailList();
        for(ReviewResultDetail detail : resultDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setCompanyCode(companyCode);
            detail.setResultId(id);
        }

        boolean i = save(reviewResult);
        if(i){
            resultDetailService.saveBatch(resultDetailList);
            return ApiResult.success("新增成功！", 200);
        }

        return ApiResult.error("新增失败！", 500);
    }

    /**
     * 修改评审结果配置
     * @param companyCode 企业编码
     * @param userCompany 用户信息
     * @param reviewResult 评审结果配置
     * */
    @Transactional(readOnly = false)
    public ApiResult updateReviewResult(String companyCode, UserCompany userCompany, ReviewResult reviewResult){
        QueryWrapper<ReviewResult> qc = new QueryWrapper<>();
        qc.eq("company_code", companyCode);
        qc.eq("meeting_type", reviewResult.getMeetingType());
        qc.ne("id", reviewResult.getId());
        List<ReviewResult> reviewResultList = list(qc);
        if(CollectionUtil.isNotEmpty(reviewResultList)){
            return ApiResult.error("已用相同会议类型的评审结果配置，请修改！", 500);
        }

        QueryWrapper<ReviewResultDetail> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("result_id", reviewResult.getId());
        resultDetailService.remove(qw);

        IdGen idGen = new IdGen();
        reviewResult.updateInit(userCompany);

        List<ReviewResultDetail> resultDetailList = reviewResult.getReviewResultDetailList();
        for(ReviewResultDetail detail : resultDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setCompanyCode(companyCode);
            detail.setResultId(reviewResult.getId());
        }

        boolean i = updateById(reviewResult);
        if(i){
            resultDetailService.saveBatch(resultDetailList);
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
    public ApiResult deleteOrder(String userCompany, String ids){
        QueryWrapper<ReviewResult> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany);
        qc.in("id", StringUtils.convertList(ids));
        boolean i = remove(qc);
        if(i){
            QueryWrapper<ReviewResultDetail> qw = new QueryWrapper<>();
            qw.eq("company_code", userCompany);
            qw.in("result_id", StringUtils.convertList(ids));
            resultDetailService.remove(qw);
            return ApiResult.success("删除成功！", 500);
        }
        return ApiResult.error("找不到数据！", 500);
    }
}
