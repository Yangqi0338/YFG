/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;
import com.base.sbc.module.purchase.mapper.PurchaseOrderMapper;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：采购-采购单 service类
 * @address com.base.sbc.module.purchase.service.PurchaseOrderService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 9:43:16
 * @version 1.0  
 */
@Service
public class PurchaseOrderServiceImpl extends BaseServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private PurchaseDemandService purchaseDemandService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult addPurchaseOrder(UserCompany userCompany, String companyCode, PurchaseOrder purchaseOrder) {
        IdGen idGen = new IdGen();

        String id = idGen.nextIdStr();
        String maxCode = purchaseOrderMapper.selectMaxCodeByCompany(companyCode);
        String code = "SO" + CodeGen.getCode(maxCode != null ? maxCode : CodeGen.BEGIN_NUM);

        purchaseOrder.insertInit(userCompany);
        purchaseOrder.setId(id);
        purchaseOrder.setCompanyCode(companyCode);
        purchaseOrder.setCode(code);
        purchaseOrder.setStatus("0");
        purchaseOrder.setOrderStatus("0");
        purchaseOrder.setWarehouseStatus("0");
        purchaseOrder.setDelFlag("0");

        List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrder.getPurchaseOrderDetailList();
        for(PurchaseOrderDetail detail : purchaseOrderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setPurchaseOrderId(id);
            detail.setWarehouseNum(BigDecimal.ZERO);
        }

