/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialPriceMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-物料档案-供应商报价 service类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:25
 */
@Service
public class BasicsdatumMaterialPriceServiceImpl
        extends BaseServiceImpl<BasicsdatumMaterialPriceMapper, BasicsdatumMaterialPrice>
        implements BasicsdatumMaterialPriceService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<BomSelMaterialVo> findDefaultToBomSel(List<String> materialCodeList) {
        QueryWrapper<BasicsdatumMaterialPrice> qw = new QueryWrapper<>();
        qw.in("p.material_code", materialCodeList);
        qw.eq("p.company_code", getCompanyCode());
        return getBaseMapper().findDefaultToBomSel(qw);
    }

    @Override
    public Map<String, BigDecimal> getDefaultSupplerQuotationPrice(List<String> materialCodes) {
        if (CollectionUtils.isEmpty(materialCodes)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<BasicsdatumMaterialPrice> qw = new QueryWrapper<BasicsdatumMaterialPrice>().lambda()
                .eq(BasicsdatumMaterialPrice::getMaterialCode, materialCodes)
                .eq(BasicsdatumMaterialPrice::getCompanyCode, super.getCompanyCode())
                .eq(BasicsdatumMaterialPrice::getDelFlag, "0")
                .eq(BasicsdatumMaterialPrice::getSelectFlag, Boolean.TRUE)
                .select(BasicsdatumMaterialPrice::getQuotationPrice, BasicsdatumMaterialPrice::getMaterialCode);
        List<BasicsdatumMaterialPrice> list = super.list(qw);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.toMap(BasicsdatumMaterialPrice::getMaterialCode, BasicsdatumMaterialPrice::getQuotationPrice, (k1, k2) -> k2));
    }

    @Override
    public  void copyByMaterialCode(String materialCode, String newMaterialCode) {
        QueryWrapper<BasicsdatumMaterialPrice> qw = new QueryWrapper<>();
        qw.eq("material_code", materialCode);
        qw.eq("company_code", getCompanyCode());
        List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = super.list(qw);
        if (CollectionUtils.isNotEmpty(basicsdatumMaterialPrices)) {
            basicsdatumMaterialPrices.forEach(e -> {
                e.insertInit();
                e.setUpdateId(null);
                e.setUpdateName(null);
                e.setUpdateDate(null);
                e.setId(null);
                e.setMaterialCode(newMaterialCode);
            });
            super.saveBatch(basicsdatumMaterialPrices);
        }
    }

    @Override
    public String getMaterialCodeBySupplierInfo(MaterialSupplierInfo materialSupplierInfo) {
        QueryWrapper qw = new QueryWrapper<>();
        qw.in("p.supplier_material_code", materialSupplierInfo.getSupplierMaterialCode());
        qw.eq("bu.supplier_abbreviation", materialSupplierInfo.getSupplierAbbreviation());
        return getBaseMapper().supplierAbbreviation(qw);
    }

// 自定义方法区 不替换的区域【other_end】

}

