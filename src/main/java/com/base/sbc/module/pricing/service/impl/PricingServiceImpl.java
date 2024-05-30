/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.annotation.EditPermission;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackProcessPriceService;
import com.base.sbc.module.pricing.dto.PricingCountDTO;
import com.base.sbc.module.pricing.dto.PricingDTO;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.dto.PricingSearchDTO;
import com.base.sbc.module.pricing.entity.Pricing;
import com.base.sbc.module.pricing.entity.PricingTemplate;
import com.base.sbc.module.pricing.enums.PricingCountTypeEnum;
import com.base.sbc.module.pricing.enums.PricingQueryDimensionEnum;
import com.base.sbc.module.pricing.enums.PricingSourceTypeEnum;
import com.base.sbc.module.pricing.mapper.PricingMapper;
import com.base.sbc.module.pricing.service.*;
import com.base.sbc.module.pricing.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：核价表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@Service
public class PricingServiceImpl extends BaseServiceImpl<PricingMapper, Pricing> implements PricingService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(PricingService.class);
    @Autowired
    private PricingProcessCostsService pricingProcessCostsService;
    @Autowired
    private PricingColorService pricingColorService;
    @Autowired
    private PricingCraftCostsService pricingCraftCostsService;
    @Autowired
    private PricingMaterialCostsService pricingMaterialCostsService;
    @Autowired
    private PricingOtherCostsService pricingOtherCostsService;
    @Autowired
    private PricingMapper pricingMapper;
    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PackBomService packBomService;
    @Autowired
    private PackProcessPriceService packProcessPriceService;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    @EditPermission(type = DataPermissionsBusinessTypeEnum.costPricing)
    public PageInfo<PricingListVO> queryPageInfo(PricingSearchDTO dto, String userCompany) {
        BaseQueryWrapper<PricingTemplate> qw = new BaseQueryWrapper<>();
        QueryGenerator.initQueryWrapperByMap(qw,dto);
        dto.setCompanyCode(userCompany);
        com.github.pagehelper.Page<PricingListVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        if (PricingQueryDimensionEnum.ITEM.getK().equals(dto.getDimension())) {
            pricingMapper.getItemDimension(qw, dto);
            return page.toPageInfo();
        }
        pricingMapper.getSummaryDimension(qw, dto);
        return page.toPageInfo();
    }

    @Transactional
    @Override
    public String save(PricingDTO pricingDTO, String userCompany) {
        logger.info("PricingService#save 保存 pricingDTO:{}, userCompany:{}", JSON.toJSONString(pricingDTO), userCompany);
        String pricingId = pricingDTO.getId();
        String pricingCode = pricingDTO.getCode();
        if (StringUtils.isEmpty(pricingDTO.getId())) {
            Pricing pricing = new Pricing();
            pricing.insertInit();
            pricing.setCompanyCode(userCompany);
            BeanUtils.copyProperties(pricingDTO, pricing);
            super.save(pricing);
            pricingId = pricing.getId();
            pricingCode = pricing.getCode();
        } else {
            this.checkIsExistAuditing(pricingId, null, userCompany);
            Pricing pricing = new Pricing();
            pricing.updateInit();
            BeanUtils.copyProperties(pricingDTO, pricing);
            LambdaUpdateWrapper<Pricing> wrapper = new UpdateWrapper<Pricing>().lambda()
                    .eq(Pricing::getId, pricingId)
                    .eq(Pricing::getCompanyCode, userCompany);
            super.update(pricing, wrapper);
        }
        Map<String, String> pricingColorIdMap = pricingColorService.insert(pricingDTO.getPricingColors(), pricingCode, userCompany);
        pricingCraftCostsService.insert(pricingDTO.getPricingCraftCosts(), pricingCode, userCompany);
        pricingMaterialCostsService.insert(pricingDTO.getPricingMaterialCosts(), pricingCode, userCompany);
        pricingProcessCostsService.insert(pricingDTO.getPricingProcessCosts(), pricingCode, userCompany);
        pricingOtherCostsService.insert(pricingDTO.getPricingOtherCosts(), pricingCode, userCompany);
        // TODO 发起审批流程
        return pricingId;
    }

    @Override
    public void delByIds(PricingDelDTO pricingDelDTO, String userCompany) {
        logger.info("PricingService#delById 删除 pricingDelDTO:{}, userCompany:{}", JSON.toJSONString(pricingDelDTO), userCompany);
        if (CollectionUtils.isEmpty(pricingDelDTO.getIds())) {
            throw new BusinessException(BaseErrorEnum.ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        this.checkIsExistAuditing(null, pricingDelDTO.getIds(), userCompany);
        pricingMapper.deleteBatchIds(pricingDelDTO.getIds());
    }


    @Override
    public PricingVO getDetailsById(String id, String userCompany) {
        LambdaQueryWrapper<Pricing> queryWrapper = new QueryWrapper<Pricing>().lambda()
                .eq(Pricing::getId, id)
                .eq(Pricing::getCompanyCode, userCompany)
                .eq(Pricing::getDelFlag, YesOrNoEnum.NO.getValueStr());
        Pricing pricing = super.getOne(queryWrapper);
        if (Objects.isNull(pricing)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        PricingVO pricingVO = new PricingVO();
        BeanUtils.copyProperties(pricing, pricingVO);
        String pricingCode = pricingVO.getCode();
        pricingVO.setPricingProcessCosts(pricingProcessCostsService.getByPricingCode(pricingCode, userCompany));
        pricingVO.setPricingColors(pricingColorService.getByPricingCode(pricingCode, userCompany));
        pricingVO.setPricingCraftCosts(pricingCraftCostsService.getByPricingCode(pricingCode, userCompany));
        pricingVO.setPricingMaterialCosts(pricingMaterialCostsService.getByPricingCode(pricingCode, userCompany));
        pricingVO.setPricingOtherCosts(pricingOtherCostsService.getByPricingCode(pricingCode, userCompany));
        return pricingVO;
    }

    @Override
    public PricingVO getPlateMakingPricing(String id, String userCompany) {
        PricingVO pricingVO = packInfoService.getPricingVoById(id);
// COde
        pricingVO.setCode("HJ" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));

        pricingVO.setSourceType(PricingSourceTypeEnum.PLATE_MAKING.getK());
        pricingVO.setStylePic(this.getStylePic(id));
        Map<String, List<PricingMaterialCostsVO>> pricingMaterialCostsMap = this.getPricingMaterialCostsMap(id, pricingVO.getColorCode());

        pricingVO.setPricingMaterialCosts(pricingMaterialCostsMap);

        BigDecimal totalPrice = new BigDecimal(0);
        List<PricingMaterialCostsVO> pricingMaterialCostsVOS = pricingMaterialCostsMap.get(pricingVO.getColorCode());
        if (CollectionUtils.isNotEmpty(pricingMaterialCostsVOS)) {
            pricingMaterialCostsVOS.forEach(e -> totalPrice.add(e.getTotalPrice()));
        }
        ArrayList<PricingColorVO> pricingColors = Lists.newArrayList();

        if (StringUtils.isNotEmpty(pricingVO.getColorCode())) {
            pricingVO.setPricingMaterialCosts(pricingMaterialCostsMap);
            PricingColorVO pricingColorVO = new PricingColorVO();
            pricingColorVO.setColor(pricingVO.getColor());
            pricingColorVO.setColorCode(pricingVO.getColorCode());
            pricingColorVO.setMaterialCost(totalPrice);
            pricingColors.add(pricingColorVO);
        }
        pricingVO.setPricingColors(pricingColors);
        pricingVO.setPricingProcessCosts(this.getPricingProcessCostsVOS(id));
        return pricingVO;
    }

    private Map<String, List<PricingMaterialCostsVO>> getPricingMaterialCostsMap(String id, String colorCode) {
        List<PricingMaterialCostsVO> pricingMaterialCostsVOS = packBomService.getPricingMaterialCostsByForeignId(id);

        if (CollectionUtils.isEmpty(pricingMaterialCostsVOS)) {
            return new HashMap<>(0);
        }
        List<String> categoryName = pricingMaterialCostsVOS.stream()
                .filter(x -> StringUtils.isNotEmpty(x.getCategoryName()))
                .map(e -> e.getCategoryName().split("-")[0])
                .collect(Collectors.toList());
        // TODO 通过类别名称获取类别id

        return pricingMaterialCostsVOS.stream()
                .peek(e -> {
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(e.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                    e.setTotalPrice(BigDecimalUtil.mul(2, e.getUnitNum(), lossRate, e.getPrice()));
                    e.setTotalUsage(BigDecimalUtil.mul(2, e.getUnitNum(), lossRate));
                    e.setColorCode(colorCode);
                }).filter(x -> StringUtils.isNotEmpty(x.getColorCode()))
                .collect(Collectors.groupingBy(PricingMaterialCostsVO::getColorCode));
    }

    private List<PricingProcessCostsVO> getPricingProcessCostsVOS(String id) {
        List<PricingProcessCostsVO> pricingProcessCostsVOS = packProcessPriceService.getPricingProcessCostsByForeignId(id);
        if (CollectionUtils.isEmpty(pricingProcessCostsVOS)) {
            return Lists.newArrayList();
        }
        return pricingProcessCostsVOS.stream()
                .filter(Objects::nonNull)
                .peek(e -> e.setQuotationPrice(BigDecimalUtil.mul(2, e.getStandardUnitPrices(), e.getMultiple())))
                .collect(Collectors.toList());
    }

    private String getStylePic(String id) {
        List<AttachmentVo> attachmentVos = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
        if (CollectionUtils.isEmpty(attachmentVos)) {
            return null;
        }
        return attachmentVos.stream()
                .map(AttachmentVo::getUrl)
                .collect(Collectors.joining(","));
    }

    @Override
    public void submitApprove(String id, String userCompany) {
        logger.info("PricingService#submitApprove 提交审核 id:{}, userCompany:{}", id, userCompany);
        Pricing pricing = super.getById(id);
        if (Objects.isNull(pricing)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        if (!BaseGlobal.STOCK_STATUS_DRAFT.equals(pricing.getConfirmStatus()) && !BaseGlobal.STOCK_STATUS_REJECT.equals(pricing.getConfirmStatus())) {
            throw new BusinessException(BaseErrorEnum.ERR_STATUS_DELETE);
        }
        LambdaUpdateWrapper<Pricing> updateWrapper = new LambdaUpdateWrapper<Pricing>()
                .set(Pricing::getConfirmStatus, BaseGlobal.STOCK_STATUS_WAIT_CHECK)
                .eq(Pricing::getId, id);
        super.update(updateWrapper);
        // TODO 发起审批


    }


    @Override
    public PricingCountVO costsCount(PricingCountDTO pricingCountDTO) {
        PricingCountVO pricingCountVO = new PricingCountVO();
        switch (PricingCountTypeEnum.getByK(pricingCountDTO.getCountType())) {
            case MATERIAL_COSTS:
                BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(pricingCountDTO.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                pricingCountVO.setTotalPrice(Objects.isNull(pricingCountDTO.getExchangeRate()) ?
                        BigDecimalUtil.mul(2, pricingCountDTO.getNum(), lossRate, pricingCountDTO.getPrice())
                        : BigDecimalUtil.mul(2, pricingCountDTO.getNum(), lossRate, pricingCountDTO.getPrice(), pricingCountDTO.getExchangeRate()));
                pricingCountVO.setTotalUsage(BigDecimalUtil.mul(2, pricingCountDTO.getNum(), lossRate));
                break;
            case PROCESS_COSTS:
                pricingCountVO.setTotalPrice(BigDecimalUtil.mul(2, pricingCountDTO.getPrice(), pricingCountDTO.getMultiple()));
                break;
            case CRAFT_COSTS:
                pricingCountVO.setTotalPrice(BigDecimalUtil.mul(2, pricingCountDTO.getNum(), pricingCountDTO.getPrice()));
                break;
            default:
                break;
        }
        return pricingCountVO;
    }

    private void checkIsExistAuditing(String id, List<String> ids, String userCompany) {
        LambdaQueryWrapper<Pricing> queryWrapper = new QueryWrapper<Pricing>().lambda()
                .in(CollectionUtils.isNotEmpty(ids), Pricing::getId, ids)
                .eq(StringUtils.isNotEmpty(id), Pricing::getId, id)
                .eq(Pricing::getCompanyCode, userCompany)
                .eq(Pricing::getDelFlag, YesOrNoEnum.NO.getValueStr())
                .ne(Pricing::getConfirmStatus, BaseGlobal.STOCK_STATUS_DRAFT)
                .ne(Pricing::getConfirmStatus, BaseGlobal.STOCK_STATUS_REJECT);
        long count = super.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(BaseErrorEnum.ERR_STATUS_DELETE);
        }
    }
// 自定义方法区 不替换的区域【other_end】

}

