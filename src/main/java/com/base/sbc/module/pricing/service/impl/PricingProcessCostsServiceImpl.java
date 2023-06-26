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
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.pricing.dto.PricingProcessCostsDTO;
import com.base.sbc.module.pricing.entity.PricingProcessCosts;
import com.base.sbc.module.pricing.mapper.PricingProcessCostsMapper;
import com.base.sbc.module.pricing.service.PricingProcessCostsService;
import com.base.sbc.module.pricing.vo.PricingProcessCostsVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：加工费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingProcessCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:31
 */
@Service
public class PricingProcessCostsServiceImpl extends ServicePlusImpl<PricingProcessCostsMapper, PricingProcessCosts> implements PricingProcessCostsService {
    @Autowired
    private PricingProcessCostsMapper pricingProcessCostsMapper;

    @Override
    public List<PricingProcessCostsVO> getByPricingCode(String pricingCode, String userCompany) {
        return pricingProcessCostsMapper.getByPricingCode(pricingCode, userCompany);
    }

    @Override
    public void delByPricingCode(String pricingCode, String userCompany) {
        LambdaUpdateWrapper<PricingProcessCosts> wrapper = new LambdaUpdateWrapper<PricingProcessCosts>()
                .set(PricingProcessCosts::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingProcessCosts::getPricingCode, pricingCode)
                .eq(PricingProcessCosts::getCompanyCode, userCompany);
        pricingProcessCostsMapper.delete(wrapper);

    }

    @Override
    public void insert(List<PricingProcessCostsDTO> pricingProcessCostsDTOS, String pricingCode, String userCompany) {
        if (CollectionUtils.isEmpty(pricingProcessCostsDTOS)) {
            return;
        }
        IdGen idGen = new IdGen();
        List<PricingProcessCosts> processCosts = pricingProcessCostsDTOS.stream()
                .map(pricingCraftCostsDTO -> {
                    PricingProcessCosts pricingProcessCosts = new PricingProcessCosts();
                    BeanUtils.copyProperties(pricingCraftCostsDTO, pricingProcessCosts);
                    if (StringUtils.isNotEmpty(pricingProcessCosts.getId())) {
                        pricingProcessCosts.updateInit();
                    } else {
                        pricingProcessCosts.preInsert(idGen.nextIdStr());
                    }
                    pricingProcessCosts.setCompanyCode(userCompany);
                    pricingProcessCosts.setPricingCode(pricingCode);
                    return pricingProcessCosts;
                }).collect(Collectors.toList());
        super.saveBatch(processCosts);
    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
