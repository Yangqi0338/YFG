/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackPricingCraftCosts;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.entity.PackPricingProcessCosts;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
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
@RequiredArgsConstructor
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
    @Lazy
    private StyleColorService styleColorService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    @Lazy
    private SmpService smpService;

    private final PackPricingCraftCostsService packPricingCraftCostsService;
    private final PackPricingProcessCostsService packPricingProcessCostsService;

    // @Resource
    // @Lazy
    // private final SmpService smpService;

    @Override
    public PageInfo<StylePricingVO> getStylePricingList(Principal user, StylePricingSearchDTO dto) {
        dto.setCompanyCode(super.getCompanyCode());
        // 关闭自动count,数据太多了,cpu差
        com.github.pagehelper.Page<StylePricingVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), false);
        BaseQueryWrapper qw = new BaseQueryWrapper();
        qw.notEmptyEq("sd.year", dto.getYear());
        qw.notEmptyEq("sd.season", dto.getSeason());
        qw.notEmptyEq("sd.month", dto.getMonth());
        qw.notEmptyEq("ssc.tag_price", dto.getTagPrice());
        qw.likeList(StrUtil.isNotBlank(dto.getStyleNo()),"ssc.style_no", com.base.sbc.config.utils.StringUtils.convertList(dto.getStyleNo()));
        qw.likeList(StrUtil.isNotBlank(dto.getDesignNo()),"sd.design_no", com.base.sbc.config.utils.StringUtils.convertList(dto.getDesignNo()));
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_pricing.getK(), "sd.");
        List<StylePricingVO> stylePricingList = super.getBaseMapper().getStylePricingList(dto, qw);
        if (CollectionUtils.isEmpty(stylePricingList)) {
            return page.toPageInfo();
        }

        if(StrUtil.equals(dto.getDeriveFlag(),BaseGlobal.YES) ){
            if(StrUtil.equals(dto.getImgFlag(),BaseGlobal.YES) ){
                if(stylePricingList.size() >1000){
                    throw new OtherException("带图片最多只能导出1000条");
                }else {
                    stylePicUtils.setStyleColorPic2(stylePricingList, "styleColorPic", 30);
                }
            }else {
                if(stylePricingList.size() >2000){
                    throw new OtherException("不带图片最多只能导出2000条");
                }
            }
        }else {
            stylePicUtils.setStyleColorPic2(stylePricingList, "styleColorPic");
        }
        this.dataProcessing(stylePricingList, dto.getCompanyCode(),true);
        return new PageInfo<>(stylePricingList);
    }

    /**
     * 数据组装处理
     *
     * @param stylePricingList
     * @param companyCode
     */
    public void dataProcessing(List<StylePricingVO> stylePricingList, String companyCode,boolean isPackType) {
        List<String> packId = stylePricingList.stream()
                .map(StylePricingVO::getId)
                .collect(Collectors.toList());
        String packType="";
        if (isPackType){
            packType ="packBigGoods";
        }
        Map<String, BigDecimal> otherCostsMap = this.getOtherCosts(packId, companyCode,packType);
//        Map<String, List<PackBomCalculateBaseVo>> packBomCalculateBaseVoS = this.getPackBomCalculateBaseVoS(packId);
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .build();

        try {
        CountDownLatch countDownLatch = new CountDownLatch(stylePricingList.size());
        for (StylePricingVO stylePricingVO : stylePricingList) {
            executor.submit(() -> {
                // List<PackBomCalculateBaseVo> packBomCalculateBaseVos = packBomCalculateBaseVoS.get(stylePricingVO.getId() + stylePricingVO.getPackType());
                PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
                if (isPackType){
                    packCommonSearchDto.setPackType(PackUtils.PACK_TYPE_BIG_GOODS);
                }else {
                    packCommonSearchDto.setPackType(stylePricingVO.getPackType());
                }

                packCommonSearchDto.setForeignId(stylePricingVO.getId());
                //材料成本,如果fob,则不计算
                if ("CMT".equals(stylePricingVO.getProductionType())) {
                    stylePricingVO.setMaterialCost(packBomService.calculateCosts(packCommonSearchDto));
                } else {
                    stylePricingVO.setMaterialCost(BigDecimal.ZERO);
                }
                stylePricingVO.setPackagingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "包装费")));
                stylePricingVO.setTestingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "检测费")));
                stylePricingVO.setSewingProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "车缝加工费")));
                stylePricingVO.setWoolenYarnProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "毛纱加工费")));
                BigDecimal coordinationProcessingFee = new BigDecimal(0);
                coordinationProcessingFee = coordinationProcessingFee.add(
                                BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协其他"))).
                        add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协印花"))).
                        add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协绣花"))).
                        add(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协压皱")));

                stylePricingVO.setCoordinationProcessingFee(coordinationProcessingFee);

                //加工费
                List<PackPricingProcessCosts> processCostsList = packPricingProcessCostsService.list(new QueryWrapper<PackPricingProcessCosts>().eq("foreign_id", stylePricingVO.getId()).eq("pack_type", "packBigGoods"));
                if (!processCostsList.isEmpty()) {
                    try {
                        processCostsList.stream()
                                .map(costs -> costs.getProcessPrice().multiply(costs.getMultiple()))
                                .reduce(BigDecimal::add)
                                .ifPresent(stylePricingVO::setProcessingFee);
                    } catch (Exception e) {
                        logger.error("StylePricingServiceImpl#dataProcessing 加工费计算异常", e);
                    }

                }
                //二次加工费用
                List<PackPricingCraftCosts> pricingCraftCostsList = packPricingCraftCostsService.list(new QueryWrapper<PackPricingCraftCosts>().eq("foreign_id", stylePricingVO.getId()).eq("pack_type", "packBigGoods"));
                if (!pricingCraftCostsList.isEmpty()) {
                    try {
                        pricingCraftCostsList.stream()
                                .map(costs -> costs.getPrice().multiply(costs.getNum()))
                                .reduce(BigDecimal::add)
                                .ifPresent(stylePricingVO::setSecondaryProcessingFee);
                    } catch (Exception e) {
                        logger.error("StylePricingServiceImpl#dataProcessing 二次加工费用计算异常", e);
                    }
                }


                stylePricingVO.setTotalCost(BigDecimalUtil.add(stylePricingVO.getMaterialCost(), stylePricingVO.getPackagingFee(),
                        stylePricingVO.getTestingFee(), stylePricingVO.getSewingProcessingFee(), stylePricingVO.getWoolenYarnProcessingFee(),
                        stylePricingVO.getCoordinationProcessingFee(), stylePricingVO.getSecondaryProcessingFee(), stylePricingVO.getProcessingFee()));
                stylePricingVO.setTotalCost(stylePricingVO.getTotalCost().setScale(3, RoundingMode.HALF_UP));
                BigDecimal taxRate = BigDecimal.ONE;
                if ("CMT".equals(stylePricingVO.getProductionType())) {

                    System.out.println(stylePricingVO.getCalcItemVal());
                    JSONObject jsonObject = JSON.parseObject(stylePricingVO.getCalcItemVal());
                    if (jsonObject != null) {
                        taxRate = jsonObject.getBigDecimal("税率");

                    }
                    if (stylePricingVO.getTotalCost() != null && taxRate != null) {
                        stylePricingVO.setTotalCost(stylePricingVO.getTotalCost().multiply(taxRate).setScale(3, RoundingMode.HALF_UP));
                    }
                }


                stylePricingVO.setExpectedSalesPrice(this.getExpectedSalesPrice(stylePricingVO.getPlanningRatio(), stylePricingVO.getTotalCost()));
                // stylePricingVO.setPlanCost(this.getPlanCost(packBomCalculateBaseVos));
               /*优先展示手数的数据*/
                if(stylePricingVO.getControlPlanCost() != null){
                    stylePricingVO.setPlanCost((stylePricingVO.getControlPlanCost()));
                }else {
                //目前逻辑修改为取计控实际成本取总成本
                stylePricingVO.setPlanCost(stylePricingVO.getTotalCost());
                }
                //计控实际倍率 = 吊牌价/计控实际成本
                stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getPlanCost(), 2));
                //实际倍率 = 吊牌价/总成本
                stylePricingVO.setActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getTotalCost(), 2));
                //每次减一
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
        stylePicUtils.setStylePic(stylePricingList, "sampleDesignPic");
    }

    @Override
    public StylePricingVO getByPackId(String packId, String companyCode) {
        if (StringUtils.isEmpty(packId)) {
            throw new RuntimeException("资料包id不可为空");
        }
        StylePricingSearchDTO stylePricingSearchDTO = new StylePricingSearchDTO();
        stylePricingSearchDTO.setPackId(packId);
        stylePricingSearchDTO.setCompanyCode(companyCode);
        List<StylePricingVO> stylePricingList = super.getBaseMapper().getStylePricingList(stylePricingSearchDTO, new QueryWrapper<>());
        if (CollectionUtils.isEmpty(stylePricingList)) {
            return null;
        }
        this.dataProcessing(stylePricingList, companyCode,true);
        return stylePricingList.get(0);
    }

    @Transactional
    @Override
    public void insertOrUpdate(StylePricingSaveDTO stylePricingSaveDTO, String companyCode) {
        logger.info("StylePricingService#insertOrUpdate 保存 stylePricingSaveDTO:{}, userCompany:{}", JSON.toJSONString(stylePricingSaveDTO), companyCode);
        StylePricing stylePricing = new StylePricing();
        if (StringUtils.isEmpty(stylePricingSaveDTO.getId())) {

            if ("1".equals(stylePricingSaveDTO.getControlConfirm())){
                stylePricing.setControlConfirmTime(new Date());
            }
        } else {
            if ("1".equals(stylePricingSaveDTO.getControlConfirm())){
                StylePricing stylePricing1 = this.getById(stylePricingSaveDTO.getId());
                if (!stylePricingSaveDTO.getControlConfirm().equals(stylePricing1.getControlConfirm())){
                    stylePricing.setControlConfirmTime(new Date());
                }
                stylePricing.setControlConfirmTime(new Date());
            }

        }

        stylePricing.setCompanyCode(companyCode);

        BeanUtil.copyProperties(stylePricingSaveDTO, stylePricing);
        super.saveOrUpdate(stylePricing);

        StylePricing pricing = super.getById(stylePricing.getId());
        PackInfo packInfo = packInfoService.getById(stylePricingSaveDTO.getPackId());

        if (packInfo != null && StrUtil.isNotBlank(packInfo.getStyleColorId())) {
            styleColorService.updateProductStyle(packInfo.getStyleColorId(), stylePricing.getProductStyle(), stylePricing.getProductStyleName());

        }
        if (YesOrNoEnum.NO.getValueStr().equals(pricing.getProductHangtagConfirm()) || YesOrNoEnum.NO.getValueStr().equals(pricing.getControlHangtagConfirm())) {

            if (Objects.isNull(packInfo)) {
                return;
            }

            styleService.updateProductCost(packInfo.getForeignId(), stylePricingSaveDTO.getTargetCost());

            StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", packInfo.getStyleNo()));
            styleColor.setTagPrice(stylePricingSaveDTO.getTagPrice());
            styleColorService.updateById(styleColor);
            // smpService.goods(new String[]{styleColor.getId()});
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
                        if ("1".equals(stylePricingSaveDTO.getControlConfirm())){
                            stylePricing.setControlConfirmTime(new Date());
                        }
                    } else {
                        StylePricing stylePricing1 = this.getById(stylePricingSaveDTO.getId());
                        if ("1".equals(stylePricingSaveDTO.getControlConfirm())){

                            if (!stylePricingSaveDTO.getControlConfirm().equals(stylePricing1.getControlConfirm())){
                                stylePricing.setControlConfirmTime(new Date());
                            }
                            stylePricing.setControlConfirmTime(new Date());
                        }
                        /*取消计控确定成本*/
                        if (StrUtil.equals(stylePricingSaveDTO.getControlConfirm(), BaseGlobal.IN)) {
                            /*校验商品吊牌和计控吊牌确定*/
                            if (StrUtil.equals(stylePricing1.getControlHangtagConfirm(), BaseGlobal.YES) ||
                                    StrUtil.equals(stylePricing1.getProductHangtagConfirm(), BaseGlobal.YES)
                            ) {
                                throw new OtherException("商品吊牌和计控吊牌未取消");
                            }
                        }
                        if (StrUtil.equals(stylePricingSaveDTO.getProductHangtagConfirm(), BaseGlobal.IN)) {
                            /*校验商品吊牌和计控吊牌确定*/
                            if (StrUtil.equals(stylePricing1.getControlHangtagConfirm(), BaseGlobal.YES)) {
                                throw new OtherException("计控吊牌未取消");
                            }
                        }
                        stylePricing.updateInit();
                    }


                    //region 2023-12-06 款式定价3个按钮反审核下发到scm
                    if (null != stylePricingSaveDTO.getControlConfirm() && "0".equals(stylePricingSaveDTO.getControlConfirm())) {
                        //是否计控确认
                        smpService.tagConfirmDates(Collections.singletonList(stylePricingSaveDTO.getId()), HangTagDeliverySCMStatusEnum.PLAN_COST_CONFIRM, 0);
                    } else if (null != stylePricingSaveDTO.getProductHangtagConfirm() && "0".equals(stylePricingSaveDTO.getProductHangtagConfirm())) {
                        //是否商品吊牌确认
                        smpService.tagConfirmDates(Collections.singletonList(stylePricingSaveDTO.getId()), HangTagDeliverySCMStatusEnum.PRODUCT_TAG_PRICE_CONFIRM, 0);
                    } else if (null != stylePricingSaveDTO.getControlHangtagConfirm() && "0".equals(stylePricingSaveDTO.getControlHangtagConfirm())) {
                        //是否计控吊牌确认
                        smpService.tagConfirmDates(Collections.singletonList(stylePricingSaveDTO.getId()), HangTagDeliverySCMStatusEnum.PLAN_TAG_PRICE_CONFIRM, 0);
                    }
                    //endregion

                    stylePricing.setCompanyCode(companyCode);
                    BeanUtils.copyProperties(stylePricingSaveDTO, stylePricing);
                    return stylePricing;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(stylePricings);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void unAuditStatus(List<String> ids) {
        LambdaUpdateWrapper<StylePricing> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StylePricing::getWagesConfirm, "0");
        updateWrapper.set(StylePricing::getControlConfirm, "0");
        updateWrapper.set(StylePricing::getControlHangtagConfirm, "0");
        updateWrapper.set(StylePricing::getProductHangtagConfirm, "0");
        updateWrapper.set(StylePricing::getWagesConfirmTime, null);
        updateWrapper.set(StylePricing::getControlConfirmTime, null);
        updateWrapper.set(StylePricing::getControlHangtagConfirmTime, null);
        updateWrapper.set(StylePricing::getProductHangtagConfirmTime, null);

        updateWrapper.set(StylePricing::getUpdateId, getUserId());
        updateWrapper.set(StylePricing::getUpdateName, getUserName());
        updateWrapper.set(StylePricing::getUpdateDate, new Date());

        updateWrapper.in(StylePricing::getId, ids);

        update(updateWrapper);
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
        return BigDecimalUtil.mul(3, planningRatio, totalCost);
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
                    BigDecimal rate;
                    BigDecimal unitUse;
                    if ("packDesign".equals(packBom.getPackType())){
                        rate=packBom.getLossRate();
                        unitUse = packBom.getDesignUnitUse();
                    }else {
                        rate=packBom.getPlanningLoossRate();
                        unitUse = packBom.getBulkUnitUse();
                    }
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(rate, BigDecimal.valueOf(100), 2), 2);
                    BigDecimal priceTax = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getPriceTax(), BigDecimal.valueOf(100), 2), 2);
                    BigDecimal cost = BigDecimalUtil.mul(2, unitUse, packBom.getPrice(), lossRate);
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
                    return BigDecimalUtil.mul(2, packBom.getBulkUnitUse(), packBom.getPrice(), lossRate);
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
    private Map<String, BigDecimal> getOtherCosts(List<String> packId, String companyCode, String packType) {
        List<PackPricingOtherCosts> packPricingOtherCosts = packPricingOtherCostsService.getPriceSumByForeignIds(packId, companyCode,packType);
        if (CollectionUtils.isEmpty(packPricingOtherCosts)) {
            return new HashMap<>();
        }
        return packPricingOtherCosts.stream()
                .filter(x -> Objects.nonNull(x.getPrice()))
                .collect(Collectors.toMap(e -> e.getForeignId() + e.getCostsType(), PackPricingOtherCosts::getPrice,(k1, k2) -> k1));

    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
