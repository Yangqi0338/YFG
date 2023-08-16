/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.RedisCodeGenUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.review.dto.ReviewMeetingStyleDTO;
import com.base.sbc.module.review.entity.*;
import com.base.sbc.module.review.mapper.ReviewMeetingLogMapper;
import com.base.sbc.module.review.mapper.ReviewMeetingMapper;
import com.base.sbc.module.review.service.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：评审会 service类
 * @address com.base.sbc.module.review.service.ReviewMeetingService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 17:06:30
 * @version 1.0  
 */
@Service
public class ReviewMeetingServiceImpl extends BaseServiceImpl<ReviewMeetingMapper, ReviewMeeting> implements ReviewMeetingService {

    @Autowired
    private ReviewMeetingLogService reviewMeetingLogService;

    @Autowired
    private ReviewMeetingLogDetailService meetingLogDetailService;

    @Autowired
    private ReviewMeetingLogFileService meetingLogFileService;

    @Autowired
    private ReviewMeetingDepartmentService meetingDepartmentService;

    @Autowired
    private ReviewMeetingLogMapper reviewMeetingLogMapper;

    @Autowired
    private ReviewMeetingMapper reviewMeetingMapper;

    @Autowired
    private RedisCodeGenUtils redisCodeGenUtils;

    /**
     * 评审会 数据处理
     */
    public ApiResult reviewMeetingHandleData(String companyCode, String id) {
        ReviewMeeting reviewMeeting = getById(id);
        if (reviewMeeting != null) {
            //会议附件
            List<ReviewMeetingLogFile> meetingLogFileList = new ArrayList<>();
            //引用附件
            List<ReviewMeetingLogFile> quoteLogFileList = new ArrayList<>();

            //会议记录
            List<ReviewMeetingLog> meetingLogList = new ArrayList<>();
            //引用记录
            List<ReviewMeetingLog> quoteLogList = new ArrayList<>();

            BaseQueryWrapper<ReviewMeetingLog> logQc = new BaseQueryWrapper<>();
            logQc.eq("company_code", companyCode);
            logQc.eq("meeting_id", id);
            List<ReviewMeetingLog> allMeetingLogList = reviewMeetingLogMapper.selectMeetingLogRelation(logQc);

            for (ReviewMeetingLog log : allMeetingLogList) {
                if ("0".equals(log.getType())) {
                    if(log.getReviewMeetingLogFile() != null) {
                        meetingLogFileList.add(log.getReviewMeetingLogFile());
                    }
                    meetingLogList.add(log);
                } else {
                    if(log.getReviewMeetingLogFile() != null) {
                        quoteLogFileList.add(log.getReviewMeetingLogFile());
                    }
                    quoteLogList.add(log);
                }
            }

            QueryWrapper<ReviewMeetingDepartment> departMentQw = new QueryWrapper<>();
            departMentQw.eq("company_code", companyCode);
            departMentQw.eq("meeting_id", id);
            List<ReviewMeetingDepartment> meetingDepartmentList = meetingDepartmentService.list(departMentQw);

            reviewMeeting.setMeetingDepartmentList(meetingDepartmentList);
            reviewMeeting.setMeetingLogList(meetingLogList);
            reviewMeeting.setQuoteLogList(quoteLogList);

            Map<String, Object> attributesMap = new HashMap<>();
            attributesMap.put("meetingLogFileList", meetingLogFileList);
            attributesMap.put("quoteLogFileList", quoteLogFileList);
            return ApiResult.success("查询成功!", reviewMeeting, attributesMap);
        }
        return ApiResult.error("找不到数据！", 500);
    }

