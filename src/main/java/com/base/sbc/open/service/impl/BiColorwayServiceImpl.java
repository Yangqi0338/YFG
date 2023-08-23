package com.base.sbc.open.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackPricing;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.style.dto.QueryStyleColorDto;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.open.entity.BiColorway;
import com.base.sbc.open.mapper.BiColorwayMapper;
import com.base.sbc.open.service.BiColorwayService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/23 9:38:50
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiColorwayServiceImpl extends ServiceImpl<BiColorwayMapper, BiColorway> implements BiColorwayService {
    private final StyleColorService styleColorService;
    private final PackInfoService packInfoService;
    private final PackPricingService packPricingService;
    private final StylePricingService stylePricingService;
    /**
     *
     */
    @Override
    public void colorway() {
        List<BiColorway> list = new ArrayList<>();
        QueryStyleColorDto queryStyleColorDto = new QueryStyleColorDto();
        queryStyleColorDto.setPageNum(1);
        queryStyleColorDto.setPageSize(99999);
        PageInfo<StyleColorVo> sampleStyleColorList = styleColorService.getSampleStyleColorList(null, queryStyleColorDto);
        for (StyleColorVo styleColorVo : sampleStyleColorList.getList()) {
            BiColorway biColorway = new BiColorway();
            biColorway.setColorwayCode(styleColorVo.getStyleNo());
            biColorway.setColorSpecification(styleColorVo.getColorSpecification());
            biColorway.setColorCode(styleColorVo.getColorCode());
            biColorway.setColorName(styleColorVo.getColorName());
            biColorway.setC8ColorwayBand(styleColorVo.getBandName());
            biColorway.setC8ColorwaySalesPrice(styleColorVo.getTagPrice());
            biColorway.setC8ColorwaySaleTime(styleColorVo.getNewDate());
            biColorway.setC8ColorwayStyles(styleColorVo.getStyleFlavourName());
            biColorway.setPatternName(styleColorVo.getPatternDesignName());
            PackInfo packInfo = packInfoService.getOne(new QueryWrapper<PackInfo>().eq("code", styleColorVo.getBom()));
            if (packInfo != null) {
                // 核价
                PackPricing packPricing = packPricingService.getOne(new QueryWrapper<PackPricing>().eq("foreign_id", packInfo.getId()).eq("pack_type", "packBigGoods"));
                if (packPricing != null) {
                    JSONObject jsonObject = JSON.parseObject(packPricing.getCalcItemVal());
                    biColorway.setC8ColorwayTotalCosts(jsonObject.getBigDecimal("成本价")== null ? new BigDecimal(0) : jsonObject.getBigDecimal("成本价"));
                    biColorway.setC8ColorwayLaborCosts(jsonObject.getBigDecimal("车缝加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("车缝加工费"));
                    biColorway.setC8ColorwayMaterialCost(jsonObject.getBigDecimal("物料费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("物料费"));
                }
                //款式定价
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), packInfo.getCompanyCode());
                biColorway.setC8ColorwayMarckup(stylePricingVO.getActualMagnification());
                biColorway.setC8ColorwaySpPriceConfirm("1".equals(stylePricingVO.getProductTagPriceConfirm()));
                biColorway.setC8ColorwayJkCosts(stylePricingVO.getPlanCost());
                biColorway.setC8ColorwayMarckup4Pc(stylePricingVO.getPlanActualMagnification());
                biColorway.setC8ColorwaySeries(stylePricingVO.getSeries());



            }
            biColorway.setC8AppbomProductName(styleColorVo.getProductName());
            biColorway.setC8ColorwayWareCode(styleColorVo.getWareCode());


            biColorway.setC8ColorwayAccessories(styleColorVo.getIsTrim());

            list.add(biColorway);
        }

        this.remove(null);
        this.saveBatch(list);
    }
}
