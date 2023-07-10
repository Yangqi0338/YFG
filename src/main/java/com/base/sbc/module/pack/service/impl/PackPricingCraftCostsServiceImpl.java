/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingCraftCostsDto;
import com.base.sbc.module.pack.entity.PackPricingCraftCosts;
import com.base.sbc.module.pack.mapper.PackPricingCraftCostsMapper;
import com.base.sbc.module.pack.service.PackPricingCraftCostsService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingCraftCostsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：资料包-二次加工费(工艺费用) service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingCraftCostsService
 * @email your email
 * @date 创建时间：2023-7-10 16:09:53
 */
@Service
public class PackPricingCraftCostsServiceImpl extends PackBaseServiceImpl<PackPricingCraftCostsMapper, PackPricingCraftCosts> implements PackPricingCraftCostsService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    String getModeName() {
        return "二次加工费";
    }

    @Override
    public PackPricingCraftCostsVo saveByDto(PackPricingCraftCostsDto dto) {
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PackPricingCraftCosts pageData = BeanUtil.copyProperties(dto, PackPricingCraftCosts.class);
            pageData.setId(null);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackPricingCraftCostsVo.class);
        }
        //修改
        else {
            PackPricingCraftCosts dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackPricingCraftCostsVo.class);
        }
    }

    @Override
    public PageInfo<PackPricingCraftCostsVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackPricingCraftCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        Page<PackPricingCraftCosts> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackPricingCraftCosts> pageInfo = page.toPageInfo();
        return CopyUtil.copy(pageInfo, PackPricingCraftCostsVo.class);
    }

    @Override
    public BigDecimal calculateCosts(PackCommonSearchDto dto) {
        BigDecimal result = BigDecimal.ZERO;
        QueryWrapper<PackPricingCraftCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        List<PackPricingCraftCosts> list = list(qw);
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        //二次加工费=单价*数量
        return list.stream().map(item -> NumberUtil.mul(
                Optional.ofNullable(item.getNum()).orElse(BigDecimal.ONE),
                item.getPrice())
        ).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);

    }


// 自定义方法区 不替换的区域【other_end】

}
