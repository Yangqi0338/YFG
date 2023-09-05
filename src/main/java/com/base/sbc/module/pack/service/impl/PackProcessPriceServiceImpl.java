/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackProcessPriceDto;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pack.mapper.PackProcessPriceMapper;
import com.base.sbc.module.pack.service.PackProcessPriceService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.base.sbc.module.pricing.vo.PricingProcessCostsVO;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：资料包-工序工价 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackProcessPriceService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 14:12:07
 */
@Service
public class PackProcessPriceServiceImpl extends PackBaseServiceImpl<PackProcessPriceMapper, PackProcessPrice> implements PackProcessPriceService {

// 自定义方法区 不替换的区域【other_start】


    @Override
    public PageInfo<PackProcessPriceVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackProcessPrice> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        if (StrUtil.isBlank(dto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        Page<PackProcessPrice> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackProcessPrice> pageInfo = page.toPageInfo();
        PageInfo<PackProcessPriceVo> voPageInfo = CopyUtil.copy(pageInfo, PackProcessPriceVo.class);
        return voPageInfo;
    }

    @Override
    public PackProcessPriceVo getDetail(String id) {
        PackProcessPrice byId = getById(id);
        return BeanUtil.copyProperties(byId, PackProcessPriceVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackProcessPriceVo saveByDto(PackProcessPriceDto dto) {
        PackProcessPrice pageData = BeanUtil.copyProperties(dto, PackProcessPrice.class);
        if (CommonUtils.isInitId(pageData.getId())) {
            pageData.setId(null);
            pageData.setEndNode(Opt.ofBlankAble(pageData.getEndNode()).orElse(BaseGlobal.NO));
            save(pageData);
            dto.setId(pageData.getId());
        } else {
            PackProcessPrice dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
        }
        return getDetail(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatchByDto(PackCommonSearchDto commonDto, List<PackProcessPriceDto> dtoList) {
        List<PackProcessPrice> dataList = BeanUtil.copyToList(dtoList, PackProcessPrice.class);
        if (CollUtil.isNotEmpty(dataList)) {
            for (PackProcessPrice entity : dataList) {
                entity.setForeignId(commonDto.getForeignId());
                entity.setPackType(commonDto.getPackType());
            }
        }
        QueryWrapper<PackProcessPrice> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, commonDto);
        addAndUpdateAndDelList(dataList, qw, false);
        return true;
    }

    @Override
    public List<PricingProcessCostsVO> getPricingProcessCostsByForeignId(String foreignId) {
        return super.getBaseMapper().getPricingProcessCostsByForeignId(foreignId);
    }

    @Override
    public List<PackProcessPrice> getListByForeignId(String foreignId, String packType) {
        if (StringUtils.isEmpty(foreignId)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PackProcessPrice> qw = new QueryWrapper<PackProcessPrice>().lambda()
                .eq(PackProcessPrice::getForeignId, foreignId)
                .eq(PackProcessPrice::getPackType, packType);

        return super.list(qw);
    }

    @Override
    String getModeName() {
        return "工序工价";
    }

// 自定义方法区 不替换的区域【other_end】

}
