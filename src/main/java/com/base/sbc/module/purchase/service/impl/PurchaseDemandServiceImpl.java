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
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.purchase.mapper.PurchaseDemandMapper;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private PackBomVersionService packBomVersionService;

    @Autowired
    private BasicsdatumMaterialService materialService;

    @Autowired
    private BasicsdatumSupplierService supplierService;

    @Override
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
    public void generatePurchaseDemand(UserCompany userCompany, String companyCode, String id) {
        IdGen idGen = new IdGen();

        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("id", id);
        qw.last("limit 1");
        PackInfo packInfo = packInfoService.getOne(qw);

        //查询资料包-物料清单-物料版本 启用状态的数据
        QueryWrapper<PackBomVersion> versionQw = new QueryWrapper<>();
        PackUtils.commonQw(versionQw, id, "packDesign", "1");
        List<PackBomVersion> packBomVersionList = packBomVersionService.list(versionQw);

        if (CollectionUtil.isNotEmpty(packBomVersionList)) {
            //再根据版本，查找物料清单
            PackBomVersion packBomVersion = packBomVersionList.get(0);

            QueryWrapper<PackBom> packBomQw = new QueryWrapper<>();
            PackUtils.commonQw(packBomQw, id, "packDesign", "1");
            packBomQw.eq("bom_version_id", packBomVersion.getId());
            List<PackBom> packBomList = packBomService.list(packBomQw);

            List<String> materialIdList = new ArrayList<>();
            List<String> supplierIdList = new ArrayList<>();
            for(PackBom bom : packBomList){
                materialIdList.add(bom.getMaterialId());
                supplierIdList.add(bom.getSupplierId());
            }

            QueryWrapper<BasicsdatumMaterial> materialQw = new QueryWrapper<>();
            materialQw.in("id", materialIdList);
            List<BasicsdatumMaterial> materialList = materialService.list(materialQw);
            Map<String, BasicsdatumMaterial> materialMap = materialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getId, item -> item));

            QueryWrapper<BasicsdatumSupplier> supplierQw = new QueryWrapper<>();
            supplierQw.in("supplier_code", supplierIdList);
            List<BasicsdatumSupplier> supplierList = supplierService.list(supplierQw);
            Map<String, BasicsdatumSupplier> supplierMap = supplierList.stream().collect(Collectors.toMap(BasicsdatumSupplier::getSupplierCode, item1 -> item1, (item1, item2) -> item1));

            QueryWrapper<PackBomSize> bomSizeQw;
            List<PurchaseDemand> purchaseDemandList = new ArrayList<>();
            for (PackBom bom : packBomList) {
                //查询物料清单 - 配码
                bomSizeQw = new QueryWrapper<>();
                PackUtils.commonQw(bomSizeQw, id, "packDesign", null);
                bomSizeQw.eq("bom_id", bom.getId());
                List<PackBomSize> packBomSizeList = packBomSizeService.list(bomSizeQw);

                //根据规格分组合计，各个规格的数量
                Map<String, BigDecimal> specificationsMap = new HashMap<>();
                for (PackBomSize size : packBomSizeList) {
                    BigDecimal num = specificationsMap.get(size.getWidth());
                    if (num == null) {
                        specificationsMap.put(size.getWidth(), size.getQuantity() == null ? BigDecimal.ZERO : size.getQuantity());
                    } else {
                        specificationsMap.put(size.getWidth(), BigDecimalUtil.add(num, size.getQuantity() == null ? BigDecimal.ZERO : size.getQuantity()));
                    }
                }

                BasicsdatumMaterial material = materialMap.get(bom.getMaterialId());
                BasicsdatumSupplier supplier = supplierMap.get(bom.getSupplierId());

                for (Map.Entry<String, BigDecimal> entry : specificationsMap.entrySet()) {
                    PurchaseDemand demand = new PurchaseDemand(packInfo, bom, material, entry.getKey(), entry.getValue());
                    demand.insertInit(userCompany);
                    demand.setId(idGen.nextIdStr());
                    demand.setCompanyCode(companyCode);
                    demand.setPurchasedNum(BigDecimal.ZERO);
                    demand.setOrderStatus("0");
                    demand.setStatus("0");
                    demand.setDelFlag("0");
                    demand.setSupplierId(supplier.getId());
                    purchaseDemandList.add(demand);
                }
            }

            if(CollectionUtil.isNotEmpty(purchaseDemandList)){
                this.saveBatch(purchaseDemandList);
            }
        }
    }

}

