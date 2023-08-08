/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.mapper.PurchaseOrderMapper;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public ApiResult addPurchaseOrder(UserCompany userCompany, String companyCode, PurchaseOrder purchaseOrder) {
        IdGen idGen = new IdGen();

        String id = idGen.nextIdStr();
        String maxCode = purchaseOrderMapper.selectMaxCodeByCompany(companyCode);
        String code = "SH" + CodeGen.getCode(maxCode != null ? maxCode : CodeGen.BEGIN_NUM);

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

	
}
