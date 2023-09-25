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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CodeGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import com.base.sbc.module.purchase.entity.*;
import com.base.sbc.module.purchase.mapper.PurchaseDemandMapper;
import com.base.sbc.module.purchase.mapper.PurchaseOrderMapper;
import com.base.sbc.module.purchase.service.MaterialWarehouseService;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：采购-采购需求表 service类
 *
 * @author tzy
 * @version 1.0
 * @address com.base.sbc.module.purchase.service.PurchaseDemandService
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-2 14:29:52
 */
@Service
public class PurchaseDemandServiceImpl extends BaseServiceImpl<PurchaseDemandMapper, PurchaseDemand> implements PurchaseDemandService {

    @Autowired
    private PackInfoService packInfoService;

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private PackBomSizeService packBomSizeService;

    @Autowired
    private PackBomColorService packBomColorService;

    @Autowired
    private PackBomVersionService packBomVersionService;

    @Autowired
    private BasicsdatumMaterialService materialService;

    @Autowired
    private BasicsdatumSupplierService supplierService;

    @Autowired
    private MaterialWarehouseService warehouseService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private PurchaseDemandMapper purchaseDemandMapper;

    @Autowired
    private UserCompanyUtils userCompanyUtils;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult cancel(String companyCode, String ids) {
        QueryWrapper<PurchaseDemand> qw = new QueryWrapper();
        qw.eq("company_code", companyCode);
        qw.in("id", StringUtils.convertList(ids));
        List<PurchaseDemand> purchaseDemandList = this.list(qw);

        for (PurchaseDemand item : purchaseDemandList) {
            if (StringUtils.equals(item.getOrderStatus(), "1") || StringUtils.equals(item.getStatus(), "1")) {
                return ApiResult.error("请选择单据状态为正常或者审核状态为待审核", 500);
            }
            item.setOrderStatus("1");
        }

        boolean result = this.updateBatchById(purchaseDemandList);
        if (result) {
            return ApiResult.success("操作成功！", result);
        }
        return ApiResult.error("操作失败！", 500);
    }

