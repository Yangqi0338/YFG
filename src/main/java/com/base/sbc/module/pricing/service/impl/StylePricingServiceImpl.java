/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsService;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：款式定价 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.StylePricingService
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
@Service
public class StylePricingServiceImpl extends BaseServiceImpl<StylePricingMapper, StylePricing> implements StylePricingService {
    private static final Logger logger = LoggerFactory.getLogger(StylePricingService.class);

    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private PackBomService packBomService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private StyleColorService styleColorService;

    @Override
    public PageInfo<StylePricingVO> getStylePricingList(StylePricingSearchDTO dto) {
        dto.setCompanyCode(super.getCompanyCode());
        com.github.pagehelper.Page<StylePricingVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<StylePricingVO> stylePricingList = super.getBaseMapper().getStylePricingList(dto);
        if (CollectionUtils.isEmpty(stylePricingList)) {
            return page.toPageInfo();
        }

        this.dataProcessing(stylePricingList, dto.getCompanyCode());
        return new PageInfo<>(stylePricingList);
    }

    /**
     * 数据组装处理
     *
     * @param stylePricingList
     * @param companyCode
     */
    private void dataProcessing(List<StylePricingVO> stylePricingList, String companyCode) {
        List<String> packId = stylePricingList.stream()
                .map(StylePricingVO::getId)
                .collect(Collectors.toList());
        Map<String, BigDecimal> otherCostsMap = this.getOtherCosts(packId, companyCode);
        Map<String, List<PackBomCalculateBaseVo>> packBomCalculateBaseVoS = this.getPackBomCalculateBaseVoS(packId);
        stylePricingList.forEach(stylePricingVO -> {
            List<PackBomCalculateBaseVo> packBomCalculateBaseVos = packBomCalculateBaseVoS.get(stylePricingVO.getId() + stylePricingVO.getPackType());
            stylePricingVO.setMaterialCost(this.getMaterialCost(packBomCalculateBaseVos));
            stylePricingVO.setTotalCost(this.getMaterialAmount(packBomCalculateBaseVos));
            stylePricingVO.setPackagingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "包装费")));
            stylePricingVO.setTestingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "检测费")));
            stylePricingVO.setSewingProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "车缝加工费")));
            stylePricingVO.setWoolenYarnProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "毛纱加工费")));
            stylePricingVO.setCoordinationProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协加工费")));
            stylePricingVO.setExpectedSalesPrice(this.getExpectedSalesPrice(stylePricingVO.getPlanningRatio(), stylePricingVO.getTotalCost()));
            stylePricingVO.setPlanCost(this.getPlanCost(packBomCalculateBaseVos));
            //计控实际倍率 = 吊牌价/计控实际成本
            stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getPlanCost(), 2));
            //实际倍率 = 吊牌价/总成本
            stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getTotalCost(), 2));
        });
        attachmentService.setListStylePic(stylePricingList, "sampleDesignPic");
    }

    @Override
    public StylePricingVO getByPackId(String packId, String companyCode) {
        if (StringUtils.isEmpty(packId)) {
            throw new RuntimeException("资料包id不可为空");
        }
        StylePricingSearchDTO stylePricingSearchDTO = new StylePricingSearchDTO();
        stylePricingSearchDTO.setPackId(packId);
        stylePricingSearchDTO.setCompanyCode(companyCode);
        List<StylePricingVO> stylePricingList = super.getBaseMapper().getStylePricingList(stylePricingSearchDTO);
        if (CollectionUtils.isEmpty(stylePricingList)) {
            return null;
        }
        this.dataProcessing(stylePricingList, companyCode);
        return stylePricingList.get(0);
    }

    @Override
    public void insertOrUpdate(StylePricingSaveDTO stylePricingSaveDTO, String companyCode) {
        logger.info("StylePricingService#insertOrUpdate 保存 stylePricingSaveDTO:{}, userCompany:{}", JSON.toJSONString(stylePricingSaveDTO), companyCode);
        StylePricing stylePricing = new StylePricing();
        if (StringUtils.isEmpty(stylePricingSaveDTO.getId())) {
            stylePricing.setId(new IdGen().nextIdStr());
            stylePricing.insertInit();
        } else {
            stylePricing.updateInit();
        }
        stylePricing.setCompanyCode(companyCode);
        BeanUtils.copyProperties(stylePricingSaveDTO, stylePricing);
        super.saveOrUpdate(stylePricing);

        StylePricing pricing = super.getById(stylePricing.getId());

        if (YesOrNoEnum.NO.getValueStr().equals(pricing.getProductHangtagConfirm()) || YesOrNoEnum.NO.getValueStr().equals(pricing.getControlHangtagConfirm())) {
            PackInfo packInfo = packInfoService.getById(stylePricingSaveDTO.getPackId());
            if (Objects.isNull(packInfo)) {
                return;
            }
            styleService.updateProductCost(packInfo.getForeignId(), stylePricingSaveDTO.getTargetCost());
            styleColorService.updateTagPrice(packInfo.getStyleColorId(), stylePricingSaveDTO.getTagPrice());
        }
    }

    @Override
    public void insertOrUpdateBatch(List<StylePricingSaveDTO> stylePricingSaves, String companyCode) {
        logger.info("StylePricingService#insertOrUpdateBatch 批量保存 stylePricingSaves:{}, userCompany:{}", JSON.toJSONString(stylePricingSaves), companyCode);
        List<StylePricing> stylePricings = stylePricingSaves.stream()
                .map(stylePricingSaveDTO -> {
                    StylePricing stylePricing = new StylePricing();
                    if (StringUtils.isEmpty(stylePricingSaveDTO.getId())) {
                        stylePricing.insertInit();
                    } else {
                        stylePricing.updateInit();
                    }
                    stylePricing.setCompanyCode(companyCode);
                    BeanUtils.copyProperties(stylePricingSaveDTO, stylePricing);
                    return stylePricing;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(stylePricings);
    }

    /**
     * 获取预计销售价 企划倍率*总成本（如果无，则直接是总成本）
     *
     * @param planningRatio
     * @param totalCost
     * @return
     */
    private BigDecimal getExpectedSalesPrice(BigDecimal planningRatio, BigDecimal totalCost) {
        if (Objects.isNull(planningRatio)) {
            return totalCost;
        }
        return BigDecimalUtil.mul(4, planningRatio, totalCost);
    }

    /**
     * 获取物料总金额
     *
     * @param packBomCalculateBaseVos
     * @return
     */
    private BigDecimal getMaterialAmount(List<PackBomCalculateBaseVo> packBomCalculateBaseVos) {
        if (CollectionUtils.isEmpty(packBomCalculateBaseVos)) {
            return BigDecimal.ZERO;
        }
        return packBomCalculateBaseVos.stream()
                .map(PackBomCalculateBaseVo::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

    }

    /**
     * 获取计控实际成本
     * 物料大货用量*物料大货单价*（1+损耗率) / (1+单价税点)
     *
     * @param list
     * @return
     */
    private BigDecimal getPlanCost(List<PackBomCalculateBaseVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(packBom -> {
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                    BigDecimal priceTax = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getPriceTax(), BigDecimal.valueOf(100), 2), 2);
                    BigDecimal cost = BigDecimalUtil.mul(2, packBom.getBulkUnitUse(), packBom.getBulkPrice(), lossRate);
                    return BigDecimalUtil.div(cost, priceTax, 2);
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 获取物料费用 物料大货用量*物料大货单价*（1+损耗率)
     *
     * @param list
     * @return
     */
    private BigDecimal getMaterialCost(List<PackBomCalculateBaseVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(packBom -> {
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                    return BigDecimalUtil.mul(2, packBom.getBulkUnitUse(), packBom.getBulkPrice(), lossRate);
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private Map<String, List<PackBomCalculateBaseVo>> getPackBomCalculateBaseVoS(List<String> packId) {
        List<PackBomCalculateBaseVo> packBomCalculateBaseVo = packBomService.getPackBomCalculateBaseVo(packId);
        if (CollectionUtils.isEmpty(packBomCalculateBaseVo)) {
            return new HashMap<>();
        }
        return packBomCalculateBaseVo.stream()
                .collect(Collectors.groupingBy(e -> e.getForeignId() + e.getPackType()));
    }


    /**
     * 获取其他费用
     *
     * @param packId
     * @param companyCode
     * @return
     */
    private Map<String, BigDecimal> getOtherCosts(List<String> packId, String companyCode) {
        List<PackPricingOtherCosts> packPricingOtherCosts = packPricingOtherCostsService.getPriceSumByForeignIds(packId, companyCode);
        if (CollectionUtils.isEmpty(packPricingOtherCosts)) {
            return new HashMap<>();
        }
        return packPricingOtherCosts.stream()
                .collect(Collectors.toMap(e -> e.getForeignId() + e.getCostsItem(), PackPricingOtherCosts::getPrice, (k1, k2) -> k1));

    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
