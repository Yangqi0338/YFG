/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pricing.dto.PricingMaterialCostsDTO;
import com.base.sbc.module.pricing.entity.PricingMaterialCosts;
import com.base.sbc.module.pricing.mapper.PricingMaterialCostsMapper;
import com.base.sbc.module.pricing.service.PricingMaterialCostsService;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：物料费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingMaterialCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:25
 */
@Service
public class PricingMaterialCostsBaseServiceImpl extends BaseServiceImpl<PricingMaterialCostsMapper, PricingMaterialCosts> implements PricingMaterialCostsService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private PricingMaterialCostsMapper pricingMaterialCostsMapper;

    @Override
    public List<PricingMaterialCostsVO> getByPricingCode(String pricingCode, String userCompany) {
        return pricingMaterialCostsMapper.getByPricingCode(pricingCode, userCompany);
    }

    @Override
    public void delByPricingCode(String pricingCode, String userCompany) {
        LambdaUpdateWrapper<PricingMaterialCosts> wrapper = new LambdaUpdateWrapper<PricingMaterialCosts>()
                .set(PricingMaterialCosts::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingMaterialCosts::getPricingCode, pricingCode)
                .eq(PricingMaterialCosts::getCompanyCode, userCompany);
        pricingMaterialCostsMapper.delete(wrapper);
    }

    @Override
    public void insert(List<PricingMaterialCostsDTO> pricingMaterialCostsDTOS, Map<String, String> pricingColorMap,
                       String pricingCode, String userCompany) {
        if (CollectionUtils.isEmpty(pricingMaterialCostsDTOS)) {
            return;
        }
        IdGen idGen = new IdGen();
        List<PricingMaterialCosts> materialCosts = pricingMaterialCostsDTOS.stream()
                .map(pricingCraftCostsDTO -> {
                    PricingMaterialCosts pricingMaterialCosts = new PricingMaterialCosts();
                    BeanUtils.copyProperties(pricingCraftCostsDTO, pricingMaterialCosts);
                    pricingMaterialCosts.preInsert(idGen.nextIdStr());
                    pricingMaterialCosts.setCompanyCode(userCompany);
                    pricingMaterialCosts.setPricingCode(pricingCode);
                    pricingMaterialCosts.setPricingColorId(pricingColorMap.get(pricingCraftCostsDTO.getColorCode()));
                    return pricingMaterialCosts;
                }).collect(Collectors.toList());

        super.saveBatch(materialCosts);
    }
// 自定义方法区 不替换的区域【other_end】

}