    /**
     * 根据资料的物料清单生成采购需求单数据
     *
     * @param companyCode 企业编码
     * @param id          资料包id
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult generatePurchaseDemand(String companyCode, String id, String materialIds, String colors) {
        IdGen idGen = new IdGen();
        UserCompany userCompany = userCompanyUtils.getCompanyUser();

        List<String> packBomIdList = StringUtils.convertList(materialIds);
        List<String> colorList = StringUtils.convertList(colors);

//        QueryWrapper<PurchaseDemand> pdQw = new QueryWrapper<>();
//        pdQw.eq("company_code", companyCode);
//        StringBuffer sql = new StringBuffer("and ");
//
//        for(String packBomId : packBomIdList){
//            for(String color : colorList){
//                sql.append("(pack_bom_id = '" + packBomId +"' and product_color = '" + color + "') OR ");
//            }
//        }
//        sql.delete(sql.length() - 4 , sql.length() -1);
//        pdQw.last(sql.toString());
//        List<PurchaseDemand> oldDataList = list(pdQw);
//        if(CollectionUtil.isNotEmpty(oldDataList)){
//            return ApiResult.error("该物料已经生成过采购需求，请重新选择！", 500);
//        }

        //查询资料包-物料清单-物料版本 启用状态的数据
        QueryWrapper<PackBomVersion> versionQw = new QueryWrapper<>();
        versionQw.eq("id", id);
        List<PackBomVersion> packBomVersionList = packBomVersionService.list(versionQw);

        if (CollectionUtil.isNotEmpty(packBomVersionList)) {
            //再根据版本，查找物料清单
            PackBomVersion packBomVersion = packBomVersionList.get(0);

            QueryWrapper<PackInfo> qw = new QueryWrapper<>();
            qw.eq("company_code", companyCode);
            qw.eq("id", packBomVersion.getForeignId());
            qw.last("limit 1");
            PackInfo packInfo = packInfoService.getOne(qw);

            QueryWrapper<Style> styleQw = new QueryWrapper<>();
            styleQw.eq("company_code", companyCode);
            styleQw.eq("id", packInfo.getForeignId());
            Style style = styleService.getOne(styleQw);

            QueryWrapper<PackBom> packBomQw = new QueryWrapper<>();
            PackUtils.commonQw(packBomQw, packInfo.getId(), "packDesign", "0");
            packBomQw.eq("bom_version_id", packBomVersion.getId());
            packBomQw.eq("main_flag", "1");
            packBomQw.in("id", packBomIdList);
            List<PackBom> packBomList = packBomService.list(packBomQw);
            if(CollectionUtil.isEmpty(packBomList)){
                //没有物料
                return ApiResult.error("找不到物料！", 500);
            }


            List<String> materialCodeList = new ArrayList<>();
            List<String> supplierIdList = new ArrayList<>();
            for(PackBom bom : packBomList){
                materialCodeList.add(bom.getMaterialCode());
                supplierIdList.add(bom.getSupplierId());
            }

            QueryWrapper<BasicsdatumMaterial> materialQw = new QueryWrapper<>();
            materialQw.in("material_code", materialCodeList);
            List<BasicsdatumMaterial> materialList = materialService.list(materialQw);
            Map<String, BasicsdatumMaterial> materialMap = materialList.stream().
                    collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, item -> item, (item1, item2) -> item1));

            QueryWrapper<BasicsdatumSupplier> supplierQw = new QueryWrapper<>();
            supplierQw.in("supplier_code", supplierIdList);
            List<BasicsdatumSupplier> supplierList = supplierService.list(supplierQw);
            Map<String, BasicsdatumSupplier> supplierMap = supplierList.stream().collect(Collectors.toMap(BasicsdatumSupplier::getSupplierCode, item1 -> item1, (item1, item2) -> item1));

            QueryWrapper<PackBomSize> bomSizeQw;
            QueryWrapper<PackBomColor> bomColorQw;
            List<PurchaseDemand> purchaseDemandList = new ArrayList<>();
            for (PackBom bom : packBomList) {
                //查询物料清单 - 配码
                bomSizeQw = new QueryWrapper<>();
                PackUtils.commonQw(bomSizeQw, packInfo.getId(), "packDesign", null);
                bomSizeQw.eq("bom_id", bom.getId());
                List<PackBomSize> packBomSizeList = packBomSizeService.list(bomSizeQw);
                //查询物料清单 - 配色
                bomColorQw = new QueryWrapper<>();
                PackUtils.commonQw(bomColorQw, packInfo.getId(), "packDesign", null);
                bomColorQw.eq("bom_id", bom.getId());
                List<PackBomColor> packBomColorList = packBomColorService.list(bomColorQw);

                Map<String, PackBomColor>  colorMap = packBomColorList.stream().collect(Collectors.toMap(PackBomColor::getColorCode, item -> item));

                //根据规格分组合计，各个规格的数量
                Map<String, BigDecimal> specificationsMap = new HashMap<>();
                Map<String, PackBomSize> packBomSizeMap = new HashMap<>();
                for (PackBomSize size : packBomSizeList) {
                    BigDecimal num = specificationsMap.get(size.getSize());
                    if (num == null) {
                        specificationsMap.put(size.getSize(), size.getQuantity() == null ? BigDecimal.ZERO : size.getQuantity());
                    } else {
                        specificationsMap.put(size.getSize(), BigDecimalUtil.add(num, size.getQuantity() == null ? BigDecimal.ZERO : size.getQuantity()));
                    }

                    PackBomSize packBomSize = packBomSizeMap.get(size.getSize());
                    if (packBomSize == null) {
                        packBomSizeMap.put(size.getSize(), size);
                    }
                }

                BasicsdatumMaterial material = materialMap.get(bom.getMaterialCode());
                BasicsdatumSupplier supplier = supplierMap.get(bom.getSupplierId());

                BigDecimal num = specificationsMap.get(style.getDefaultSize());
                PackBomSize packBomSize = packBomSizeMap.get(style.getDefaultSize());

                for(String color : colorList) {
                    BaseQueryWrapper<PurchaseDemand> pdQw = new BaseQueryWrapper<>();
                    pdQw.eq("company_code", companyCode);
                    pdQw.eq("pack_bom_id", bom.getId());
                    pdQw.eq("product_color", color);
                    Integer countResult = purchaseDemandMapper.countPurchaseDemand(pdQw);

                    String materialColor = "";
                    PackBomColor packBomColor = colorMap.get(color);
                    if(packBomColor != null){
                        materialColor = packBomColor.getMaterialColorName();
                    }
                    PurchaseDemand demand = new PurchaseDemand(packInfo, bom, material, packBomSize, num);
                    demand.insertInit(userCompany);
                    demand.setId(idGen.nextIdStr());
                    demand.setCompanyCode(companyCode);
                    demand.setProductColor(packBomColor != null ? packBomColor.getColorName() : "");
                    demand.setMaterialColor(materialColor);
                    demand.setMaterialColorCode(packBomColor != null ? packBomColor.getMaterialColorCode() : "");
                    demand.setVersionNo(countResult + 1);
                    demand.setPurchasedNum(BigDecimal.ZERO);
                    demand.setReadyNum(BigDecimal.ZERO);
                    demand.setOrderStatus("0");
                    demand.setStatus("0");
                    demand.setDelFlag("0");
                    demand.setSupplierId(supplier == null ? "" : supplier.getId());
                    purchaseDemandList.add(demand);
                }

//                if(CollectionUtil.isNotEmpty(packBomColorList)) {
//                    for (PackBomColor color : packBomColorList) {
//                        for (Map.Entry<String, BigDecimal> entry : specificationsMap.entrySet()) {
//                            if (material != null) {
//                                PurchaseDemand demand = new PurchaseDemand(packInfo, bom, material, entry.getKey(), entry.getValue());
//                                demand.insertInit(userCompany);
//                                demand.setId(idGen.nextIdStr());
//                                demand.setCompanyCode(companyCode);
//                                demand.setMaterialColor(color.getMaterialColorName());
//                                demand.setPurchasedNum(BigDecimal.ZERO);
//                                demand.setReadyNum(BigDecimal.ZERO);
//                                demand.setOrderStatus("0");
//                                demand.setStatus("0");
//                                demand.setDelFlag("0");
//                                demand.setSupplierId(supplier == null ? "" : supplier.getId());
//                                purchaseDemandList.add(demand);
//                            }
//                        }
//                    }
//                }else{
//                    //没有配色，用物料颜色
//                    for (Map.Entry<String, BigDecimal> entry : specificationsMap.entrySet()) {
//                        if (material != null) {
//                            PurchaseDemand demand = new PurchaseDemand(packInfo, bom, material, entry.getKey(), entry.getValue());
//                            demand.insertInit(userCompany);
//                            demand.setId(idGen.nextIdStr());
//                            demand.setCompanyCode(companyCode);
//                            demand.setPurchasedNum(BigDecimal.ZERO);
//                            demand.setReadyNum(BigDecimal.ZERO);
//                            demand.setOrderStatus("0");
//                            demand.setStatus("0");
//                            demand.setDelFlag("0");
//                            demand.setSupplierId(supplier == null ? "" : supplier.getId());
//                            purchaseDemandList.add(demand);
//                        }
//                    }
//                }
            }

            if(CollectionUtil.isNotEmpty(purchaseDemandList)){
                this.saveBatch(purchaseDemandList);
                return ApiResult.success("生成采购需求成功！", purchaseDemandList.size());
            }
        }
        return ApiResult.error("生成失败！", 500);
    }

    /**
     * 删除相关联设计款号的采购需求数据（设计BOM资料包 解锁触发）
     *
     * @param companyCode 企业编码
     * @param id          资料包id
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteRelationData(String companyCode, String id) {
        QueryWrapper<PackBomVersion> versionQw = new QueryWrapper<>();
        versionQw.eq("id", id);
        List<PackBomVersion> packBomVersionList = packBomVersionService.list(versionQw);

        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("id", packBomVersionList.get(0).getForeignId());
        qw.last("limit 1");
        PackInfo packInfo = packInfoService.getOne(qw);

        //查询没有被采购单引用过的采购需求单，删除
        QueryWrapper<PurchaseDemand> qc = new QueryWrapper<>();
        qc.eq("company_code", companyCode);
        qc.eq("design_style_code", packInfo.getDesignNo());
        qc.eq("plate_bill_code", packInfo.getPatternNo());
        qc.eq("style_bom", packInfo.getName());
        qc.eq("purchased_num", 0);
        List<PurchaseDemand> purchaseDemandList = list(qc);

        if(CollectionUtil.isNotEmpty(purchaseDemandList)){
            physicalDeleteQWrap(qc);
        }
    }

    /**
     * 采购需求单生成采购单
     * @param companyCode 企业编码
     * @param userCompany 用户信息
     * @param purchaseDemandList 采购需求单集合
     * */
    public ApiResult generatePurchaseOrder(UserCompany userCompany, String companyCode, List<PurchaseDemand> purchaseDemandList){
        IdGen idGen = new IdGen();

        //仓库信息
        MaterialWarehouse materialWarehouse = warehouseService.getById(purchaseDemandList.get(0).getWarehouseId());
        String purchaserId = purchaseDemandList.get(0).getPurchaserId();
        String purchaserName = purchaseDemandList.get(0).getPurchaserName();
        Date deliveryDate = purchaseDemandList.get(0).getDeliveryDate();

        List<String> idList = purchaseDemandList.stream().map(PurchaseDemand::getId).collect(Collectors.toList());

        QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
        qw.in("id", idList);
        List<PurchaseDemand> purchaseDemandListPlus = list(qw);

        List<String> supplierCodeList = new ArrayList<>();
        List<String> materialCodeList = new ArrayList<>();
        //根据供应商分组
        Map<String, List<PurchaseDemand>> supplierDemandMap = new HashMap<>();
        for(PurchaseDemand demand : purchaseDemandListPlus){
            List<PurchaseDemand> list = supplierDemandMap.get(demand.getSupplierCode());
            if(CollectionUtil.isEmpty(list)){
                list = new ArrayList<>();
                supplierCodeList.add(demand.getSupplierCode());
            }
            list.add(demand);
            supplierDemandMap.put(demand.getSupplierCode(), list);
            materialCodeList.add(demand.getMaterialCode());
        }

        //供应商信息
        QueryWrapper<BasicsdatumSupplier> supplierQw = new QueryWrapper<>();
        supplierQw.in("supplier_code", supplierCodeList);
        List<BasicsdatumSupplier> supplierList = supplierService.list(supplierQw);
        Map<String, BasicsdatumSupplier> supplierMap = supplierList.stream().collect(Collectors.toMap(BasicsdatumSupplier::getSupplierCode, item1 -> item1, (item1, item2) -> item1));

        //物料信息
        QueryWrapper<BasicsdatumMaterial> materialQw = new QueryWrapper<>();
        materialQw.in("material_code", materialCodeList);
        List<BasicsdatumMaterial> materialList = materialService.list(materialQw);
        Map<String, BasicsdatumMaterial> materialMap = materialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, item -> item, (item1, item2) -> item1));

        String maxCode = purchaseOrderMapper.selectMaxCodeByCompany(companyCode);
        String codeOne = maxCode != null ? maxCode : CodeGen.BEGIN_NUM;

        List<PurchaseOrder> purchaseOrderList = new ArrayList<>();
        List<PurchaseOrderDetail> purchaseOrderDetailList = new ArrayList<>();
        for(Map.Entry<String, List<PurchaseDemand>> entry : supplierDemandMap.entrySet()){
            BasicsdatumSupplier supplier = supplierMap.get(entry.getKey());
            String code = "SO" + CodeGen.getBoxCode(2, codeOne);

            String id = idGen.nextIdStr();
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.insertInit(userCompany);
            purchaseOrder.setId(id);
            purchaseOrder.setCode(code);
            purchaseOrder.setCompanyCode(companyCode);
            purchaseOrder.setStatus("0");
            purchaseOrder.setOrderStatus("0");
            purchaseOrder.setWarehouseStatus("0");
            purchaseOrder.setDistributeStatus("0");
            purchaseOrder.setDelFlag("0");

            purchaseOrder.setSupplierId(supplier.getId());
            purchaseOrder.setSupplierCode(supplier.getSupplierCode());
            purchaseOrder.setSupplierName(supplier.getSupplier());
            purchaseOrder.setSupplierContacts(supplier.getContact());
            purchaseOrder.setSupplierPhone(supplier.getTelephone());
            purchaseOrder.setSupplierAddress(supplier.getAddress());

            purchaseOrder.setPurchaserId(purchaserId);
            purchaseOrder.setPurchaserName(purchaserName);

            purchaseOrder.setWarehouseId(materialWarehouse.getId());
            purchaseOrder.setWarehouseName(materialWarehouse.getWarehouseName());
            purchaseOrder.setWarehouseContacts(materialWarehouse.getContacts());
            purchaseOrder.setWarehousePhone(materialWarehouse.getPhone());
            purchaseOrder.setWarehouseAddress(materialWarehouse.getAddress());

            purchaseOrder.setDeliveryDate(deliveryDate);

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for(PurchaseDemand demandInfo : entry.getValue()){
                BasicsdatumMaterial material = materialMap.get(demandInfo.getMaterialCode());
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.insertInit(userCompany);
                detail.setId(idGen.nextIdStr());
                detail.setCompanyCode(companyCode);
                detail.setPurchaseOrderId(id);
                detail.setDemandId(demandInfo.getId());

                detail.setMaterialCode(demandInfo.getMaterialCode());
                detail.setMaterialName(demandInfo.getMaterialName());
                detail.setDesignStyleCode(demandInfo.getDesignStyleCode());
                detail.setPlateBillCode(demandInfo.getPlateBillCode());
                detail.setMaterialSpecifications(demandInfo.getMaterialSpecifications());
                detail.setMaterialSpecificationsCode(demandInfo.getMaterialSpecificationsCode());
                detail.setStyleName(demandInfo.getStyleName());
                detail.setDeliveryDate(deliveryDate);

                detail.setSupplierColor(demandInfo.getSupplierColor());
                detail.setPurchaseUnit(demandInfo.getUnit());
                detail.setPurchaseUnitName(demandInfo.getUnitName());
                detail.setConvertUnitRatio(new BigDecimal(material.getPurchaseConvertStock()));
                detail.setPurchaseNum(demandInfo.getNeedNum());
                detail.setPrice(demandInfo.getPrice());
                detail.setMaterialColor(demandInfo.getMaterialColor());
                detail.setMaterialColorCode(demandInfo.getMaterialColorCode());
                detail.setProductColor(demandInfo.getProductColor());
                detail.setLoss(demandInfo.getLoss());
                BigDecimal amount = BigDecimalUtil.mul(demandInfo.getNeedNum(), demandInfo.getPrice()).setScale(2, BigDecimal.ROUND_DOWN);
                detail.setTotalAmount(amount);
                detail.setWarehouseNum(BigDecimal.ZERO);

                purchaseOrderDetailList.add(detail);

                total = BigDecimalUtil.add(total, demandInfo.getNeedNum());
                totalAmount = BigDecimalUtil.add(totalAmount, amount);
            }

            purchaseOrder.setTotal(total);
            purchaseOrder.setTotalAmount(totalAmount);
            codeOne = code;

            purchaseOrderList.add(purchaseOrder);
        }

        boolean result = purchaseOrderService.saveBatch(purchaseOrderList);
        if(result){
            purchaseOrderDetailService.saveBatch(purchaseOrderDetailList);
            manipulatePlanNum(purchaseOrderDetailList, "0");
            return ApiResult.success("生成完成！");
        }
        return ApiResult.error("生成失败！", 500);
    }

    /**
     * 用于 采购单 操作采购需求单的已采购数量
     * @param type 0 占用  1 返还
     * */
    public void manipulatePlanNum(List<PurchaseOrderDetail> purchaseOrderDetailList, String type){
        List<String> idList = new ArrayList<>();
        for(PurchaseOrderDetail detail : purchaseOrderDetailList){
            if(StringUtils.isNotBlank(detail.getDemandId())){
                idList.add(detail.getDemandId());
            }
        }

        if(CollectionUtil.isEmpty(idList)){
            return;
        }

        QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
        qw.in("id", idList);
        List<PurchaseDemand> purchaseDemandList = list(qw);
        if(CollectionUtil.isEmpty(purchaseDemandList)){
            return;
        }

        Map<String, PurchaseDemand> purchaseDemandMap = purchaseDemandList.stream().collect(Collectors.toMap(PurchaseDemand::getId, item -> item));

        for(PurchaseOrderDetail details : purchaseOrderDetailList){
            PurchaseDemand item = purchaseDemandMap.get(details.getDemandId());
            if(item != null){
                if(StringUtils.equals(type, "0")){
                    item.setPurchasedNum(BigDecimalUtil.add(item.getPurchasedNum(), details.getPurchaseNum()));
                }else{
                    item.setPurchasedNum(BigDecimalUtil.sub(item.getPurchasedNum(), details.getPurchaseNum()));
                }
            }
            purchaseDemandMap.put(details.getDemandId(), item);
        }

        List<PurchaseDemand> updateList = new ArrayList<>(purchaseDemandMap.values());
        updateBatchById(updateList);
    }

    /**
     * 用于 出库单 操作采购需求单的已配料数量
     * @param type 0 占用  1 返还
     * */
    @Override
    public void manipulateReadyNum(List<OutboundOrderDetail> outboundOrderDetailList, String type) {
        List<String> idList = new ArrayList<>();
        for(OutboundOrderDetail detail : outboundOrderDetailList){
            if(StringUtils.isNotBlank(detail.getSourceId())){
                idList.add(detail.getSourceId());
            }
        }

        if(CollectionUtil.isEmpty(idList)){
            return;
        }

        QueryWrapper<PurchaseDemand> qw = new QueryWrapper<>();
        qw.in("id", idList);
        List<PurchaseDemand> purchaseDemandList = list(qw);
        if(CollectionUtil.isEmpty(purchaseDemandList)){
            return;
        }

        Map<String, PurchaseDemand> purchaseDemandMap = purchaseDemandList.stream().collect(Collectors.toMap(PurchaseDemand::getId, item -> item));

        for(OutboundOrderDetail details : outboundOrderDetailList){
            PurchaseDemand item = purchaseDemandMap.get(details.getSourceId());
            if(item != null){
                if(StringUtils.equals(type, "0")){
                    item.setPurchasedNum(BigDecimalUtil.add(item.getReadyNum(), details.getOutNum()));
                }else{
                    item.setPurchasedNum(BigDecimalUtil.sub(item.getReadyNum(), details.getOutNum()));
                }
            }
            purchaseDemandMap.put(details.getSourceId(), item);
        }

        List<PurchaseDemand> updateList = new ArrayList<>(purchaseDemandMap.values());
        updateBatchById(updateList);
    }


}