        boolean result = save(purchaseOrder);
        if(result){
            purchaseOrderDetailService.saveBatch(purchaseOrderDetailList);
            //操作采购需求单的已采购数量
            purchaseDemandService.manipulatePlanNum(purchaseOrderDetailList, "0");
            return ApiResult.success("新增成功！", id);
        }
        return ApiResult.error("新增失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult updatePurchaseOrder(UserCompany userCompany, String companyCode, PurchaseOrder purchaseOrder) {
        IdGen idGen = new IdGen();

        //删除旧数据，返回旧数据所占用的数量
        QueryWrapper<PurchaseOrderDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("purchase_order_id", purchaseOrder.getId());
        List<PurchaseOrderDetail> oldList = purchaseOrderDetailService.list(detailQw);
        purchaseDemandService.manipulatePlanNum(oldList, "1");
        purchaseOrderDetailService.remove(detailQw);

        purchaseOrder.updateInit(userCompany);
        List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrder.getPurchaseOrderDetailList();
        for(PurchaseOrderDetail detail : purchaseOrderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setPurchaseOrderId(purchaseOrder.getId());
        }

        boolean result = updateById(purchaseOrder);
        if(result){
            purchaseOrderDetailService.saveBatch(purchaseOrderDetailList);
            //操作采购需求单的已采购数量
            purchaseDemandService.manipulatePlanNum(purchaseOrderDetailList, "0");
            return ApiResult.success("修改成功！", purchaseOrder);
        }
        return ApiResult.error("修改失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<PurchaseOrder> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<PurchaseOrder> purchaseOrderList = this.list(qw);

        for (PurchaseOrder item : purchaseOrderList) {
            if (StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")) {
                return ApiResult.error("请选择单据状态为正常或者审核状态为待审核", 500);
            }
            item.setOrderStatus("1");
        }

        boolean result = this.updateBatchById(purchaseOrderList);
        if (result) {
            QueryWrapper<PurchaseOrderDetail> detailQw = new QueryWrapper<>();
            detailQw.in("purchase_order_id", StringUtils.convertList(ids));
            List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderDetailService.list(detailQw);
            //操作采购需求单的已采购数量
            purchaseDemandService.manipulatePlanNum(purchaseOrderDetailList, "1");

            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

    /**
     * 入库单 操作 采购单的收货数量
     * @param warehousingOrderDetailList 入库单明细数据
     * @param type 操作类型 0占用 1返还
     * @param ifMain 是否变更采购单的入库状态
     * */
    @Override
    public void manipulateWarehouseNum(List<WarehousingOrderDetail> warehousingOrderDetailList, String type, Boolean ifMain) {
        List<String> idList = new ArrayList<>();
        for(WarehousingOrderDetail detail : warehousingOrderDetailList){
            if(StringUtils.isNotBlank(detail.getPurchaseOrderDetailId())){
                idList.add(detail.getPurchaseOrderDetailId());
            }
        }

        if(CollectionUtil.isEmpty(idList)){
            return;
        }

        QueryWrapper<PurchaseOrderDetail> qw = new QueryWrapper<>();
        qw.in("id", idList);
        List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderDetailService.list(qw);
        if(CollectionUtil.isEmpty(purchaseOrderDetailList)){
            return;
        }

        Map<String, PurchaseOrderDetail> purchaseOrderDetailMap = purchaseOrderDetailList.stream().collect(Collectors.toMap(PurchaseOrderDetail::getId, item -> item));
        Set<String> purchaseIdSet = new HashSet<>();
        for(WarehousingOrderDetail details : warehousingOrderDetailList){
            PurchaseOrderDetail item = purchaseOrderDetailMap.get(details.getPurchaseOrderDetailId());
            if(item != null){
                if(StringUtils.equals(type, "0")){
                    item.setWarehouseNum(BigDecimalUtil.add(item.getWarehouseNum(), details.getReceivedQuantity()));
                }else{
                    item.setWarehouseNum(BigDecimalUtil.sub(item.getWarehouseNum(), details.getReceivedQuantity()));
                }
                purchaseIdSet.add(item.getPurchaseOrderId());
            }
            purchaseOrderDetailMap.put(details.getPurchaseOrderDetailId(), item);
        }

        List<PurchaseOrderDetail> updateList = new ArrayList<>(purchaseOrderDetailMap.values());
        purchaseOrderDetailService.updateBatchById(updateList);
        if(ifMain && CollectionUtil.isNotEmpty(purchaseIdSet)){
            verifyWarehousingStatus(new ArrayList<>(purchaseIdSet));
        }
    }

    @Override
    public void verifyWarehousingStatus(List<String> idList) {
        QueryWrapper<PurchaseOrder> qw = new QueryWrapper<>();
        qw.in("id", idList);
        List<PurchaseOrder> orderList = list(qw);
        if(CollectionUtil.isNotEmpty(orderList)){
            QueryWrapper<PurchaseOrderDetail> detailQw;
            for(PurchaseOrder order : orderList){
                detailQw = new QueryWrapper<>();
                detailQw.eq("purchase_order_id", order.getId());
                List<PurchaseOrderDetail> detailList = purchaseOrderDetailService.list(detailQw);

                String completeStatus = "0";
                Boolean ifComplete = true;
                for(PurchaseOrderDetail detail : detailList){
                    if(BigDecimalUtil.biggerThenZero(detail.getWarehouseNum())){
                        //入库数量大于零,入库中
                        completeStatus = "1";
                    }
                    if(detail.getWarehouseNum().compareTo(detail.getPurchaseNum()) == -1){
                        //入库数量小于采购数量
                        ifComplete = false;
                    }
                }

                if(ifComplete){
                    //所有明细入库数量都大于采购数量，入库完成
                    completeStatus = "2";
                }

                order.setWarehouseStatus(completeStatus);
            }

            updateBatchById(orderList);
        }

    }

    /**
     * 审核通过
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void examinePass(UserCompany userCompany, AnswerDto dto) {
        PurchaseOrder purchaseOrder = getById(dto.getBusinessKey());
        purchaseOrder.setReviewer(userCompany.getUserId());
        purchaseOrder.setReviewDate(new Date());
        purchaseOrder.setStatus("1");
        updateById(purchaseOrder);
    }

    /**
     * 审核驳回
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void examineNoPass(UserCompany userCompany, AnswerDto dto) {
        PurchaseOrder purchaseOrder = getById(dto.getBusinessKey());
        purchaseOrder.setReviewer(userCompany.getUserId());
        purchaseOrder.setReviewDate(new Date());
        purchaseOrder.setRejectReason(dto.getConfirmSay());
        purchaseOrder.setStatus("2");
        updateById(purchaseOrder);
    }

    /**
     * 取消审核
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void cancelExamine(UserCompany userCompany, AnswerDto dto) {
        PurchaseOrder purchaseOrder = getById(dto.getBusinessKey());
        purchaseOrder.setReviewer(userCompany.getUserId());
        purchaseOrder.setReviewDate(new Date());
        purchaseOrder.setStatus("0");
        updateById(purchaseOrder);
    }


}
