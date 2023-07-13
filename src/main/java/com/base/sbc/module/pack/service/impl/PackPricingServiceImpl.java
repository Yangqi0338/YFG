/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.entity.PackPricing;
import com.base.sbc.module.pack.mapper.PackPricingMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.PackPricingVo;
import org.nfunk.jep.JEP;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：资料包-核价信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:16
 */
@Service
public class PackPricingServiceImpl extends PackBaseServiceImpl<PackPricingMapper, PackPricing> implements PackPricingService {


// 自定义方法区 不替换的区域【other_start】

    @Resource
    PackPricingOtherCostsService packPricingOtherCostsService;
    @Resource
    PackBomService packBomService;
    @Resource
    PackPricingProcessCostsService packPricingProcessCostsService;
    @Resource
    PackPricingCraftCostsService packPricingCraftCostsService;

    @Override
    public PackPricingVo getDetail(PackCommonSearchDto dto) {
        PackPricing one = get(dto.getForeignId(), dto.getPackType());
        PackPricingVo packPricingVo = BeanUtil.copyProperties(one, PackPricingVo.class);
        return packPricingVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackPricingVo saveByDto(PackPricingDto dto) {
        PackPricing one = get(dto.getForeignId(), dto.getPackType());
        if (one == null) {
            one = new PackPricing();
            BeanUtil.copyProperties(dto, one);
            save(one);

        } else {
            BeanUtil.copyProperties(dto, one, "id");
            updateById(one);
        }
        return BeanUtil.copyProperties(one, PackPricingVo.class);
    }

    @Override
    public Map<String, BigDecimal> calculateCosts(PackCommonSearchDto dto) {
        Map<String, BigDecimal> temp = new HashMap<>(16);
        //其他费用统计
        Map<String, BigDecimal> otherStatistics = packPricingOtherCostsService.statistics(dto);
        temp.putAll(otherStatistics);
        //物料费用统计
        temp.put("物料费", packBomService.calculateCosts(dto));
        //统计加工费用
        temp.put("加工费", packPricingProcessCostsService.calculateCosts(dto));
        //统计二次加工费用
        temp.put("二次加工费", packPricingCraftCostsService.calculateCosts(dto));
        Map<String, BigDecimal> result = new HashMap<>(16);
        for (Map.Entry<String, BigDecimal> a : temp.entrySet()) {
            if (a.getValue() != null) {
                result.put(a.getKey(), a.getValue().setScale(2, RoundingMode.HALF_UP));
            }
        }
        return result;
    }

    @Override
    public BigDecimal formula(String formula, Map<String, Object> itemVal) {
        JEP jep = new JEP();
        for (Map.Entry<String, Object> item : itemVal.entrySet()) {
            if (NumberUtil.isNumber(String.valueOf(item.getValue()))) {
                jep.addVariable(item.getKey(), Double.valueOf(item.getValue().toString()));
            }
        }
        jep.parseExpression(formula);
        double value = jep.getValue();
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    String getModeName() {
        return "核价信息";
    }

// 自定义方法区 不替换的区域【other_end】

}
