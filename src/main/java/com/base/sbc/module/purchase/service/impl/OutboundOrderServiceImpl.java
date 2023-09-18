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
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.OutboundOrderDetail;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;
import com.base.sbc.module.purchase.mapper.OutboundOrderMapper;
import com.base.sbc.module.purchase.entity.OutboundOrder;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.base.sbc.module.purchase.service.OutboundOrderDetailService;
import com.base.sbc.module.purchase.service.OutboundOrderService;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：出库单 service类
 * @address com.base.sbc.module.purchase.service.OutboundOrderService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-18 15:21:46
 * @version 1.0  
 */
@Service
public class OutboundOrderServiceImpl extends BaseServiceImpl<OutboundOrderMapper, OutboundOrder> implements OutboundOrderService {

    @Autowired
    private OutboundOrderMapper outboundOrderMapper;

    @Autowired
    private OutboundOrderDetailService outboundOrderDetailService;

    @Autowired
    private PurchaseDemandService purchaseDemandService;

    @Autowired
    private MaterialStockService materialStockService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<OutboundOrder> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<OutboundOrder> outboundOrderList = this.list(qw);

        List<String> idList = new ArrayList<>();
        for (OutboundOrder item : outboundOrderList) {
            if (StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")) {
                return ApiResult.error("请选择单据状态为正常或者审核状态为待审核", 500);
            }
            item.setOrderStatus("1");

            idList.add(item.getId());
        }

        boolean result = this.updateBatchById(outboundOrderList);
        if (result) {
            QueryWrapper<OutboundOrderDetail> detailQw = new QueryWrapper<>();
            detailQw.in("outbound_id", idList);
            List<OutboundOrderDetail> detailList = outboundOrderDetailService.list(detailQw);
            purchaseDemandService.manipulateReadyNum(detailList, "1");
            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult addOutbound(UserCompany userCompany, String companyCode, OutboundOrder outboundOrder) {
        IdGen idGen = new IdGen();

        String maxCode = outboundOrderMapper.selectMaxCodeByCompany(companyCode);
        String code = "CK" + CodeGen.getCode(maxCode != null ? maxCode : CodeGen.BEGIN_NUM);

        String id = idGen.nextIdStr();
        outboundOrder.insertInit(userCompany);
        outboundOrder.setId(id);
        outboundOrder.setCode(code);
        outboundOrder.setCompanyCode(companyCode);
        outboundOrder.setStatus("0");
        outboundOrder.setOrderStatus("0");
        outboundOrder.setDelFlag("0");

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalNum = BigDecimal.ZERO;
        List<OutboundOrderDetail> orderDetailList = outboundOrder.getOrderDetailList();
        for(OutboundOrderDetail detail : orderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setCompanyCode(companyCode);
            detail.setOutboundId(id);

            totalAmount = BigDecimalUtil.add(totalAmount, detail.getOutNum().multiply(detail.getStockPrice()));
            totalNum = BigDecimalUtil.add(totalNum, detail.getOutNum());
        }

        outboundOrder.setOutboundAmount(totalAmount);
        outboundOrder.setOutboundNum(totalNum);
        boolean result = save(outboundOrder);
        if(result){
            outboundOrderDetailService.saveBatch(orderDetailList);
            purchaseDemandService.manipulateReadyNum(orderDetailList, "0");
            return ApiResult.success("新增成功！", outboundOrder);
        }
        return ApiResult.error("新增失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult updateOutbound(UserCompany userCompany, String companyCode, OutboundOrder outboundOrder) {
        IdGen idGen = new IdGen();
        //删除旧数据，返回旧数据所占用的数量
        QueryWrapper<OutboundOrderDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("outbound_id", outboundOrder.getId());
        List<OutboundOrderDetail> detailList = outboundOrderDetailService.list(detailQw);
        purchaseDemandService.manipulateReadyNum(detailList, "1");
        outboundOrderDetailService.physicalDeleteQWrap(detailQw);

        outboundOrder.updateInit(userCompany);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalNum = BigDecimal.ZERO;
        List<OutboundOrderDetail> orderDetailList = outboundOrder.getOrderDetailList();
        for(OutboundOrderDetail detail : orderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setCompanyCode(companyCode);
            detail.setOutboundId(outboundOrder.getId());

            totalAmount = BigDecimalUtil.add(totalAmount, detail.getOutNum().multiply(detail.getStockPrice()));
            totalNum = BigDecimalUtil.add(totalNum, detail.getOutNum());
        }

        outboundOrder.setOutboundAmount(totalAmount);
        outboundOrder.setOutboundNum(totalNum);
        boolean result = updateById(outboundOrder);
        if(result){
            outboundOrderDetailService.saveBatch(orderDetailList);
            purchaseDemandService.manipulateReadyNum(orderDetailList, "0");
            return ApiResult.success("修改成功！", outboundOrder);
        }
        return ApiResult.error("修改失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void examinePass(UserCompany userCompany, AnswerDto dto) {
        OutboundOrder outboundOrder = getById(dto.getBusinessKey());
        outboundOrder.setReviewerId(userCompany.getUserId());
        outboundOrder.setReviewerName(userCompany.getAliasUserName());
        outboundOrder.setReviewDate(new Date());
        outboundOrder.setStatus("2");

        // 物料库存 数据
        QueryWrapper<OutboundOrderDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("outbound_id", outboundOrder.getId());
        List<OutboundOrderDetail> detailList = outboundOrderDetailService.list(detailQw);
        materialStockService.outBoundOrderMaterialStock(outboundOrder, detailList, "1");

        updateById(outboundOrder);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void examineNoPass(UserCompany userCompany, AnswerDto dto) {
        OutboundOrder outboundOrder = getById(dto.getBusinessKey());
        outboundOrder.setReviewerId(userCompany.getUserId());
        outboundOrder.setReviewerName(userCompany.getAliasUserName());
        outboundOrder.setReviewDate(new Date());
        outboundOrder.setStatus("-1");
        outboundOrder.setRejectReason(dto.getConfirmSay());
        updateById(outboundOrder);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void cancelExamine(UserCompany userCompany, AnswerDto dto) {
        OutboundOrder outboundOrder = getById(dto.getBusinessKey());
        outboundOrder.setReviewerId(userCompany.getUserId());
        outboundOrder.setReviewerName(userCompany.getAliasUserName());
        outboundOrder.setReviewDate(new Date());
        outboundOrder.setStatus("0");
        updateById(outboundOrder);
    }
}
