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
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.flowable.vo.FlowRecordVo;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.basicsdatum.controller.BasicsdatumMaterialController;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
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
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;
    @Autowired
    private HangTagLogService hangTagLogService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private StyleColorMapper styleColorMapper;
    @Autowired
    private FlowableService flowableService;
    @Autowired
    @Lazy
    private BasicsdatumMaterialController basicsdatumMaterialController;
    @Autowired
    @Lazy
    private StyleColorService styleColorService;

    @Autowired
    @Lazy
    private PackInfoService packInfoService;
    private final StylePricingService stylePricingService;
    private final StyleService styleService;
    private final BasicsdatumSizeService basicsdatumSizeService;
    private final PackBomService packBomService;
    @Autowired
    @Lazy
    private DataPermissionsService dataPermissionsService;

    @Override
    public PageInfo<HangTagListVO> queryPageInfo(HangTagSearchDTO hangTagDTO, String userCompany) {
        hangTagDTO.setCompanyCode(userCompany);
        PageHelper.startPage(hangTagDTO.getPageNum(), hangTagDTO.getPageSize());
        String authSql = dataPermissionsService.getDataPermissionsSql(DataPermissionsBusinessTypeEnum.hangTagList.getK(), "tsd.", null, false);
        List<HangTagListVO> hangTagListVOS = hangTagMapper.queryList(hangTagDTO, authSql);

        if (hangTagListVOS.isEmpty()) {
            return new PageInfo<>(hangTagListVOS);
        }
        /*获取大货款号*/
        List<String> stringList = hangTagListVOS.stream().filter(h -> !StringUtils.isEmpty(h.getBulkStyleNo())).map(HangTagListVO::getBulkStyleNo).distinct().collect(Collectors.toList());
        /*查询流程审批的结果*/
        Map<String, FlowRecordVo> flowRecordVoMap = flowableService.getFlowRecordMapBybusinessKey(stringList);
        //1A7290012
        IdGen idGen = new IdGen();
        List<String> bulkStyleNos = new ArrayList<>();
        hangTagListVOS.forEach(e -> {
            FlowRecordVo flowRecordVo = flowRecordVoMap.get(e.getBulkStyleNo());
            if (!ObjectUtils.isEmpty(flowRecordVo)) {
//                判断流程是否完成
                e.setExamineUserNema(flowRecordVo.getUserName());
                e.setExamineUserId(flowRecordVo.getUserId());
                if (BaseGlobal.YES.equals(flowRecordVo.getEndFlag())) {
//                    e.setConfirmDate(flowRecordVo.getEndTime());
                    //e.setStatus("5");  不需要设置为通过,通过或者不通过会在回调页面设置
                } else {
                    //状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认
                    switch (flowRecordVo.getName()) {
                        case "大货工艺员确认":
                            e.setStatus("2");
                            break;
                        case "后技术确认":
                            e.setStatus("3");
                            break;
                        case "品控确认":
                            e.setStatus("4");
                            break;
                    }
                }
            }
            bulkStyleNos.add(e.getBulkStyleNo());

        });
        List<PackInfo> packInfos = packInfoService.list(new QueryWrapper<PackInfo>().in("style_no", bulkStyleNos).select("id", "style_no"));
        List<String> packInfoIds = packInfos.stream().map(PackInfo::getId).collect(Collectors.toList());
        if (!packInfoIds.isEmpty()){
            List<PackInfoStatus> packInfoStatus = packInfoStatusService.list(new QueryWrapper<PackInfoStatus>().in("foreign_id", packInfoIds).select("foreign_id", "bom_status"));
            Map<String, String> hashMap = new HashMap<>();
            for (PackInfo packInfo : packInfos) {
                for (PackInfoStatus infoStatus : packInfoStatus) {
                    if (packInfo.getId().equals(infoStatus.getForeignId())) {
                        hashMap.put(packInfo.getStyleNo(), infoStatus.getBomStatus());
                    }
                }
            }
            for (HangTagListVO hangTagListVO : hangTagListVOS) {
                hangTagListVO.setBomStatus(hashMap.get(hangTagListVO.getBulkStyleNo()));
            }
        }

        return new PageInfo<>(hangTagListVOS);
    }

    @Override
    public HangTagVO getDetailsByBulkStyleNo(String bulkStyleNo, String userCompany, String selectType) {
        HangTagVO hangTagVO = hangTagMapper.getDetailsByBulkStyleNo(bulkStyleNo, userCompany, selectType);
        if (hangTagVO != null && StringUtils.isEmpty(hangTagVO.getStatus())) {
            hangTagVO.setStatus(HangTagStatusEnum.NOT_SUBMIT.getK());
        }
        //查关联成分表
        if (hangTagVO != null) {
            PackInfo packInfo = packInfoService.getOne(new QueryWrapper<PackInfo>().eq("style_no", hangTagVO.getBulkStyleNo()));
            if (packInfo != null) {
                List<PackBom> packBomList = packBomService.list(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("pack_type", "packBigGoods"));
                if (!packBomList.isEmpty()) {
                    List<String> codes = packBomList.stream().map(PackBom::getMaterialCode).collect(Collectors.toList());
                    if (!codes.isEmpty()) {
                        List<EscmMaterialCompnentInspectCompanyDto> inspectCompanyDtoList = escmMaterialCompnentInspectCompanyService.list(new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().in("materials_no", codes));
                        hangTagVO.setInspectCompanyDtoList(inspectCompanyDtoList);
                    }
                }
            }
        }
        return hangTagVO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String save(HangTagDTO hangTagDTO, String userCompany) {
        logger.info("HangTagService#save 保存吊牌 hangTagDTO:{}, userCompany:{}", JSON.toJSONString(hangTagDTO), userCompany);
        HangTag hangTag = new HangTag();
        BeanUtils.copyProperties(hangTagDTO, hangTag);
        super.saveOrUpdate(hangTag,"吊牌管理");
        String id = hangTag.getId();

        //List<BasicsdatumMaterialIngredient> materialIngredientList = basicsdatumMaterialController.formatToList(hangTagDTO.getIngredient(), "0", "");

        //List<HangTagIngredientDTO> hangTagIngredients = new ArrayList<>();
        //for (BasicsdatumMaterialIngredient basicsdatumMaterialIngredient : materialIngredientList) {
        //    HangTagIngredientDTO hangTagIngredient = new HangTagIngredientDTO();
        //    hangTagIngredient.setPercentage(basicsdatumMaterialIngredient.getRatio());
        //    hangTagIngredient.setType(basicsdatumMaterialIngredient.getName());
        //    hangTagIngredient.setTypeCode(basicsdatumMaterialIngredient.getType());
        //    hangTagIngredient.setDescriptionRemarks(basicsdatumMaterialIngredient.getSay());
        //    hangTagIngredients.add(hangTagIngredient);
        //}
        //hangTagIngredientService.remove(new QueryWrapper<HangTagIngredient>().eq("hang_tag_id", id));
        //hangTagIngredientService.save(hangTagIngredients, id, userCompany);
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
        if ("2".equals(hangTag.getStatus())) {
            hangTag = this.getById(hangTag.getId());
            //发起审批
            flowableService.start(FlowableService.HANGING_TAG_REVIEW + hangTag.getBulkStyleNo(), FlowableService.HANGING_TAG_REVIEW, hangTag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
                    "/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null, BeanUtil.beanToMap(hangTag));
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

                //if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(e.getStatus()) &&
                //        !HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                //    throw new OtherException("存在待工艺员确认数据，请先待工艺员确认");
                //}
                if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(e.getStatus()) &&
                        !HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                    throw new OtherException("已提交审核,请等待");
                }

                //if (HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(e.getStatus()) &&
                //        !HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                //    throw new OtherException("存在待技术员确认数据，请先技术员确认");
                //}
                //if (HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(e.getStatus()) &&
                //        !HangTagStatusEnum.CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
                //    throw new OtherException("存在待品控确认数据，请先品控确认");
                //}
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
        List<HangTag> hangTags1 = this.listByIds(hangTagUpdateStatusDTO.getIds());
        for (HangTag tag : hangTags1) {
            flowableService.start(FlowableService.HANGING_TAG_REVIEW + tag.getBulkStyleNo(), FlowableService.HANGING_TAG_REVIEW, tag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
                    "/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null, BeanUtil.beanToMap(tag));

        }
    }

    @Override
    public List<TagPrinting> hangTagPrinting(String styleNo, boolean likeQueryFlag) {
        BaseQueryWrapper<HangTag> baseQueryWrapper = new BaseQueryWrapper<>();
        List<TagPrinting> tagPrintings = new ArrayList<>();
        if (likeQueryFlag) {
            baseQueryWrapper.notEmptyEq("bulk_style_no", styleNo);
        } else {
            baseQueryWrapper.notEmptyLike("bulk_style_no", styleNo);
        }

        List<HangTag> list = this.list(baseQueryWrapper);
        if (!list.isEmpty()) {
            for (HangTag hangTag : list) {
                //配色
                StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", hangTag.getBulkStyleNo()));
                Style style = styleService.getById(styleColor.getStyleId());


                TagPrinting tagPrinting = new TagPrinting();
                // 唯一码
                tagPrinting.setC8_Colorway_WareCode(styleColor.getWareCode());
                // 是否赠品
                tagPrinting.setIsGift(null);
                // 批次
                tagPrinting.setC8_Colorway_BatchNo(null);
                // 颜色名称
                tagPrinting.setColorDescription(styleColor.getColorName());
                // 颜色编码
                tagPrinting.setColorCode(styleColor.getColorCode());
                // 大货款号
                tagPrinting.setStyleCode(hangTag.getBulkStyleNo());


                PackInfo packInfo = packInfoService.getOne(new QueryWrapper<PackInfo>().eq("code", styleColor.getBom()));
                if (packInfo != null) {
                    //款式定价
                    StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), styleColor.getCompanyCode());
                    if (stylePricingVO != null) {
                        // 商品吊牌价确认
                        tagPrinting.setMerchApproved("1".equals(stylePricingVO.getProductTagPriceConfirm()));
                        // 系列
                        tagPrinting.setC8_Colorway_Series(stylePricingVO.getSeries());

                    }
                }

                // 配饰款号
                tagPrinting.setSecCode(styleColor.getAccessoryNo());
                // 主款款号
                tagPrinting.setMainCode(styleColor.getPrincipalStyleNo());
                // 吊牌价
                tagPrinting.setC8_Colorway_SalesPrice(styleColor.getTagPrice());
                // 是否内配饰
                tagPrinting.setIsAccessories(!StringUtils.isEmpty(styleColor.getAccessoryNo()));
                // 大货款号是否激活
                tagPrinting.setActive("0".equals(styleColor.getStatus()));
                // 销售类型
                tagPrinting.setC8_Colorway_SaleType(styleColor.getSalesType());
                // 品牌_描述
                tagPrinting.setC8_Season_Brand(style.getYear() + style.getSeason() + style.getBrandName());
                // 品类id
                tagPrinting.setC8_Collection_ProdCategory(style.getProdCategory());
                // 主题
                tagPrinting.setTheme(style.getSubject());
                // 小类编码
                tagPrinting.setC8_Style_3rdCategory(style.getProdCategory3rd());
                // 中类编码
                tagPrinting.setC8_Style_2ndCategory(style.getProdCategory2nd());
                // 尺码号型编码
                tagPrinting.setSizeRangeCode(style.getSizeRange());
                // 尺码号型名称
                tagPrinting.setSizeRangeName(style.getSizeRangeName());
                // 款式分类
                tagPrinting.setProductType(style.getStyleName());
                // 大类
                tagPrinting.setC8_1stProdCategory(style.getProdCategory1stName());
                // 尺码号型:分类
                tagPrinting.setSizeRangeDimensionType("size");
                // 成分
                tagPrinting.setComposition(hangTag.getIngredient());
                // 洗标
                tagPrinting.setCareSymbols(hangTag.getWashingLabelName());
                // 质量等级
                tagPrinting.setQualityClass(hangTag.getQualityGrade());
                // 品名
                tagPrinting.setProductName(hangTag.getProductName());
                // 安全类别
                tagPrinting.setSaftyType(hangTag.getSaftyType());
                // 执行标准
                tagPrinting.setOPStandard(hangTag.getExecuteStandard());
                // 品控部确认
                tagPrinting.setApproved("5".equals(hangTag.getStatus()));
                // 温馨提示
                tagPrinting.setAttention(hangTag.getWarmTips());
                // 后技术确认
                tagPrinting.setTechApproved(Integer.parseInt(hangTag.getStatus()) > 2 && !"6".equals(hangTag.getStatus()));
                // 安全标题
                tagPrinting.setSaftyTitle(hangTag.getSaftyTitle());
                // 洗唛材质备注
                tagPrinting.setC8_APPBOM_Comment(hangTag.getWashingMaterialRemarks());
                // 贮藏要求
                tagPrinting.setC8_APPBOM_StorageReq(hangTag.getStorageDemand());
                // 产地
                tagPrinting.setC8_APPBOM_MadeIn(hangTag.getProducer());
                // 入库时间
                tagPrinting.setC8_APPBOM_StorageTime(null);
                // 英文成分
                tagPrinting.setCompsitionMix(null);
                // 英文温馨提示
                tagPrinting.setWarmPointEN(null);
                // 英文贮藏要求
                tagPrinting.setStorageReqEN(null);
                List<TagPrinting.Size> size = new ArrayList<>();

                String sizeIds = style.getSizeIds();
                if (!StringUtils.isEmpty(sizeIds)) {
                    for (BasicsdatumSize basicsdatumSize : basicsdatumSizeService.listByIds(Arrays.asList(sizeIds.split(",")))) {
                        TagPrinting.Size size1 = new TagPrinting.Size();
                        size1.setSIZECODE(basicsdatumSize.getInternalSize());
                        size1.setSORTCODE(basicsdatumSize.getCode());
                        size1.setSIZENAME(basicsdatumSize.getModel());
                        size1.setSizeID(basicsdatumSize.getSort());
                        size1.setEXTSIZECODE("");
                        size1.setShowIntSize("0".equals(basicsdatumSize.getShowSizeStatus()));
                        size1.setEuropeCode(basicsdatumSize.getEuropeanSize());
                        size1.setSKUFiller(hangTag.getDownContent());
                        size1.setSpecialSpec("");
                        size.add(size1);
                    }

                }
                // 款式尺码明细
                tagPrinting.setSize(size);

                tagPrintings.add(tagPrinting);
            }
        }

        //改变吊牌打印状态
        list.forEach(e -> {
            e.setPrintOrNot("1");
        });
        this.updateBatchById(list);

        return tagPrintings;
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

