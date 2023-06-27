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
import com.base.sbc.module.pricing.dto.PricingColorDTO;
import com.base.sbc.module.pricing.entity.PricingColor;
import com.base.sbc.module.pricing.mapper.PricingColorMapper;
import com.base.sbc.module.pricing.service.PricingColorService;
import com.base.sbc.module.pricing.vo.PricingColorVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：核价颜色 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingColorService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:20
 */
@Service
public class PricingColorServiceImpl extends BaseServiceImpl<PricingColorMapper, PricingColor> implements PricingColorService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private PricingColorMapper pricingColorMapper;

    @Override
    public List<PricingColorVO> getByPricingCode(String pricingCode, String userCompany) {
        return pricingColorMapper.getByPricingCode(pricingCode, userCompany);
    }

    @Override
    public void delByPricingCode(String pricingCode, String userCompany) {
        LambdaUpdateWrapper<PricingColor> wrapper = new LambdaUpdateWrapper<PricingColor>()
                .set(PricingColor::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingColor::getPricingCode, pricingCode)
                .eq(PricingColor::getCompanyCode, userCompany);
        pricingColorMapper.delete(wrapper);
    }

    @Override
    public Map<String, String> insert(List<PricingColorDTO> pricingColorDTOS, String pricingCode, String userCompany) {
        if (CollectionUtils.isEmpty(pricingColorDTOS)) {
            return new HashMap<>(0);
        }
        IdGen idGen = new IdGen();
        Map<String, String> map = new HashMap<>();
        List<PricingColor> pricingColors = pricingColorDTOS.stream()
                .map(pricingColorDTO -> {
                    PricingColor pricingColor = new PricingColor();
                    BeanUtils.copyProperties(pricingColorDTO, pricingColor);
                    pricingColor.preInsert(idGen.nextIdStr());
                    pricingColor.setCompanyCode(userCompany);
                    pricingColor.setPricingCode(pricingCode);
                    map.put(pricingColorDTO.getColorCode(), pricingColor.getId());
                    return pricingColor;
                }).collect(Collectors.toList());
        super.saveBatch(pricingColors);
        return map;
    }


// 自定义方法区 不替换的区域【other_end】

}
