//package com.base.sbc.open.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.base.sbc.config.utils.StringUtils;
//import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
//import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
//import com.base.sbc.module.hangtag.entity.HangTag;
//import com.base.sbc.module.hangtag.service.HangTagService;
//import com.base.sbc.module.pack.entity.PackInfo;
//import com.base.sbc.module.pack.entity.PackPricing;
//import com.base.sbc.module.pack.entity.PackTechSpec;
//import com.base.sbc.module.pack.service.PackInfoService;
//import com.base.sbc.module.pack.service.PackPricingService;
//import com.base.sbc.module.pack.service.PackTechSpecService;
//import com.base.sbc.module.pricing.service.StylePricingService;
//import com.base.sbc.module.pricing.vo.StylePricingVO;
//import com.base.sbc.module.style.dto.QueryStyleColorDto;
//import com.base.sbc.module.style.entity.Style;
//import com.base.sbc.module.style.service.StyleColorService;
//import com.base.sbc.module.style.service.StyleService;
//import com.base.sbc.module.style.vo.StyleColorVo;
//import com.base.sbc.open.entity.BiColorway;
//import com.base.sbc.open.mapper.BiColorwayMapper;
//import com.base.sbc.open.service.BiColorwayService;
//import com.github.pagehelper.PageInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author 卞康
// * @date 2023/8/23 9:38:50
// * @mail 247967116@qq.com
// */
//@Service
//@RequiredArgsConstructor
//public class BiColorwayServiceImpl extends ServiceImpl<BiColorwayMapper, BiColorway> implements BiColorwayService {
//    private final StyleColorService styleColorService;
//    private final PackInfoService packInfoService;
//    private final PackPricingService packPricingService;
//    private final StylePricingService stylePricingService;
//    private final StyleService styleService;
//    private final PackTechSpecService packTechSpecService;
//    private final HangTagService hangTagService;
//    private final BasicsdatumSizeService basicsdatumSizeService;
//    /**
//     *
//     */
//    @Override
//    public void colorway() {
//        List<BiColorway> list = new ArrayList<>();
//        QueryStyleColorDto queryStyleColorDto = new QueryStyleColorDto();
//        queryStyleColorDto.setPageNum(1);
//        queryStyleColorDto.setPageSize(99999);
//        PageInfo<StyleColorVo> sampleStyleColorList = styleColorService.getSampleStyleColorList(null, queryStyleColorDto);
//
//
//        for (StyleColorVo styleColorVo : sampleStyleColorList.getList()) {
//
//            Style style = styleService.getById(styleColorVo.getStyleId());
//            BiColorway biColorway = new BiColorway();
//            biColorway.setColorwayCode(styleColorVo.getStyleNo());
//            biColorway.setColorSpecification(styleColorVo.getColorSpecification());
//            biColorway.setColorCode(styleColorVo.getColorCode());
//            biColorway.setColorName(styleColorVo.getColorName());
//            biColorway.setC8ColorwayBand(styleColorVo.getBandName());
//            biColorway.setC8ColorwaySalesPrice(styleColorVo.getTagPrice());
//            biColorway.setC8ColorwaySaleTime(styleColorVo.getNewDate());
//            biColorway.setC8ColorwayStyles(styleColorVo.getStyleFlavourName());
//            biColorway.setPatternName(styleColorVo.getPatternDesignName());
//            PackInfo packInfo = packInfoService.getOne(new QueryWrapper<PackInfo>().eq("code", styleColorVo.getBom()).eq("style_color_id",styleColorVo.getId()));
//            if (packInfo != null) {
//                // 核价
//                PackPricing packPricing = packPricingService.getOne(new QueryWrapper<PackPricing>().eq("foreign_id", packInfo.getId()).eq("pack_type", "packBigGoods"));
//                if (packPricing != null) {
//                    JSONObject jsonObject = JSON.parseObject(packPricing.getCalcItemVal());
//                    biColorway.setC8ColorwayTotalCosts(jsonObject.getBigDecimal("成本价") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("成本价"));
//                    biColorway.setC8ColorwayLaborCosts(jsonObject.getBigDecimal("车缝加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("车缝加工费"));
//                    biColorway.setC8ColorwayMaterialCost(jsonObject.getBigDecimal("物料费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("物料费"));
//                    biColorway.setC8ColorwayPackageCost(jsonObject.getString("包装费"));
//                    biColorway.setC8ColorwayFixedTagPrice(jsonObject.getString("倍价"));
//                    biColorway.setC8ColorwayTotalCostWOCollar(jsonObject.getString("除毛领成本"));
//                    biColorway.setC8ColorwayInspectionFee(jsonObject.getString("检测费"));
//                    biColorway.setC8ColorwayCollarCost(jsonObject.getString("毛领成本"));
//                    biColorway.setC8ColorwayMaoshaCosts(jsonObject.getString("毛纱加工费"));
//                    biColorway.setC8ColorwayMarckupWOCollar(jsonObject.getString("去毛领倍率"));
//                    biColorway.setC8ColorwaySubcontractCosts(jsonObject.getString("外协加工费"));
//
//                }
//                //款式定价
//                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), packInfo.getCompanyCode());
//                biColorway.setC8ColorwayMarckup(stylePricingVO.getActualMagnification());
//                biColorway.setC8ColorwaySpPriceConfirm("1".equals(stylePricingVO.getProductTagPriceConfirm()));
//                biColorway.setC8ColorwayJkCosts(stylePricingVO.getPlanCost());
//                biColorway.setC8ColorwayMarckup4Pc(stylePricingVO.getPlanActualMagnification());
//                biColorway.setC8ColorwaySeries(stylePricingVO.getSeries());
//
//                //充绒量
//                biColorway.setC8ColorwayFabricQuantity(stylePricingVO.getDownContent());
//
//            }
//            biColorway.setC8AppbomProductName(styleColorVo.getProductName());
//            biColorway.setC8ColorwayWareCode(styleColorVo.getWareCode());
//            biColorway.setColorwayActive("0".equals(styleColorVo.getStatus()) ? "启用" : "停用");
//            biColorway.setC8ColorwayBomPhase(styleColorVo.getBomStatus());
//            String scmSendFlag = styleColorVo.getScmSendFlag();
//            String status = "";
//            if (scmSendFlag!=null){
//                switch (scmSendFlag) {
//                    case "0":
//                        status = "未发送";
//                        break;
//                    case "1":
//                        status = "发送成功";
//                        break;
//                    case "2":
//                        status = "发送失败";
//                        break;
//                    case "3":
//                        status = "重新打开 ";
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            biColorway.setC8SyncSendStatus(status);
//
//            biColorway.setC8ColorwayBimages(styleColorVo.getStylePic());
//            biColorway.setC8ColorwayFixedRatio("6.5");
//            biColorway.setC8ColorwayProdSeg(styleColorVo.getProductSubdivideName());
//
//            biColorway.setC8ColorwayCpNum(styleColorVo.getDefectiveNo());
//            biColorway.setC8ColorwayJkCostConfirm(styleColorVo.getControlConfirm());
//            biColorway.setC8ColorwaySalesApproved(styleColorVo.getProductHangtagConfirm());
//
//            biColorway.setDevelopmentMode(style.getDevtTypeName());
//
//            biColorway.setC8ColorwayAccessories(styleColorVo.getIsTrim());
//            biColorway.setC8ColorwayCollectAccCodes(styleColorVo.getAccessoryNo());
//            biColorway.setC8ColorwayIfQ(("1".equals(styleColorVo.getIsLuxury()) ? "是" : "否"));
//
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            if (styleColorVo.getDesignDetailDate() != null) {
//                biColorway.setC8ColorwayDetailedListTs(simpleDateFormat.format(styleColorVo.getDesignDetailDate()));
//            }
//
//            if (styleColorVo.getDesignCorrectDate() != null) {
//                biColorway.setC8ColorwayCorrectSampleTs(simpleDateFormat.format(styleColorVo.getDesignCorrectDate()));
//            }
//            biColorway.setC8ColorwayIfOrderMeeting("1".equals(styleColorVo.getMeetFlag()) ? "未上会" : "已上会");
//
//
//            if (styleColorVo.getSendBatchingDate1() != null) {
//                biColorway.setC8ColorwaySubMat1Ts(simpleDateFormat.format(styleColorVo.getSendBatchingDate1()));
//            }
//
//            if (styleColorVo.getSendBatchingDate2() != null) {
//                biColorway.setC8ColorwaySubMat2Ts(simpleDateFormat.format(styleColorVo.getSendBatchingDate2()));
//            }
//            if (styleColorVo.getSendMainFabricDate() != null) {
//                biColorway.setC8ColorwayMainMatts(simpleDateFormat.format(styleColorVo.getSendMainFabricDate()));
//            }
//
//            if (styleColorVo.getSendSingleDate() != null) {
//                biColorway.setC8ColorwayInterMatTs(simpleDateFormat.format(styleColorVo.getSendSingleDate()));
//            }
//
//
//            if (styleColorVo.getCreateDate() != null) {
//                biColorway.setCreatedAt(simpleDateFormat.format(styleColorVo.getCreateDate()));
//            }
//
//            if (styleColorVo.getUpdateDate() != null) {
//                biColorway.setModifiedAt(simpleDateFormat.format(styleColorVo.getUpdateDate()));
//            }
//
//            biColorway.setC8ColorwaySaleType(styleColorVo.getSalesTypeName());
//
//            biColorway.setC8ColorwayMainStylesCodes(styleColorVo.getPrincipalStyleNo());
//            biColorway.setC8ColorwayBomSendStatus(styleColorVo.getBomStatus());
//            biColorway.setC8ColorwayInactivedCause(styleColorVo.getRemarks());
//            biColorway.setC8ColorwaySupplier(styleColorVo.getSupplierAbbreviation());
//            biColorway.setC8ColorwaySupplierArticle(styleColorVo.getSupplierNo());
//            biColorway.setC8ColorwaySupplierArticleColor(styleColorVo.getSupplierColor());
//            biColorway.setC8ColorwayPlmid(styleColorVo.getId());
//            biColorway.setC8StylePlmid(style.getId());
//            biColorway.setC8ColorwayIsFeaturedPro("0".equals(styleColorVo.getIsMainly()) ? "否" : "是");
//
//            biColorway.setCreatedBy(styleColorVo.getCreateName());
//            biColorway.setModifiedBy(styleColorVo.getUpdateName());
//            biColorway.setC8AppbomProTechnician(style.getTechnicianName());
//            biColorway.setC8AppbomOrdePersonnel(StringUtils.isNotEmpty(style.getDesigner()) ? style.getDesigner().split(",")[0] : "");
//
//
//            //暂不需要
//            //`包含于`
//            biColorway.setContainedBy(null);
//            //预计销售价
//            biColorway.setC8ColorwayTargetPrice(null);
//            //折扣率
//            biColorway.setC8ColorwayDiscountRate(null);
//            //开发类型
//            biColorway.setDevelopmentType(null);
//            //下单量
//            biColorway.setC8ColorwayOrderQty(null);
//            //上新价
//            biColorway.setC8ColorwayFinalSalesPrice(null);
//            //编码方式结束
//            biColorway.setC8ColorwayCodeWayEnd(null);
//
//
//            HangTag hangTag = hangTagService.getOne(new QueryWrapper<HangTag>().eq("bulk_style_no", styleColorVo.getStyleNo()));
//            //下单时间
//            if(hangTag!=null){
//                biColorway.setC8AppbomOrderTime(hangTag.getPlaceOrderDate());
//            }
//
//
//
//
//
//
//            //工艺说明             //含外辅工艺
//            long count = packTechSpecService.count(new QueryWrapper<PackTechSpec>().eq("pack_type", "packBigGoods").eq("foreign_id", style.getId()).eq("spec_type", "外辅工艺"));
//            biColorway.setC8ColorwayIsOutsource(count > 0 ? "是" : "否");
//            String sizeIds = style.getSizeIds();
//            if (sizeIds != null) {
//                List<BasicsdatumSize> basicsdatumSizes = basicsdatumSizeService.listByIds(Arrays.asList(sizeIds.split(",")));
//                for (BasicsdatumSize basicsdatumSize : basicsdatumSizes) {
//                    BiColorway newbiColorway=new BiColorway();
//                    BeanUtil.copyProperties(biColorway,newbiColorway);
//                    //默认条形码  (默认条形码=配色编码+颜色编码+尺码代码)
//                    String wareCode="";
//                    if (StringUtils.isNotEmpty(styleColorVo.getWareCode())){
//                        wareCode=styleColorVo.getWareCode();
//                    }
//                    String colorCode="";
//                    if (StringUtils.isNotEmpty(styleColorVo.getColorCode())){
//                        colorCode= styleColorVo.getColorCode();
//                    }
//
//                    String code="";
//                    if (StringUtils.isNotEmpty(basicsdatumSize.getCode())){
//                        code = basicsdatumSize.getCode();
//                    }
//
//                    newbiColorway.setC8ColorwayDefaultBarcode(wareCode+colorCode+code);
//                    list.add(newbiColorway);
//                }
//            }
//        }
//
//        this.remove(null);
//        this.saveBatch(list,100);
//    }
//}
