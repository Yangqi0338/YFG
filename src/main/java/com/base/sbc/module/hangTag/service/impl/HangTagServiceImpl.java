/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangTag.dto.HangTagDTO;
import com.base.sbc.module.hangTag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangTag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangTag.entity.HangTag;
import com.base.sbc.module.hangTag.enums.HangTagStatusEnum;
import com.base.sbc.module.hangTag.enums.OperationDescriptionEnum;
import com.base.sbc.module.hangTag.mapper.HangTagMapper;
import com.base.sbc.module.hangTag.service.HangTagIngredientService;
import com.base.sbc.module.hangTag.service.HangTagLogService;
import com.base.sbc.module.hangTag.service.HangTagService;
import com.base.sbc.module.hangTag.vo.HangTagListVO;
import com.base.sbc.module.hangTag.vo.HangTagVO;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：吊牌表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:53
 */
@Service
@RequiredArgsConstructor
public class HangTagServiceImpl extends BaseServiceImpl<HangTagMapper, HangTag> implements HangTagService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(HangTagService.class);

    @Autowired
    private HangTagMapper hangTagMapper;
    @Autowired
    private HangTagIngredientService hangTagIngredientService;
    @Autowired
    private HangTagLogService hangTagLogService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private StyleColorMapper styleColorMapper;
    private final FlowableService flowableService;

    @Override
    public PageInfo<HangTagListVO> queryPageInfo(HangTagSearchDTO hangTagDTO, String userCompany) {
        hangTagDTO.setCompanyCode(userCompany);
        PageHelper.startPage(hangTagDTO.getPageNum(), hangTagDTO.getPageSize());
        List<HangTagListVO> hangTagListVOS = hangTagMapper.queryList(hangTagDTO);
        IdGen idGen = new IdGen();
        hangTagListVOS.forEach(e -> {
            if (StringUtils.isEmpty(e.getId())) {
                e.setId(idGen.nextIdStr());
            }
        });
        return new PageInfo<>(hangTagListVOS);
    }

    @Override
    public HangTagVO getDetailsByBulkStyleNo(String bulkStyleNo, String userCompany) {
        HangTagVO hangTagVO = hangTagMapper.getDetailsByBulkStyleNo(bulkStyleNo, userCompany);
        if (hangTagVO != null && StringUtils.isEmpty(hangTagVO.getStatus())) {
            hangTagVO.setStatus(HangTagStatusEnum.NOT_SUBMIT.getK());
        }

        return hangTagVO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String save(HangTagDTO hangTagDTO, String userCompany) {
        logger.info("HangTagService#save 保存吊牌 hangTagDTO:{}, userCompany:{}", JSON.toJSONString(hangTagDTO), userCompany);
        HangTag hangTag = new HangTag();
        BeanUtils.copyProperties(hangTagDTO, hangTag);
        hangTag.insertInit();
        super.saveOrUpdate(hangTag);
        String id = hangTag.getId();
        hangTagIngredientService.save(hangTagDTO.getHangTagIngredients(), id, userCompany);
        hangTagLogService.save(id, OperationDescriptionEnum.SAVE.getV(), userCompany);
        /**
         * 当存在品名时同步到配色
         */
        if (!StringUtils.isEmpty(hangTag.getProductCode()) && !StringUtils.isEmpty(hangTag.getProductName())) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("style_no", hangTag.getBulkStyleNo());
            queryWrapper.eq("company_code", userCompany);
            /**/
            StyleColor styleColor = styleColorMapper.selectOne(queryWrapper);
            /*同步配色品名*/
            if (!ObjectUtils.isEmpty(styleColor)) {
                styleColor.setProductCode(hangTag.getProductCode());
                styleColor.setProductName(hangTag.getProductName());
                styleColorMapper.updateById(styleColor);
            }
        }
        return id;
    }


    @Override
    public void updateStatus(HangTagUpdateStatusDTO hangTagUpdateStatusDTO, String userCompany) {
        logger.info("HangTagService#updateStatus 更新状态 hangTagUpdateStatusDTO:{}, userCompany:{}", JSON.toJSONString(hangTagUpdateStatusDTO), userCompany);

        LambdaQueryWrapper<HangTag> queryWrapper = new QueryWrapper<HangTag>().lambda()
                .in(HangTag::getId, hangTagUpdateStatusDTO.getIds())
                .eq(HangTag::getCompanyCode, userCompany);
        List<HangTag> hangTags = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(hangTags)) {
            throw new OtherException("存在未填写数据，请先填写");
        }
        ArrayList<HangTag> updateHangTags = Lists.newArrayList();
        hangTags.forEach(e -> {
            if (!HangTagStatusEnum.NOT_SUBMIT.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                if (HangTagStatusEnum.CONFIRMED.getK().equals(e.getStatus())) {
                    throw new OtherException("存在已确认数据，请勿重复确认");
                }
                if (HangTagStatusEnum.NOT_SUBMIT.getK().equals(e.getStatus()) &&
                        !HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                    throw new OtherException("存在待提交数据，请先提交");
                }

                if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(e.getStatus()) &&
                        !HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                    throw new OtherException("存在待工艺员确认数据，请先待工艺员确认");
                }

                if (HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(e.getStatus()) &&
                        !HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                    throw new OtherException("存在待技术员确认数据，请先技术员确认");
                }
                if (HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(e.getStatus()) &&
                        !HangTagStatusEnum.CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                    throw new OtherException("存在待品控确认数据，请先品控确认");
                }
            }
            HangTag hangTag = new HangTag();
            hangTag.setId(e.getId());
            hangTag.updateInit();
            hangTag.setStatus(hangTagUpdateStatusDTO.getStatus());
            if (HangTagStatusEnum.CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                hangTag.setConfirmDate(new Date());
            }
            updateHangTags.add(hangTag);

        });
        super.updateBatchById(updateHangTags);
        hangTagLogService.saveBatch(hangTagUpdateStatusDTO.getIds(), OperationDescriptionEnum.getV(hangTagUpdateStatusDTO.getStatus()), userCompany);
        //发送审批
        for (HangTag tag : updateHangTags) {
            flowableService.start(FlowableService.HANGING_TAG_REVIEW + tag.getBulkStyleNo(), FlowableService.HANGING_TAG_REVIEW, tag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
                    "/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null, BeanUtil.beanToMap(tag));

        }
    }

    @Override
    public List<TagPrinting> hangTagPrinting(String styleNo, Boolean likeQueryFlag, String companyCode) {
        String flag = Boolean.TRUE.equals(likeQueryFlag) ? "1" : "0";
        return hangTagMapper.hangTagPrinting(companyCode, styleNo, flag);
//        HangTagSearchDTO hangTagSearchDTO = new HangTagSearchDTO();
//        List<HangTagListVO> hangTagListVOS = hangTagMapper.queryList(hangTagSearchDTO);
//        List<TagPrinting> tagPrintings = hangTagListVOS.stream()
//                .map(hangTagListVO -> {
//                    TagPrinting tagPrinting = new TagPrinting();
//                    tagPrinting.setColorCode(hangTagListVO.getColorCode());
//                    tagPrinting.setColorDescription(hangTagListVO.getColor());
//                    tagPrinting.setStyleCode(hangTagListVO.getBulkStyleNo());
//                    tagPrinting.setComposition(hangTagListVO.getIngredient());
//                    tagPrinting.setCareSymbols(hangTagListVO.getWashingLabel());
//                    tagPrinting.setQualityClass(hangTagListVO.getQualityGrade());
//                    tagPrinting.setSaftyType(hangTagListVO.getSaftyType());
//                    tagPrinting.setOPStandard(hangTagListVO.getExecuteStandard());
//                    boolean isApproved = HangTagStatusEnum.CONFIRMED.getK().equals(hangTagListVO.getStatus());
//                    tagPrinting.setApproved(isApproved);
//                    tagPrinting.setAttention(hangTagListVO.getWarmTips());
//                    boolean isTechApproved = HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(hangTagListVO.getStatus());
//                    tagPrinting.setTechApproved(isApproved || isTechApproved);
//                    tagPrinting.setSaftyTitle(hangTagListVO.getSaftyTitle());
//                    tagPrinting.setC8_APPBOM_Comment(hangTagListVO.getWashingMaterialRemarks());
//                    tagPrinting.setStorageRequirement(hangTagListVO.getStorageDemand());
//                    return tagPrinting;
//                }).collect(Collectors.toList());
//        return tagPrintings;
    }

    @Override
    public String getTechSpecFileByStyleNo(String styleNo) {
        String techSpecFileId = packInfoStatusService.getTechSpecFileIdByStyleNo(styleNo);
        if (StringUtils.isEmpty(techSpecFileId)) {
            return null;
        }
        return uploadFileService.getUrlById(techSpecFileId);
    }


// 自定义方法区 不替换的区域【other_end】

}