    @Transactional(readOnly = false)
    public ApiResult addReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeeting, Boolean addOrUpdate) {
        IdGen idGen = new IdGen();

        String id = addOrUpdate ? idGen.nextIdStr() : reviewMeeting.getId();
        reviewMeeting.insertInit(userCompany);
        reviewMeeting.setId(id);
        reviewMeeting.setCompanyCode(companyCode);
        reviewMeeting.setDelFlag("0");
        reviewMeeting.setStatus("0");
        String maxCode = reviewMeetingMapper.selectMaxCodeByCompany(companyCode);
        String code = "PSH" + CodeGen.getBoxCode(3, maxCode != null ? maxCode : CodeGen.BEGIN_NUM);
        String meetingNo = redisCodeGenUtils.getCode_MMDD00(companyCode, "reviewMeetingTwo", "",true, false);
        reviewMeeting.setCode(code);
        reviewMeeting.setMeetingNo(meetingNo);

        //部门信息
        List<ReviewMeetingDepartment> addMeetingDepartmentList = new ArrayList<>();
        List<ReviewMeetingDepartment> meetingDepartmentList = reviewMeeting.getMeetingDepartmentList();
        for (ReviewMeetingDepartment department : meetingDepartmentList) {
            department.setId(idGen.nextIdStr());
            department.setCompanyCode(companyCode);
            department.setMeetingId(id);
            addMeetingDepartmentList.add(department);
        }

        List<ReviewMeetingLog> addMeetingLogList = new ArrayList<>();
        List<ReviewMeetingLogDetail> addMeetingLogDetailList = new ArrayList<>();
        List<ReviewMeetingLogFile> addMeetingLogFileList = new ArrayList<>();
        //会议记录
        List<ReviewMeetingLog> reviewMeetingLogList = reviewMeeting.getMeetingLogList();
        if(CollectionUtil.isNotEmpty(reviewMeetingLogList)) {
            for (ReviewMeetingLog reviewMeetingLog : reviewMeetingLogList) {
                String logId = idGen.nextIdStr();
                reviewMeetingLog.setId(logId);
                reviewMeetingLog.setCompanyCode(companyCode);
                reviewMeetingLog.setMeetingId(id);
                reviewMeetingLog.setType("0");

                //维度信息
                for (ReviewMeetingLogDetail detail : reviewMeetingLog.getMeetingLogDetailList()) {
                    detail.setId(idGen.nextIdStr());
                    detail.setCompanyCode(companyCode);
                    detail.setMeetingLogId(logId);
                    detail.setMeetingId(id);
                    addMeetingLogDetailList.add(detail);
                }

                //附件
                if (reviewMeetingLog.getReviewMeetingLogFile() != null) {
                    ReviewMeetingLogFile reviewMeetingLogFile = reviewMeetingLog.getReviewMeetingLogFile();
                    reviewMeetingLogFile.insertInit(userCompany);
                    reviewMeetingLogFile.setId(idGen.nextIdStr());
                    reviewMeetingLogFile.setCompanyCode(companyCode);
                    reviewMeetingLogFile.setMeetingId(id);
                    reviewMeetingLogFile.setLogId(logId);
                    addMeetingLogFileList.add(reviewMeetingLogFile);
                }

                addMeetingLogList.add(reviewMeetingLog);
            }
        }

        //引用信息
        List<ReviewMeetingLog> quoteLogList = reviewMeeting.getQuoteLogList();
        if(CollectionUtil.isNotEmpty(quoteLogList)) {
            for (ReviewMeetingLog reviewMeetingLog : quoteLogList) {
                String logId = idGen.nextIdStr();
                reviewMeetingLog.setId(logId);
                reviewMeetingLog.setCompanyCode(companyCode);
                reviewMeetingLog.setMeetingId(id);
                reviewMeetingLog.setType("0");

                //维度信息
                for (ReviewMeetingLogDetail detail : reviewMeetingLog.getMeetingLogDetailList()) {
                    detail.setId(idGen.nextIdStr());
                    detail.setCompanyCode(companyCode);
                    detail.setMeetingLogId(logId);
                    detail.setMeetingId(id);
                    addMeetingLogDetailList.add(detail);
                }

                //附件
                if(reviewMeetingLog.getReviewMeetingLogFile() != null) {
                    ReviewMeetingLogFile reviewMeetingLogFile = reviewMeetingLog.getReviewMeetingLogFile();
                    reviewMeetingLogFile.insertInit(userCompany);
                    reviewMeetingLogFile.setId(idGen.nextIdStr());
                    reviewMeetingLogFile.setCompanyCode(companyCode);
                    reviewMeetingLogFile.setMeetingId(id);
                    reviewMeetingLogFile.setLogId(logId);
                    addMeetingLogFileList.add(reviewMeetingLogFile);
                }

                addMeetingLogList.add(reviewMeetingLog);
            }
        }

        if(CollectionUtil.isNotEmpty(addMeetingDepartmentList)){
            meetingDepartmentService.saveBatch(addMeetingDepartmentList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogList)){
            reviewMeetingLogService.saveBatch(addMeetingLogList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogDetailList)){
            meetingLogDetailService.saveBatch(addMeetingLogDetailList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogFileList)){
            meetingLogFileService.saveBatch(addMeetingLogFileList);
        }

        boolean i = false;
        if(addOrUpdate) {
            i = save(reviewMeeting);
        }
        if(i){
            return ApiResult.success("新增成功！", reviewMeeting);
        }
        return ApiResult.error("新增失败！", 500);
    }

    @Transactional(readOnly = false)
    public ApiResult updateReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeeting, Boolean addOrUpdate){
        //删除相关明细数据
        deleteDetailData(companyCode, reviewMeeting.getId());

        reviewMeeting.updateInit(userCompany);
        boolean i = updateById(reviewMeeting);
        if(i){
            addReviewMeeting(companyCode, userCompany, reviewMeeting, addOrUpdate);
            return ApiResult.success("修改成功！", reviewMeeting);
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
        QueryWrapper<ReviewMeeting> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany);
        qc.in("id", StringUtils.convertList(ids));
        Integer i = physicalDeleteQWrap(qc);
        if(i > 0){
            deleteDetailData(userCompany, ids);
            return ApiResult.success("删除成功！", 500);
        }
        return ApiResult.error("找不到数据！", 500);
    }

    @Override
    public ApiResult batchAddReviewMeeting(String companyCode, UserCompany userCompany, ReviewMeeting reviewMeetings) {
        IdGen idGen = new IdGen();
        String maxCode = reviewMeetingMapper.selectMaxCodeByCompany(companyCode);

        List<ReviewMeeting> reviewMeetingList = new ArrayList<>();
        List<ReviewMeetingDepartment> addMeetingDepartmentList = new ArrayList<>();
        List<ReviewMeetingLog> addMeetingLogList = new ArrayList<>();
        List<ReviewMeetingLogDetail> addMeetingLogDetailList = new ArrayList<>();
        List<ReviewMeetingLogFile> addMeetingLogFileList = new ArrayList<>();

        List<ReviewMeetingStyleDTO> styleList = reviewMeetings.getStyleList();
        for(ReviewMeetingStyleDTO style : styleList) {
            ReviewMeeting reviewMeeting = new ReviewMeeting();
            BeanUtils.copyProperties(reviewMeetings, reviewMeeting);
            String id = idGen.nextIdStr();
            reviewMeeting.insertInit(userCompany);
            reviewMeeting.setId(id);
            reviewMeeting.setCompanyCode(companyCode);
            reviewMeeting.setDelFlag("0");
            reviewMeeting.setStatus("0");

            String code = "PSH" + CodeGen.getBoxCode(3, maxCode != null ? maxCode : CodeGen.BEGIN_NUM);
            String meetingNo = redisCodeGenUtils.getCode_MMDD00(companyCode, "reviewMeetingTwo", "", true, false);
            reviewMeeting.setCode(code);
            reviewMeeting.setMeetingNo(meetingNo);
            maxCode = code;

            reviewMeeting.setStyleNo(style.getStyleNo());
            reviewMeeting.setPlateBillId(style.getPlateBillId());
            reviewMeeting.setPlateBillCode(style.getPlateBillCode());
            reviewMeeting.setPictureUrl(style.getPictureUrl());

            //部门信息
            List<ReviewMeetingDepartment> meetingDepartmentList = reviewMeetings.getMeetingDepartmentList();
            for (ReviewMeetingDepartment department : meetingDepartmentList) {
                department.setId(idGen.nextIdStr());
                department.setCompanyCode(companyCode);
                department.setMeetingId(id);
                addMeetingDepartmentList.add(department);
            }

            //会议记录
            List<ReviewMeetingLog> reviewMeetingLogList = reviewMeetings.getMeetingLogList();
            if (CollectionUtil.isNotEmpty(reviewMeetingLogList)) {
                for (ReviewMeetingLog reviewMeetingLog : reviewMeetingLogList) {
                    String logId = idGen.nextIdStr();
                    reviewMeetingLog.setId(logId);
                    reviewMeetingLog.setCompanyCode(companyCode);
                    reviewMeetingLog.setMeetingId(id);
                    reviewMeetingLog.setType("0");

                    //维度信息
                    for (ReviewMeetingLogDetail detail : reviewMeetingLog.getMeetingLogDetailList()) {
                        detail.setId(idGen.nextIdStr());
                        detail.setCompanyCode(companyCode);
                        detail.setMeetingLogId(logId);
                        detail.setMeetingId(id);
                        addMeetingLogDetailList.add(detail);
                    }

                    //附件
                    if (reviewMeetingLog.getReviewMeetingLogFile() != null) {
                        ReviewMeetingLogFile reviewMeetingLogFile = reviewMeetingLog.getReviewMeetingLogFile();
                        reviewMeetingLogFile.insertInit(userCompany);
                        reviewMeetingLogFile.setId(idGen.nextIdStr());
                        reviewMeetingLogFile.setCompanyCode(companyCode);
                        reviewMeetingLogFile.setMeetingId(id);
                        reviewMeetingLogFile.setLogId(logId);
                        addMeetingLogFileList.add(reviewMeetingLogFile);
                    }

                    addMeetingLogList.add(reviewMeetingLog);
                }
            }

            //引用信息
            List<ReviewMeetingLog> quoteLogList = reviewMeetings.getQuoteLogList();
            if (CollectionUtil.isNotEmpty(quoteLogList)) {
                for (ReviewMeetingLog reviewMeetingLog : quoteLogList) {
                    String logId = idGen.nextIdStr();
                    reviewMeetingLog.setId(logId);
                    reviewMeetingLog.setCompanyCode(companyCode);
                    reviewMeetingLog.setMeetingId(id);
                    reviewMeetingLog.setType("0");

                    //维度信息
                    for (ReviewMeetingLogDetail detail : reviewMeetingLog.getMeetingLogDetailList()) {
                        detail.setId(idGen.nextIdStr());
                        detail.setCompanyCode(companyCode);
                        detail.setMeetingLogId(logId);
                        detail.setMeetingId(id);
                        addMeetingLogDetailList.add(detail);
                    }

                    //附件
                    if (reviewMeetingLog.getReviewMeetingLogFile() != null) {
                        ReviewMeetingLogFile reviewMeetingLogFile = reviewMeetingLog.getReviewMeetingLogFile();
                        reviewMeetingLogFile.insertInit(userCompany);
                        reviewMeetingLogFile.setId(idGen.nextIdStr());
                        reviewMeetingLogFile.setCompanyCode(companyCode);
                        reviewMeetingLogFile.setMeetingId(id);
                        reviewMeetingLogFile.setLogId(logId);
                        addMeetingLogFileList.add(reviewMeetingLogFile);
                    }

                    addMeetingLogList.add(reviewMeetingLog);
                }
            }

            reviewMeetingList.add(reviewMeeting);
        }

        if(CollectionUtil.isNotEmpty(addMeetingDepartmentList)){
            meetingDepartmentService.saveBatch(addMeetingDepartmentList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogList)){
            reviewMeetingLogService.saveBatch(addMeetingLogList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogDetailList)){
            meetingLogDetailService.saveBatch(addMeetingLogDetailList);
        }
        if(CollectionUtil.isNotEmpty(addMeetingLogFileList)){
            meetingLogFileService.saveBatch(addMeetingLogFileList);
        }

        boolean i = saveBatch(reviewMeetingList);
        if(i){
            return ApiResult.success("新增成功！", i);
        }
        return ApiResult.error("新增失败！", 500);
    }

    public void deleteDetailData(String companyCode, String ids){
        QueryWrapper<ReviewMeetingDepartment> departQw = new QueryWrapper<>();
        departQw.eq("company_code", companyCode);
        departQw.in("meeting_id", StringUtils.convertList(ids));

        QueryWrapper<ReviewMeetingLog> logQw = new QueryWrapper<>();
        logQw.eq("company_code", companyCode);
        logQw.in("meeting_id", StringUtils.convertList(ids));

        QueryWrapper<ReviewMeetingLogDetail> logDetailQw = new QueryWrapper<>();
        logDetailQw.eq("company_code", companyCode);
        logDetailQw.in("meeting_id", StringUtils.convertList(ids));

        QueryWrapper<ReviewMeetingLogFile> logFileQw = new QueryWrapper<>();
        logFileQw.eq("company_code", companyCode);
        logFileQw.in("meeting_id", StringUtils.convertList(ids));

        meetingDepartmentService.physicalDeleteQWrap(departQw);
        reviewMeetingLogService.physicalDeleteQWrap(logQw);
        meetingLogDetailService.physicalDeleteQWrap(logDetailQw);
        meetingLogFileService.physicalDeleteQWrap(logFileQw);
    }
}
