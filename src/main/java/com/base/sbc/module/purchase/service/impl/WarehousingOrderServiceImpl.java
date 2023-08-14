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
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;
import com.base.sbc.module.purchase.mapper.WarehousingOrderMapper;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import com.base.sbc.module.purchase.service.WarehousingOrderDetailService;
import com.base.sbc.module.purchase.service.WarehousingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：入库单 service类
 * @address com.base.sbc.module.purchase.service.WarehousingOrderService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-9 16:21:43
 * @version 1.0  
 */
@Service
public class WarehousingOrderServiceImpl extends BaseServiceImpl<WarehousingOrderMapper, WarehousingOrder> implements WarehousingOrderService {

    @Autowired
    private WarehousingOrderDetailService warehousingOrderDetailService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private WarehousingOrderMapper warehousingOrderMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<WarehousingOrder> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<WarehousingOrder> warehousingOrderList = this.list(qw);

        for (WarehousingOrder item : warehousingOrderList) {
            if (StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")) {
                return ApiResult.error("请选择单据状态为正常或者审核状态为待审核", 500);
            }
            item.setOrderStatus("1");
        }

        boolean result = this.updateBatchById(warehousingOrderList);
        if (result) {
            QueryWrapper<WarehousingOrderDetail> detailQw = new QueryWrapper<>();
            detailQw.in("warehouse_order_id", StringUtils.convertList(ids));
            List<WarehousingOrderDetail> warehousingOrderDetailList = warehousingOrderDetailService.list(detailQw);
            purchaseOrderService.manipulateWarehouseNum(warehousingOrderDetailList, "1", true);
            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult addWarehousing(UserCompany userCompany, String companyCode, WarehousingOrder warehousingOrder) {
        IdGen idGen = new IdGen();

        String maxCode = warehousingOrderMapper.selectMaxCodeByCompany(companyCode);
        String code = "RK" + CodeGen.getCode(maxCode != null ? maxCode : CodeGen.BEGIN_NUM);

        String id = idGen.nextIdStr();
        warehousingOrder.insertInit(userCompany);
        warehousingOrder.setId(id);
        warehousingOrder.setCode(code);
        warehousingOrder.setCompanyCode(companyCode);
        warehousingOrder.setStatus("0");
        warehousingOrder.setOrderStatus("0");
        warehousingOrder.setDelFlag("0");

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalNum = BigDecimal.ZERO;
        List<WarehousingOrderDetail> orderDetailList = warehousingOrder.getOrderDetailList();
        for(WarehousingOrderDetail detail : orderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setCompanyCode(companyCode);
            detail.setWarehouseOrderId(id);

            totalAmount = BigDecimalUtil.add(totalAmount, detail.getActualAmount());
            totalNum = BigDecimalUtil.add(totalNum, detail.getReceivedQuantity());
        }

        warehousingOrder.setMoney(totalAmount);
        warehousingOrder.setDeliveryQuantity(totalNum);

        boolean result = save(warehousingOrder);
        if(result){
            warehousingOrderDetailService.saveBatch(orderDetailList);
            //操作采购单的入库数量
            purchaseOrderService.manipulateWarehouseNum(orderDetailList, "0", true);
            return ApiResult.success("新增成功！", warehousingOrder);
        }

        return ApiResult.error("新增失败！", 500);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult updateWarehousing(UserCompany userCompany, String companyCode, WarehousingOrder warehousingOrder) {
        IdGen idGen = new IdGen();

        //删除旧数据，返回旧数据所占用的数量
        QueryWrapper<WarehousingOrderDetail> detailQw = new QueryWrapper<>();
        detailQw.eq("warehouse_order_id", warehousingOrder.getId());
        List<WarehousingOrderDetail> oldList = warehousingOrderDetailService.list(detailQw);
        purchaseOrderService.manipulateWarehouseNum(oldList, "1", true);
        warehousingOrderDetailService.remove(detailQw);

        warehousingOrder.updateInit(userCompany);
        List<WarehousingOrderDetail> warehousingOrderDetailList = warehousingOrder.getOrderDetailList();
        for(WarehousingOrderDetail detail : warehousingOrderDetailList){
            detail.setId(idGen.nextIdStr());
            detail.setWarehouseOrderId(warehousingOrder.getId());
        }

        boolean result = updateById(warehousingOrder);
        if(result){
            warehousingOrderDetailService.saveBatch(warehousingOrderDetailList);
            //操作采购需求单的已采购数量
            purchaseOrderService.manipulateWarehouseNum(warehousingOrderDetailList, "0", true);
            return ApiResult.success("修改成功！", warehousingOrder);
        }
        return ApiResult.error("修改失败！", 500);
    }
}
