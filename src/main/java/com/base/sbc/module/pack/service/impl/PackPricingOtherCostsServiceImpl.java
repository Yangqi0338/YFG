/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.vo.TotalVo;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsDto;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.mapper.PackPricingOtherCostsMapper;
import com.base.sbc.module.pack.service.PackPricingOtherCostsService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包-核价信息-其他费用 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingOtherCostsService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:18
 */
@Service
public class PackPricingOtherCostsServiceImpl extends PackBaseServiceImpl<PackPricingOtherCostsMapper, PackPricingOtherCosts> implements PackPricingOtherCostsService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto) {
        QueryWrapper<PackPricingOtherCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getCostsItem()), "costs_item", dto.getCostsItem());
        Page<PackPricingOtherCosts> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackPricingOtherCosts> pageInfo = page.toPageInfo();
        return CopyUtil.copy(pageInfo, PackPricingOtherCostsVo.class);
    }


    @Override
    public PackPricingOtherCostsVo saveByDto(PackPricingOtherCostsDto dto) {
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PackPricingOtherCosts pageData = BeanUtil.copyProperties(dto, PackPricingOtherCosts.class);
            pageData.setId(null);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackPricingOtherCostsVo.class);
        }
        //修改
        else {
            PackPricingOtherCosts dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackPricingOtherCostsVo.class);
        }
    }

    @Override
    public Map<String, BigDecimal> statistics(PackCommonSearchDto dto) {
        Map<String, BigDecimal> result = new HashMap<>(16);
        QueryWrapper<PackPricingOtherCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        // 其他费用
        List<TotalVo> costsItemTotalList = getBaseMapper().costsItemTotal(qw);
        if (CollUtil.isNotEmpty(costsItemTotalList)) {
            for (TotalVo total : costsItemTotalList) {
                result.put(total.getLabel(), total.getTotal());
            }
        }
        //物料费用
        return result;
    }

    @Override
    String getModeName() {
        return "其他费用";
    }

// 自定义方法区 不替换的区域【other_end】

}
