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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingProcessCostsDto;
import com.base.sbc.module.pack.entity.PackPricingProcessCosts;
import com.base.sbc.module.pack.mapper.PackPricingProcessCostsMapper;
import com.base.sbc.module.pack.service.PackPricingProcessCostsService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingProcessCostsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-加工费 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingProcessCostsService
 * @email your email
 * @date 创建时间：2023-7-10 16:09:56
 */
@Service
public class PackPricingProcessCostsServiceImpl extends PackBaseServiceImpl<PackPricingProcessCostsMapper, PackPricingProcessCosts> implements PackPricingProcessCostsService {


// 自定义方法区 不替换的区域【other_start】
    @Autowired
    private CcmFeignService ccmFeignService;


    @Override
    public PackPricingProcessCostsVo saveByDto(PackPricingProcessCostsDto dto) {
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PackPricingProcessCosts pageData = BeanUtil.copyProperties(dto, PackPricingProcessCosts.class);
            pageData.setId(null);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackPricingProcessCostsVo.class);
        }
        //修改
        else {
            PackPricingProcessCosts dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackPricingProcessCostsVo.class);
        }
    }

    @Override
    public PageInfo<PackPricingProcessCostsVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackPricingProcessCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        Page<PackPricingProcessCosts> page = PageHelper.startPage(dto);
        qw.eq(StringUtils.isNotEmpty(dto.getColorCode()), "color_code", dto.getColorCode());
        list(qw);
        PageInfo<PackPricingProcessCosts> pageInfo = page.toPageInfo();
        return CopyUtil.copy(pageInfo, PackPricingProcessCostsVo.class);
    }

    @Override
    public BigDecimal calculateCosts(PackCommonSearchDto dto) {
        BigDecimal result = BigDecimal.ZERO;
        QueryWrapper<PackPricingProcessCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StringUtils.isNotEmpty(dto.getColorCode()), "color_code", dto.getColorCode());
        List<PackPricingProcessCosts> list = list(qw);
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        //加工费=单价*倍数
        return list.stream().map(item -> NumberUtil.mul(
                Optional.ofNullable(item.getMultiple()).orElse(BigDecimal.ONE),
                item.getProcessPrice())
        ).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
    }

    @Override
    public void batchInsertProcessCosts(List<PackPricingProcessCostsDto> dto) {
        List<String> sourceIds = dto.stream()
                .map(PackPricingProcessCostsDto::getSourceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        Map<String, String> idMap = this.getIdBySourceIds(sourceIds, dto.get(0).getForeignId());
        List<PackPricingProcessCosts> packPricingProcessCostsList = dto.stream()
                .map(e -> {
                    PackPricingProcessCosts packPricingProcessCosts = CopyUtil.copy(e, PackPricingProcessCosts.class);
                    String id = idMap.get(packPricingProcessCosts.getSourceId());
                    packPricingProcessCosts.setCompanyCode(super.getCompanyCode());
                    packPricingProcessCosts.setSourceType("1");
                    if (StringUtils.isEmpty(id)) {
                        packPricingProcessCosts.insertInit();
                    } else {
                        packPricingProcessCosts.setId(id);
                        packPricingProcessCosts.updateInit();
                    }
                    return packPricingProcessCosts;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(packPricingProcessCostsList);
    }

    @NotNull
    private Map<String, String> getIdBySourceIds(List<String> sourceIds, String foreignId) {
        if (CollectionUtils.isEmpty(sourceIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<PackPricingProcessCosts> qw = new QueryWrapper<PackPricingProcessCosts>().lambda()
                .eq(PackPricingProcessCosts::getForeignId, foreignId)
                .eq(PackPricingProcessCosts::getDelFlag, "0")
                .in(PackPricingProcessCosts::getSourceId, sourceIds)
                .select(PackPricingProcessCosts::getId, PackPricingProcessCosts::getSourceId);
        List<PackPricingProcessCosts> list = super.list(qw);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.toMap(PackPricingProcessCosts::getSourceId, PackPricingProcessCosts::getId, (k1, k2) -> k2));
    }


    @Override
    String getModeName() {
        return "加工费";
    }


// 自定义方法区 不替换的区域【other_end】

}
