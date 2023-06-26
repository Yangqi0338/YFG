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
import com.base.sbc.module.pricing.dto.PricingOtherCostsDTO;
import com.base.sbc.module.pricing.entity.PricingOtherCosts;
import com.base.sbc.module.pricing.mapper.PricingOtherCostsMapper;
import com.base.sbc.module.pricing.service.PricingOtherCostsService;
import com.base.sbc.module.pricing.vo.PricingOtherCostsVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：其他费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingOtherCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:28
 */
@Service
public class PricingOtherCostsBaseServiceImpl extends BaseServiceImpl<PricingOtherCostsMapper, PricingOtherCosts> implements PricingOtherCostsService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private PricingOtherCostsMapper pricingOtherCostsMapper;

    @Override
    public List<PricingOtherCostsVO> getByPricingCode(String pricingCode, String userCompany) {
        return pricingOtherCostsMapper.getByPricingCode(pricingCode, userCompany);
    }

    @Override
    public void delByPricingCode(String pricingCode, String userCompany) {
        LambdaUpdateWrapper<PricingOtherCosts> wrapper = new LambdaUpdateWrapper<PricingOtherCosts>()
                .set(PricingOtherCosts::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingOtherCosts::getPricingCode, pricingCode)
                .eq(PricingOtherCosts::getCompanyCode, userCompany);
        pricingOtherCostsMapper.delete(wrapper);
    }

    @Override
    public void insert(List<PricingOtherCostsDTO> pricingOtherCostsDTOS, String pricingCode, String userCompany) {
        if (CollectionUtils.isEmpty(pricingOtherCostsDTOS)) {
            return;
        }
        IdGen idGen = new IdGen();
        List<PricingOtherCosts> pricingOtherCosts = pricingOtherCostsDTOS.stream()
                .map(pricingCraftCostsDTO -> {
                    PricingOtherCosts otherCosts = new PricingOtherCosts();
                    BeanUtils.copyProperties(pricingCraftCostsDTO, otherCosts);
                    otherCosts.preInsert(idGen.nextIdStr());
                    otherCosts.setCompanyCode(userCompany);
                    otherCosts.setPricingCode(pricingCode);
                    return otherCosts;
                }).collect(Collectors.toList());
        super.saveBatch(pricingOtherCosts);
    }


// 自定义方法区 不替换的区域【other_end】

}
