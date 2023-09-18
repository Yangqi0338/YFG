/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.entity.PurchaseRequest;
import com.base.sbc.module.purchase.entity.PurchaseRequestDetail;
import com.base.sbc.module.purchase.mapper.PurchaseRequestMapper;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.base.sbc.module.purchase.service.PurchaseRequestDetailService;
import com.base.sbc.module.purchase.service.PurchaseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：采购申请单 service类
 * @address com.base.sbc.module.purchase.service.PurchaseRequestService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-18 15:59:21
 * @version 1.0  
 */
@Service
public class PurchaseRequestServiceImpl extends BaseServiceImpl<PurchaseRequestMapper, PurchaseRequest> implements PurchaseRequestService {
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    @Autowired
    private PurchaseRequestDetailService requestDetailService;

    @Autowired
    private PurchaseDemandService purchaseDemandService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult addPurchaseRequest(String companyCode, UserCompany userCompany, PurchaseRequest purchaseRequest) {
        IdGen idGen = new IdGen();

        String id = idGen.nextIdStr();
        String maxCode = purchaseRequestMapper.selectMaxCodeByCompany(companyCode);
        String code = "SQ" + CodeGen.getCode(maxCode != null ? maxCode : CodeGen.BEGIN_NUM);

        purchaseRequest.insertInit(userCompany);
        purchaseRequest.setId(id);
        purchaseRequest.setCompanyCode(companyCode);
        purchaseRequest.setCode(code);
        purchaseRequest.setStatus("0");
        purchaseRequest.setOrderStatus("0");
        purchaseRequest.setDelFlag("0");

        List<PurchaseRequestDetail> purchaseRequestDetailList = purchaseRequest.getDetailList();
        for(PurchaseRequestDetail detail : purchaseRequestDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setRequestId(id);
        }

        boolean result = save(purchaseRequest);
        if(result){
            requestDetailService.saveBatch(purchaseRequestDetailList);
            return ApiResult.success("新增成功！", purchaseRequest);
        }

        return ApiResult.error("新增失败！", 500);
    }

    @Override
    public ApiResult updatePurchaseRequest(String companyCode, UserCompany userCompany, PurchaseRequest purchaseRequest) {
        IdGen idGen = new IdGen();
        //删除旧数据，返回旧数据所占用的数量
        QueryWrapper<PurchaseRequestDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("request_id", purchaseRequest.getId());
        requestDetailService.physicalDeleteQWrap(detailQw);

        purchaseRequest.updateInit(userCompany);
        List<PurchaseRequestDetail> purchaseRequestDetailList = purchaseRequest.getDetailList();
        for(PurchaseRequestDetail detail : purchaseRequestDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setRequestId(purchaseRequest.getId());
        }

        boolean result = updateById(purchaseRequest);
        if(result){
            requestDetailService.saveBatch(purchaseRequestDetailList);
            return ApiResult.success("修改成功！", purchaseRequest);
        }
        return ApiResult.error("修改失败！", 500);
    }

    @Override
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<PurchaseRequest> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<PurchaseRequest> purchaseRequestList = this.list(qw);

        for (PurchaseRequest item : purchaseRequestList) {
            if (StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")) {
                return ApiResult.error("请选择单据状态为正常或者审核状态为待审核", 500);
            }
            item.setOrderStatus("1");
        }

        boolean result = this.updateBatchById(purchaseRequestList);
        if (result) {
            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

    @Override
    public void examinePass(UserCompany userCompany, AnswerDto dto) {
        IdGen idGen = new IdGen();

        PurchaseRequest purchaseRequest = getById(dto.getBusinessKey());
        purchaseRequest.setReviewer(userCompany.getUserId());
        purchaseRequest.setReviewDate(new Date());
        purchaseRequest.setStatus("2");

        QueryWrapper<PurchaseRequestDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("request_id", purchaseRequest.getId());
        List<PurchaseRequestDetail> detailList = requestDetailService.list(detailQw);

        //审核通过， 生成采购需求单
        List<PurchaseDemand> purchaseDemandList = new ArrayList<>();
        for(PurchaseRequestDetail detail : detailList){
            PurchaseDemand demand = new PurchaseDemand(purchaseRequest, detail);
            demand.insertInit(userCompany);
            demand.setId(idGen.nextIdStr());
            demand.setCompanyCode(userCompany.getCompanyCode());
            demand.setPurchasedNum(BigDecimal.ZERO);
            demand.setReadyNum(BigDecimal.ZERO);
            demand.setOrderStatus("0");
            demand.setStatus("0");
            demand.setDelFlag("0");
            purchaseDemandList.add(demand);
        }
        purchaseDemandService.saveBatch(purchaseDemandList);

        updateById(purchaseRequest);
    }

    @Override
    public void examineNoPass(UserCompany userCompany, AnswerDto dto) {
        PurchaseRequest purchaseRequest = getById(dto.getBusinessKey());
        purchaseRequest.setReviewer(userCompany.getUserId());
        purchaseRequest.setReviewDate(new Date());
        purchaseRequest.setRejectReason(dto.getConfirmSay());
        purchaseRequest.setStatus("-1");
        updateById(purchaseRequest);
    }

    @Override
    public void cancelExamine(UserCompany userCompany, AnswerDto dto) {
        PurchaseRequest purchaseRequest = getById(dto.getBusinessKey());
        purchaseRequest.setReviewer(userCompany.getUserId());
        purchaseRequest.setReviewDate(new Date());
        purchaseRequest.setStatus("0");
        updateById(purchaseRequest);
    }
}
