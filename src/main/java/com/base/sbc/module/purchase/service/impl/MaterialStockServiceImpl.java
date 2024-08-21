/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.*;
import com.base.sbc.module.purchase.mapper.MaterialStockMapper;
import com.base.sbc.module.purchase.service.MaterialStockLogService;
import com.base.sbc.module.purchase.service.MaterialStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：物料库存 service类
 * @address com.base.sbc.module.purchase.service.MaterialStockService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-13 15:44:13
 * @version 1.0  
 */
@Service
public class MaterialStockServiceImpl extends BaseServiceImpl<MaterialStockMapper, MaterialStock> implements MaterialStockService {
    @Autowired
    private MaterialStockService materialStockService;

    @Lazy
    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;

    @Autowired
    private MaterialStockLogService materialStockLogService;

    /**
     * 入库单 操作 物料库存
     *
     * @param order  入库单
     * @param orderDetailList  入库单明细
     * @param operation  操作 0 增加 1 减少
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void warehousingMaterialStock(WarehousingOrder order, List<WarehousingOrderDetail> orderDetailList, String operation) {
        IdGen idGen = new IdGen();

        List<String> materialCodeList = orderDetailList.stream().map(WarehousingOrderDetail::getMaterialCode).collect(Collectors.toList());

        QueryWrapper<MaterialStock> materialQw = new QueryWrapper<>();
        materialQw.in("material_code", materialCodeList);
        materialQw.eq("warehouse_id", order.getWarehouseId());
        List<MaterialStock> materialStockList = materialStockService.list(materialQw);
        Map<String, MaterialStock> materialStockMap = materialStockList.stream().collect(Collectors.toMap(materialStock -> materialStock.getMaterialSku() + materialStock.getPositionCode(), item -> item));

        QueryWrapper<BasicsdatumMaterial> basicQw = new QueryWrapper<>();
        basicQw.in("material_code", materialCodeList);
        List<BasicsdatumMaterial> basicsdatumMaterialList = basicsdatumMaterialService.list(basicQw);
        Map<String, BasicsdatumMaterial> materialMap = basicsdatumMaterialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, item -> item, (k, v) -> k));


        List<MaterialStock> addList = new ArrayList<>();
        List<MaterialStock> updateList = new ArrayList<>();
        List<MaterialStockLog> materialStockLogList = new ArrayList<>();
        for(WarehousingOrderDetail orderDetail : orderDetailList){
            BigDecimal beforeValue =  BigDecimal.valueOf(0.0);
            BigDecimal afterValue =  BigDecimal.valueOf(0.0);

            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialCode() + orderDetail.getMaterialColorCode() + orderDetail.getMaterialSpecificationsCode() + orderDetail.getPositionCode());
            BasicsdatumMaterial material = materialMap.get(orderDetail.getMaterialCode());
            if(materialStock == null){
                //物料库存中不存在此物料，初始化物料
                materialStock = new MaterialStock(order, orderDetail, material);
                materialStock.setId(idGen.nextIdStr());
                materialStock.setCompanyCode(order.getCompanyCode());

                if(StringUtils.equals(operation, "0")) {
                    materialStock.setStockQuantity(orderDetail.getWarehouseNum());
                    materialStock.setAvailableQuantity(orderDetail.getWarehouseNum());
                    afterValue = orderDetail.getWarehouseNum();
                }else{
                    materialStock.setStockQuantity(orderDetail.getWarehouseNum().negate());
                    materialStock.setAvailableQuantity(orderDetail.getWarehouseNum().negate());
                    afterValue = orderDetail.getWarehouseNum().negate();
                }
                addList.add(materialStock);
            }else{
                beforeValue = materialStock.getStockQuantity();
                if(StringUtils.equals(operation, "0")) {
                    BigDecimal stockQuantity = BigDecimalUtil.add(materialStock.getStockQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setStockQuantity(stockQuantity);
                    BigDecimal availableQuantity = BigDecimalUtil.add(materialStock.getAvailableQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setAvailableQuantity(availableQuantity);
                    afterValue = stockQuantity;
                }else{
                    BigDecimal result = BigDecimalUtil.sub(materialStock.getStockQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setStockQuantity(result);
                    BigDecimal availableQuantity = BigDecimalUtil.sub(materialStock.getAvailableQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setAvailableQuantity(availableQuantity);
                    afterValue = result;
                }
                updateList.add(materialStock);
            }

            MaterialStockLog materialStockLog = new MaterialStockLog(order, material, beforeValue, orderDetail.getWarehouseNum(), afterValue);
            materialStockLog.setId(idGen.nextIdStr());
            materialStockLog.setCompanyCode(order.getCompanyCode());
            materialStockLog.setMaterialWarehouseId(materialStock.getId());
            materialStockLog.setType("0");
            materialStockLogList.add(materialStockLog);
        }

        if(CollectionUtil.isNotEmpty(addList)){
            materialStockService.saveBatch(addList);
        }

        if(CollectionUtil.isNotEmpty(updateList)){
            materialStockService.updateBatchById(updateList);
        }

        if(CollectionUtil.isNotEmpty(materialStockLogList)){
            materialStockLogService.saveBatch(materialStockLogList);
        }
    }

    /**
     * 出库单 操作 物料库存 锁定库存，可用库存
     *
     * @param order  出库单
     * @param orderDetailList  出库单明细
     * @param operation  操作 0 增加 1 减少
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void outBoundOrderMaterialStockLock(OutboundOrder order, List<OutboundOrderDetail> orderDetailList, String operation) {
        IdGen idGen = new IdGen();

        List<String> materialCodeList = orderDetailList.stream().map(OutboundOrderDetail::getMaterialCode).collect(Collectors.toList());

        QueryWrapper<MaterialStock> materialQw = new QueryWrapper<>();
        materialQw.in("material_code", materialCodeList);
        materialQw.eq("warehouse_id", order.getWarehouseId());
        List<MaterialStock> materialStockList = materialStockService.list(materialQw);
        Map<String, MaterialStock> materialStockMap = materialStockList.stream().collect(Collectors.toMap(materialStock -> materialStock.getMaterialSku() + materialStock.getPositionCode(), item -> item));

        List<MaterialStock> updateList = new ArrayList<>();
        for(OutboundOrderDetail orderDetail : orderDetailList){

//            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialSku());
            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialCode() + orderDetail.getColorCode() + orderDetail.getSpecificationsCode() + orderDetail.getPositionCode());
            if(materialStock != null){
                if(StringUtils.equals(operation, "0")) {
                    //锁定库存增加，可用库存减少
                    materialStock.setLockQuantity(BigDecimalUtil.add(materialStock.getLockQuantity(), orderDetail.getOutNum()));
                    materialStock.setAvailableQuantity(BigDecimalUtil.sub(materialStock.getAvailableQuantity(), orderDetail.getOutNum()));
                }else{
                    //锁定库存减少，可用库存增加
                    materialStock.setLockQuantity(BigDecimalUtil.sub(materialStock.getLockQuantity(), orderDetail.getOutNum()));
                    materialStock.setAvailableQuantity(BigDecimalUtil.add(materialStock.getAvailableQuantity(), orderDetail.getOutNum()));
                }
                updateList.add(materialStock);
            }
        }

        if(CollectionUtil.isNotEmpty(updateList)){
            materialStockService.updateBatchById(updateList);
        }

    }


    /**
     * 出库单 操作 物料库存
     *
     * @param order  出库单
     * @param orderDetailList  出库单明细
     * @param operation  操作 0 增加 1 减少
     * */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void outBoundOrderMaterialStock(OutboundOrder order, List<OutboundOrderDetail> orderDetailList, String operation) {
        IdGen idGen = new IdGen();

        List<String> materialCodeList = orderDetailList.stream().map(OutboundOrderDetail::getMaterialCode).collect(Collectors.toList());

        QueryWrapper<MaterialStock> materialQw = new QueryWrapper<>();
        materialQw.in("material_code", materialCodeList);
        materialQw.eq("warehouse_id", order.getWarehouseId());
        List<MaterialStock> materialStockList = materialStockService.list(materialQw);
        Map<String, MaterialStock> materialStockMap = materialStockList.stream().collect(Collectors.toMap(materialStock -> materialStock.getMaterialSku() + materialStock.getPositionCode(), item -> item));

        QueryWrapper<BasicsdatumMaterial> basicQw = new QueryWrapper<>();
        basicQw.in("material_code", materialCodeList);
        List<BasicsdatumMaterial> basicsdatumMaterialList = basicsdatumMaterialService.list(basicQw);
        Map<String, BasicsdatumMaterial> materialMap = basicsdatumMaterialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, item -> item, (k, v) -> k));


        List<MaterialStock> updateList = new ArrayList<>();
        List<MaterialStockLog> materialStockLogList = new ArrayList<>();
        for(OutboundOrderDetail orderDetail : orderDetailList){
            BigDecimal beforeValue = BigDecimal.valueOf(0.0);
            BigDecimal afterValue = BigDecimal.valueOf(0.0);

//            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialSku());
            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialCode() + orderDetail.getColorCode() + orderDetail.getSpecificationsCode() + orderDetail.getPositionCode());
            BasicsdatumMaterial material = materialMap.get(orderDetail.getMaterialCode());
            if(materialStock != null){
                beforeValue = materialStock.getStockQuantity();
                if(StringUtils.equals(operation, "0")) {
                    BigDecimal result = BigDecimalUtil.add(materialStock.getStockQuantity(), orderDetail.getOutNum());
                    materialStock.setStockQuantity(result);
                    materialStock.setLockQuantity(BigDecimalUtil.add(materialStock.getLockQuantity(), orderDetail.getOutNum()));
                    afterValue = result;
                }else{
                    // 释放锁定库存，库存总数调整为真实库存数量
                    BigDecimal result = BigDecimalUtil.sub(materialStock.getStockQuantity(), orderDetail.getOutNum());
                    materialStock.setStockQuantity(result);
                    materialStock.setLockQuantity(BigDecimalUtil.sub(materialStock.getLockQuantity(), orderDetail.getOutNum()));
                    afterValue = result;
                }
                updateList.add(materialStock);
            }

            MaterialStockLog materialStockLog = new MaterialStockLog(order, material, beforeValue, orderDetail.getOutNum(), afterValue);
            materialStockLog.setId(idGen.nextIdStr());
            materialStockLog.setCompanyCode(order.getCompanyCode());
            if (materialStock != null) {
                materialStockLog.setMaterialWarehouseId(materialStock.getId());
            }
            materialStockLog.setType("1");
            materialStockLogList.add(materialStockLog);
        }

        if(CollectionUtil.isNotEmpty(updateList)){
            materialStockService.updateBatchById(updateList);
        }

        if(CollectionUtil.isNotEmpty(materialStockLogList)){
            materialStockLogService.saveBatch(materialStockLogList);
        }
    }
}
