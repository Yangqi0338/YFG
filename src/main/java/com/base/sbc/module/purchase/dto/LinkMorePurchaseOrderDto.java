/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：采购-采购单 实体类
 * @address com.base.sbc.module.purchase.entity.PurchaseOrder
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-8 10:54:12
 * @version 1.0
 */
@Data
@ApiModel("采购-领猫采购单dto PurchaseOrderDto")
public class LinkMorePurchaseOrderDto{

    /**
     * 初始化
     * @param purchaseOrder
     */
    public void init(PurchaseOrder purchaseOrder){
        this.spCode = purchaseOrder.getSupplierCode();
        this.orderDate = purchaseOrder.getPurchaseDate();
        this.orderType = "原辅料采购（打样专用）";
        this.whCode = purchaseOrder.getWarehouseName();//没有仓库编码
        this.followMan = purchaseOrder.getPurchaserName();
        this.remarks = purchaseOrder.getRemarks();
        this.statuz = 1;
        this.apiCode = purchaseOrder.getCode();
        this.checkMan = purchaseOrder.getReviewer();
        this.checkDate = purchaseOrder.getReviewDate();
        this.accountDate = purchaseOrder.getPurchaseDate();
        this.payType = "99";
        this.companyCode = "ds01";
        if (CollectionUtil.isNotEmpty(purchaseOrder.getPurchaseOrderDetailList())){
            List<Sub> subs = new ArrayList<>();
            for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
                Sub sub = new Sub();
                sub.init(detail);
                subs.add(sub);
            }
            this.subs = subs;
        }
    }
    /**供应商编码*/
    private String spCode;
    /**采购日期-制单日期*/
    private Date orderDate;
    /**订单类型编码(默认原辅料采购（打样专用）)*/
    private String orderType;
    /**仓库编码*/
    private String whCode;
    /**采购员-跟单员*/
    private String followMan;
    /**备注*/
    private String remarks;
    /**状态*/
    private Integer statuz;
    /**打款方式编码*/
    private String ficType;
    /**采购单号-外部单号*/
    private String apiCode;
    /**审核人，审核状态时必填*/
    private String checkMan;
    /**审核日期，审核状态时必填*/
    private Date checkDate;
    /**会计日期，不填默认当前时间*/
    private Date accountDate;
    /**结算方式编码（默认99）*/
    private String payType;
    /**审核人-操作人*/
    private String operator;
    /**主体公司编码（默认ds01）*/
    private String companyCode;
    /**品牌编码（默认空）*/
    private String brandCode;

    /**采购明细*/
    private List<Sub> subs;

    /** 采购明细 */
    @Data
    class Sub{
        /** 交付日期（物料交期） */
        private Date deliverDate;
        /** 物料SubCode（物料编码+颜色编码+规格编码） */
        private String mtSubCode;
        /** 数量 */
        private BigDecimal qty;
        /**单位（编码）*/
        private String uintCode;
        /**规格描述*/
        private String mtSpec;
        /**不含税单价*/
        private BigDecimal cleanPrice;
        /** 税率（默认13） */
        private BigDecimal taxRate;
        /**含税单价*/
        private BigDecimal price;
        /**匹数*/
        private String checkQty;
        /**备注*/
        private String remarks;

        /**
         * 初始化
         * @param detail
         */
        public void init(PurchaseOrderDetail detail) {
            this.deliverDate = detail.getDeliveryDate();
            this.mtSubCode = detail.getMaterialCode()+detail.getMaterialColorCode()+detail.getMaterialSpecificationsCode();//物料编码+颜色编码+规格编码
            this.qty = detail.getPurchaseNum();
            this.uintCode = detail.getPurchaseUnit();//编码
            this.mtSpec = detail.getMaterialSpecifications();
            this.cleanPrice = detail.getPrice();
            this.taxRate = new BigDecimal(13);
            this.price = detail.getPrice();
            this.remarks = detail.getRemarks();
        }
    }
}
