/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
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
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class PackPricingOtherCostsServiceImpl extends AbstractPackBaseServiceImpl<PackPricingOtherCostsMapper, PackPricingOtherCosts> implements PackPricingOtherCostsService {


    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private  PackPricingService packPricingService;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto) {
        QueryWrapper<PackPricingOtherCosts> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getCostsItem()), "costs_item", dto.getCostsItem());
        Page<PackPricingOtherCosts> page = PageHelper.startPage(dto);
        qw.eq(StringUtils.isNotEmpty(dto.getColorCode()), "color_code", dto.getColorCode());
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
            saveOrUpdateOperaLog(dto, dbData, genOperaLogEntity(dbData, "修改"));
            BeanUtil.copyProperties(dto, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackPricingOtherCostsVo.class);
        }
    }

    /**
     * 批量保存他费用
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean batchOtherCosts(List<PackPricingOtherCostsDto> dto) {
        List<PackPricingOtherCosts> list =BeanUtil.copyToList(dto,PackPricingOtherCosts.class);
        saveOrUpdateBatch(list);
        /*重新计算*/
        packPricingService.calculatePricingJson(dto.get(0).getForeignId(),dto.get(0).getPackType());
        return true;
    }

    @Override
    public Map<String, BigDecimal> statistics(PackCommonSearchDto dto) {
        Map<String, BigDecimal> result = new HashMap<>(16);
        QueryWrapper<PackPricingOtherCosts> qw = new QueryWrapper<>();
        QueryWrapper<PackPricingOtherCosts> qw1 = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        PackUtils.commonQw(qw1, dto);
        // 其他费用
        qw.eq(StringUtils.isNotEmpty(dto.getColorCode()), "color_code", dto.getColorCode());
        qw1.eq(StringUtils.isNotEmpty(dto.getColorCode()), "color_code", dto.getColorCode());
        qw.ne("costs_item","其它费");
        qw1.eq("costs_item","其它费");
        List<TotalVo> costsItemTotalList = getBaseMapper().newCostsItemTotal(qw,qw1);
        if (CollUtil.isNotEmpty(costsItemTotalList)) {
            costsItemTotalList =  costsItemTotalList.stream().filter(c -> !ObjectUtil.isEmpty(c)&& StrUtil.isNotBlank(c.getLabel())).collect(Collectors.toList());
            for (TotalVo total : costsItemTotalList) {
                if(StrUtil.isNotBlank(total.getLabel()) ){
                    result.put(total.getLabel(), total.getTotal());
                }
            }
        }
        //物料费用
        return result;
    }

    @Override
    public List<PackPricingOtherCosts> getPriceSumByForeignIds(List<String> foreignIds, String companyCode, String packType) {
        if (CollectionUtils.isEmpty(foreignIds)) {
            return Lists.newArrayList();
        }
        return super.getBaseMapper().getPriceSumByForeignIds(foreignIds, companyCode,packType);
    }

    /**
     * 生成费用明细单
     *
     * @param dict      字典编码 多个用,分割
     * @param foreignId 父级id
     * @param packType
     * @return
     */
    @Override
    public boolean createCostDetail(String dict, String foreignId, String packType) {
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap(dict);
        List<PackPricingOtherCosts> list = new ArrayList<>();
        /**/
        if (CollUtil.isNotEmpty(dictInfoToMap)) {

            for (Map.Entry<String, Map<String, String>> outerEntry : dictInfoToMap.entrySet()) {
                Map<String, String> innerMap = outerEntry.getValue();
                for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
                    PackPricingOtherCosts packPricingOtherCosts = new PackPricingOtherCosts();
                    packPricingOtherCosts.setPackType(packType);
                    packPricingOtherCosts.setForeignId(foreignId);
                    packPricingOtherCosts.setCostsItem( "costOtherPrice".equals(outerEntry.getKey())?"其他费":"外协加工费"  );
                    packPricingOtherCosts.setCostsType(innerEntry.getValue());
                    packPricingOtherCosts.setCostsTypeId(innerEntry.getKey());
                    packPricingOtherCosts.setPrice(BigDecimal.ZERO);
                    list.add(packPricingOtherCosts);
                }
            }
        }
        if(CollUtil.isNotEmpty(list)){
            saveOrUpdateBatch(list);
        }
        return true;
    }

    /**
     * 其他费用外协加工费-删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean delOtherCosts(String id) {
        PackPricingOtherCosts packPricingOtherCosts = baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(packPricingOtherCosts)){
            throw new OtherException("id错误");
        }
        baseMapper.deleteById(packPricingOtherCosts.getId());
        packPricingService.calculatePricingJson(packPricingOtherCosts.getForeignId(),packPricingOtherCosts.getPackType());
        return true;
    }


    @Override
    String getModeName() {
        return "其他费用";
    }

// 自定义方法区 不替换的区域【other_end】

}
