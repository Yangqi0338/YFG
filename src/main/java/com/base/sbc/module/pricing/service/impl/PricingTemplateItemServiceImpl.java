/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pricing.dto.PricingTemplateItemDTO;
import com.base.sbc.module.pricing.entity.PricingTemplateItem;
import com.base.sbc.module.pricing.mapper.PricingTemplateItemMapper;
import com.base.sbc.module.pricing.service.PricingTemplateItemService;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：核价模板明细表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingTemplateItemService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:36
 */
@Service
public class PricingTemplateItemServiceImpl extends BaseServiceImpl<PricingTemplateItemMapper, PricingTemplateItem> implements PricingTemplateItemService {
    // 自定义方法区 不替换的区域【other_start】
    @Autowired
    private PricingTemplateItemMapper pricingTemplateItemMapper;

    @Override
    public List<PricingTemplateItemVO> getByPricingTemplateId(String pricingTemplateId, String userCompany) {
        QueryWrapper<PricingTemplateItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pricing_template_id", pricingTemplateId);
        queryWrapper.eq("company_code", userCompany);
        queryWrapper.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL).lambda();
        return pricingTemplateItemMapper.getByPricingTemplateId(pricingTemplateId, userCompany);
    }

    @Override
    public void delByPricingTemplateId(String pricingTemplateId, String userCompany) {
        LambdaUpdateWrapper<PricingTemplateItem> wrapper = new LambdaUpdateWrapper<PricingTemplateItem>()
                .set(PricingTemplateItem::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .eq(PricingTemplateItem::getPricingTemplateId, pricingTemplateId)
                .eq(PricingTemplateItem::getCompanyCode, userCompany);
        pricingTemplateItemMapper.delete(wrapper);
    }

    @Override
    public void saveBatch(List<PricingTemplateItemDTO> pricingTemplateItemDTOS,
                          String pricingTemplateId, String userCompany) {
        if (CollUtil.isEmpty(pricingTemplateItemDTOS)) {
            LambdaQueryWrapper<PricingTemplateItem> queryWrapper = new QueryWrapper<PricingTemplateItem>()
                    .lambda()
                    .eq(PricingTemplateItem::getPricingTemplateId, pricingTemplateId)
                    .eq(PricingTemplateItem::getDelFlag, "0");
            super.getBaseMapper().delete(queryWrapper);
            return;
        }

        List<String> ids = this.getIdByPricingTemplateId(pricingTemplateId);
        IdGen idGen = new IdGen();
        List<PricingTemplateItem> pricingTemplateItems = pricingTemplateItemDTOS.stream()
                .map(item -> {
                    PricingTemplateItem pricingTemplateItem = CopyUtil.copy(item, PricingTemplateItem.class);
                    if (StringUtils.isEmpty(pricingTemplateItem.getId())) {
                        pricingTemplateItem.insertInit();
                        pricingTemplateItem.setId(idGen.nextIdStr());
                        pricingTemplateItem.setCompanyCode(userCompany);
                    } else {
                        pricingTemplateItem.updateInit();
                    }
                    pricingTemplateItem.setPricingTemplateId(pricingTemplateId);
                    ids.remove(item.getId());
                    return pricingTemplateItem;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(pricingTemplateItems);
        if (CollUtil.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    private List<String> getIdByPricingTemplateId(String pricingTemplateId) {
        LambdaQueryWrapper<PricingTemplateItem> qw = new QueryWrapper<PricingTemplateItem>().lambda()
                .eq(PricingTemplateItem::getPricingTemplateId, pricingTemplateId)
                .eq(PricingTemplateItem::getDelFlag, "0")
                .select(PricingTemplateItem::getId);
        List<PricingTemplateItem> list = super.list(qw);
        return CollUtil.isEmpty(list) ? Lists.newArrayList() : list.stream()
                .map(PricingTemplateItem::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}
