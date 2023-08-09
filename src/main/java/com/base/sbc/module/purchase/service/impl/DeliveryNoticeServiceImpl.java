/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.mapper.DeliveryNoticeMapper;
import com.base.sbc.module.purchase.entity.DeliveryNotice;
import com.base.sbc.module.purchase.service.DeliveryNoticeService;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：送货通知单 service类
 * @address com.base.sbc.module.purchase.service.DeliveryNoticeService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-8 16:38:37
 * @version 1.0  
 */
@Service
public class DeliveryNoticeServiceImpl extends BaseServiceImpl<DeliveryNoticeMapper, DeliveryNotice> implements DeliveryNoticeService {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Override
    public ApiResult generateNotice(UserCompany userCompany, String companyCode, String ids) {
        QueryWrapper<PurchaseOrderDetail> detailQw = new QueryWrapper<>();
        detailQw.in("id", StringUtils.convertList(ids));
        List<PurchaseOrderDetail> orderDetailList = purchaseOrderDetailService.list(detailQw);

        PurchaseOrder purchaseOrder = purchaseOrderService.getById(orderDetailList.get(0).getPurchaseOrderId());

        IdGen idGen = new IdGen();
        List<DeliveryNotice> deliveryNoticeList = new ArrayList<>();
        for(PurchaseOrderDetail detail : orderDetailList){
            DeliveryNotice deliveryNotice = new DeliveryNotice();
            deliveryNotice.insertInit(userCompany);
            deliveryNotice.setId(idGen.nextIdStr());
            deliveryNotice.setCompanyCode(companyCode);
            deliveryNotice.setStatus("0");
            deliveryNotice.setDelFlag("0");

            deliveryNotice.setPurchaseCode(purchaseOrder.getCode());
            deliveryNotice.setSupplierId(purchaseOrder.getSupplierId());
            deliveryNotice.setSupplierName(purchaseOrder.getSupplierName());
            deliveryNotice.setPurchaserId(purchaseOrder.getPurchaserId());
            deliveryNotice.setPurchaserName(purchaseOrder.getPurchaserName());
            deliveryNotice.setPurchaseDate(purchaseOrder.getPurchaseDate());

            deliveryNotice.setDeliveryDate(detail.getDeliveryDate());
            deliveryNotice.setDesignStyleCode(detail.getDesignStyleCode());
            deliveryNotice.setPlateBillCode(detail.getPlateBillCode());
            deliveryNotice.setMaterialCode(detail.getMaterialCode());
            deliveryNotice.setMaterialName(detail.getMaterialName());
            deliveryNotice.setMaterialSpecifications(detail.getMaterialSpecifications());
            deliveryNotice.setMaterialColor(detail.getMaterialColor());
            deliveryNotice.setExpectedPurchaseNum(detail.getPurchaseNum());
            deliveryNotice.setPrice(detail.getPrice());
            deliveryNotice.setPurchaseUnit(detail.getPurchaseUnit());
            deliveryNotice.setDeliveryQuantity(detail.getPurchaseNum());
            deliveryNotice.setReceivedQuantity(detail.getPurchaseNum());
            deliveryNotice.setRemarks(detail.getRemarks());
            deliveryNoticeList.add(deliveryNotice);
        }

        if(CollectionUtil.isNotEmpty(deliveryNoticeList)){
            boolean result = saveBatch(deliveryNoticeList);
            if(result){
                return ApiResult.success("新增成功！");
            }
        }
        return ApiResult.error("找不到数据！", 500);
    }
}
