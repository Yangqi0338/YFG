/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.entity.OpenUserDto;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.mapper.DeliveryNoticeMapper;
import com.base.sbc.module.purchase.entity.DeliveryNotice;
import com.base.sbc.module.purchase.service.DeliveryNoticeService;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import com.base.sbc.module.purchase.service.PurchaseOrderService;
import com.base.sbc.open.dto.OpenMaterialNoticeDto;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private BasicsdatumSupplierService supplierService;

    @Autowired
    private AmcService amcService;

    @Autowired
    private BaseController baseController;

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
            deliveryNotice.setPurchaseOrderDetailId(detail.getId());
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

    /**
     * 保存通知单（领猫）
     * @param noticeDtoList
     * @return
     */
    @Override
    public ApiResult saveNoticeList(List<OpenMaterialNoticeDto> noticeDtoList) {
        ApiResult result = ApiResult.success("新增成功");
        result.setStatus(200);
        List<String> purchaseCodeList = new ArrayList<>();
        List<String> supplierCodeList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (OpenMaterialNoticeDto noticeDto : noticeDtoList) {
            if (!supplierCodeList.contains(noticeDto.getSupplierName())){
                supplierCodeList.add(noticeDto.getSupplierName());
            }
            if (!purchaseCodeList.contains(noticeDto.getPurchaseCode())){
                purchaseCodeList.add(noticeDto.getPurchaseCode());
            }
            if (!names.contains(noticeDto.getPurchaseCode())){
                names.add(noticeDto.getPurchaserName());
            }
        }

        //查询采购单明细数据
        Map<String, PurchaseOrderDetail> purchaseOrderDetailMap = Maps.newHashMap();
        if (purchaseCodeList.size() > 0){
            QueryWrapper<PurchaseOrderDetail> purchaseQw = new QueryWrapper<>();
            purchaseQw.in("tpo.code",purchaseCodeList);
            List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderDetailService.selectPurchaseCode(purchaseQw);
            purchaseOrderDetailMap = purchaseOrderDetailList.stream()
                    .collect(Collectors.toMap(p -> p.getCode()+p.getMaterialCode()+p.getMaterialColor()+p.getMaterialSpecifications(),p->p,(p1,p2)->p2));
        }

        //查询供应商数据
        Map<String, BasicsdatumSupplier> supplierMap = Maps.newHashMap();
        if (supplierCodeList.size() > 0){
            QueryWrapper<BasicsdatumSupplier> supplierQw = new QueryWrapper<>();
            supplierQw.in("supplier_code",supplierCodeList);
            supplierMap = supplierService.list(supplierQw).stream()
                    .collect(Collectors.toMap(s -> s.getSupplierCode(), s -> s, (s1, s2) -> s2));
        }

        //查询用户
        Map<String, OpenUserDto> userMap = Maps.newHashMap();
        if (names.size() > 0){
            ApiResult amcResult = amcService.getUserListByNames(StringUtils.convertListToString(names));
            if (amcResult != null
                    && amcResult.getSuccess() != null
                    && amcResult.getSuccess()
                    && ObjectUtil.isNotEmpty(amcResult.getData())){
                List<OpenUserDto> data = (List<OpenUserDto>) amcResult.getData();
                for (OpenUserDto user : data) {
                    userMap.put(user.getName(),user);
                }
            }
        }


        IdGen idGen = new IdGen();
        String name = "LinkMore";
        Date date = new Date();
        List<DeliveryNotice> noticeList = new ArrayList<>();
        for (OpenMaterialNoticeDto noticeDto : noticeDtoList) {
            DeliveryNotice notice = new DeliveryNotice();
            BeanUtils.copyProperties(noticeDto,notice);

            PurchaseOrderDetail detail = purchaseOrderDetailMap.get(notice.getPurchaseCode() + notice.getMaterialCode() + notice.getMaterialColor() + notice.getMaterialSpecifications());
            if (detail != null){
                notice.setPurchaseOrderDetailId(detail.getId());
            }else {
                break;
            }

            BasicsdatumSupplier supplier = supplierMap.get(notice.getSupplierName());
            if (supplier != null){
                notice.setSupplierId(supplier.getId());
                notice.setSupplierName(supplier.getSupplierAbbreviation());
            }else {
                break;
            }

            OpenUserDto user = userMap.get(notice.getPurchaserName());
            if (user != null){
                notice.setPurchaserId(user.getId());
            }else {
                break;
            }

            notice.preInsert(String.valueOf(idGen.nextId()));
            notice.setCompanyCode(baseController.getUserCompany());
            notice.setCreateName(name);
            notice.setCreateDate(date);
            notice.setUpdateName(name);
            notice.setUpdateDate(date);
            noticeList.add(notice);
        }
        this.saveBatch(noticeList);
        return result;
    }
}
