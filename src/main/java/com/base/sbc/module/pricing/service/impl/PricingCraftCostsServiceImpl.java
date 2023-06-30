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
import com.base.sbc.module.pricing.dto.PricingCraftCostsDTO;
import com.base.sbc.module.pricing.entity.PricingCraftCosts;
import com.base.sbc.module.pricing.mapper.PricingCraftCostsMapper;
import com.base.sbc.module.pricing.service.PricingCraftCostsService;
import com.base.sbc.module.pricing.vo.PricingCraftCostsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：核价工艺费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingCraftCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:22
 */
@Service
public class PricingCraftCostsServiceImpl extends BaseServiceImpl<PricingCraftCostsMapper, PricingCraftCosts> implements PricingCraftCostsService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private PricingCraftCostsMapper pricingCraftCostsMapper;

    @Override
    public List<PricingCraftCostsVO> getByPricingCode(String pricingCode, String userCompany) {
        return pricingCraftCostsMapper.getByPricingCode(pricingCode, userCompany);
    }

    @Override
    public void delByPricingCode(String pricingCode, String userCompany) {
        LambdaUpdateWrapper<PricingCraftCosts> wrapper = new LambdaUpdateWrapper<PricingCraftCosts>()
                .set(PricingCraftCosts::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingCraftCosts::getPricingCode, pricingCode)
                .eq(PricingCraftCosts::getCompanyCode, userCompany);
        pricingCraftCostsMapper.delete(wrapper);
    }

    @Override
    public void insert(List<PricingCraftCostsDTO> pricingCraftCostsDTOS, String pricingCode, String userCompany) {
        if (CollectionUtils.isEmpty(pricingCraftCostsDTOS)) {
            return;
        }
        IdGen idGen = new IdGen();
        List<PricingCraftCosts> pricingColors = pricingCraftCostsDTOS.stream()
                .map(pricingCraftCostsDTO -> {
                    PricingCraftCosts pricingCraftCosts = new PricingCraftCosts();
                    BeanUtils.copyProperties(pricingCraftCostsDTO, pricingCraftCosts);
                    if (StringUtils.isNotEmpty(pricingCraftCosts.getId())) {
                        pricingCraftCosts.updateInit();
                    } else {
                        pricingCraftCosts.preInsert(idGen.nextIdStr());
                    }
                    pricingCraftCosts.setCompanyCode(userCompany);
                    pricingCraftCosts.setPricingCode(pricingCode);
                    return pricingCraftCosts;
                }).collect(Collectors.toList());
        super.saveBatch(pricingColors);
    }


// 自定义方法区 不替换的区域【other_end】

}
