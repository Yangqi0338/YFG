//package com.base.sbc.open.service.impl;
//
//import com.alibaba.nacos.client.naming.utils.CollectionUtils;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.base.sbc.config.common.BaseQueryWrapper;
//import com.base.sbc.config.utils.StringUtils;
//import com.base.sbc.module.formtype.vo.FieldManagementVo;
//import com.base.sbc.module.pack.entity.PackBom;
//import com.base.sbc.module.pack.entity.PackBomVersion;
//import com.base.sbc.module.pack.entity.PackInfo;
//import com.base.sbc.module.pack.service.PackBomService;
//import com.base.sbc.module.pack.service.PackBomVersionService;
//import com.base.sbc.module.pack.service.PackInfoService;
//import com.base.sbc.module.patternmaking.entity.PatternMaking;
//import com.base.sbc.module.patternmaking.service.PatternMakingService;
//import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
//import com.base.sbc.module.pricing.service.StylePricingService;
//import com.base.sbc.module.pricing.vo.StylePricingVO;
//import com.base.sbc.module.sample.entity.PreProductionSampleTask;
//import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
//import com.base.sbc.module.style.entity.Style;
//import com.base.sbc.module.style.entity.StyleColor;
//import com.base.sbc.module.style.service.StyleColorService;
//import com.base.sbc.module.style.service.StyleService;
//import com.base.sbc.open.entity.BiSample;
//import com.base.sbc.open.mapper.BiSampleMapper;
//import com.base.sbc.open.service.BiSampleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author 卞康
// * @date 2023/8/24 16:26:28
// * @mail 247967116@qq.com
// */
//@Service
//@RequiredArgsConstructor
//public class BiSampleServiceImpl extends ServiceImpl<BiSampleMapper, BiSample> implements BiSampleService {
//
//    private final PackInfoService packInfoService;
//    private final StylePricingService stylePricingService;
//    private final StyleColorService styleColorService;
//    private final PackBomService packBomService;
//    private final PackBomVersionService packBomVersionService;
//    private final StyleService styleService;
//    private final PreProductionSampleTaskService preProductionSampleTaskService;
//    private final PatternMakingService patternMakingService;
//
//    /**
//     * 款式设计
//     */
//    @Override
//    public void sample() {
//        List<BiSample> list = new ArrayList<>();
//        for (Style style : styleService.list()) {
//            BiSample biSample = new BiSample();
//            List<PackInfo> packInfos = packInfoService.list(new QueryWrapper<PackInfo>().eq("foreign_id", style.getId()));
//            for (PackInfo packInfo : packInfos) {
//                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), null);
//                if (stylePricingVO != null) {
//                    PackBomVersion packBomVersion = packBomVersionService.getEnableVersion(packInfo.getId(), "packBigGoods");
//                    if (packBomVersion != null) {
//                        List<PackBom> packBomList = packBomService.list(new BaseQueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("pack_type", "packBigGoods").eq("bom_version_id", packBomVersion.getId()));
//                        for (PackBom packBom : packBomList) {
//                            //克重
//                            biSample.setC8StyleAttrKeZhong(packBom.getGramWeight());
//                        }
//                    }
//                    //是否上会
//                    biSample.setC8SyncIfSalesconfernce(stylePricingVO.getMeetFlag());
//
//
//                    StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", packInfo.getStyleNo()));
//
//                    if (styleColor != null) {
//                        //是否大货
//                        biSample.setC8SyncIfProduction(StringUtils.isEmpty(styleColor.getStyleNo()) ? "否" : "是");
//                        //下稿设计师
//                        biSample.setC8SaDataDesigner(styleColor.getSenderDesignerName());
//                        //下稿设计师,用户登录
//                        biSample.setC8SaDataDesignerId(styleColor.getSenderDesignerId());
//                    }
//                    //紧急状态
//                    biSample.setC8StyleStatus(style.getTaskLevelName());
//
//                    //季节,品牌
//                    biSample.setC8SeasonBrand(style.getBrandName());
//                    //季节,年度
//                    biSample.setC8SeasonYear(style.getYearName());
//                    //季节,季节
//                    biSample.setC8SeasonQuarter(style.getSeasonName());
//                    //设计款号
//                    biSample.setStyleCode(style.getDesignNo());
//                    //启用
//                    biSample.setStyleActive(true);
//                    //大类
//                    biSample.setStyle1stCategory(style.getProdCategory1stName());
//                    //品类
//                    biSample.setC8StyleProdCategory(style.getProdCategoryName());
//                    //中类
//                    biSample.setC8Style2ndCategory(style.getProdCategory2ndName());
//                    //小类
//                    biSample.setC8Style3rdCategory(style.getProdCategory3rdName());
//                    //款式类型
//                    biSample.setProductType(style.getStyleTypeName());
//                    //生产类型
//                    biSample.setDevelopmentType(style.getDevtTypeName());
//                    //款式名称
//                    biSample.setStyleName(style.getStyleName());
//                    //廓形
//                    biSample.setC8StylerKuoXing(style.getSilhouetteName());
//                    //设计师
//                    biSample.setC8StyleAttrDesigner(style.getDesigner());
//                    //设计师,用户登录
//                    biSample.setC8StyleAttrDesignerId(style.getDesignerId());
//                    //跟款设计师
//                    biSample.setC8StyleAttrMerchDesigner(style.getMerchDesignName());
//                    //跟款设计师,用户登录
//                    biSample.setC8StyleAttrMerchDesignerId(style.getMerchDesignId());
//                    //工艺员
//                    biSample.setC8StyleAttrTechnician(style.getTechnicianName());
//                    //工艺员,用户登录
//                    biSample.setC8StyleAttrTechnicianId(style.getTechnicianId());
//                    //版师
//                    biSample.setC8StyleAttrPatternMaker(style.getPatternDesignName());
//                    //版师,用户登录
//                    biSample.setC8StyleAttrPatternMakerId(style.getPatternDesignId());
//                    //材料专员
//                    biSample.setC8StyleAttrFabDevelope(style.getFabDevelopeName());
//                    //材料专员,用户登录
//                    biSample.setC8StyleAttrFabDevelopeId(style.getFabDevelopeId());
//                    //实际出稿时间
//                    biSample.setC8StyleAttrActualDesignTime(style.getActualPublicationDate());
//                    //单位
//                    biSample.setC8StyleAttrUom(style.getStyleUnit());
//                    //开发分类
//                    biSample.setC8StyleAttrDevClass(style.getDevClassName());
//                    //打版难度
//                    biSample.setC8StylePatDiff(style.getPatDiffName());
//                    //创建时间
//                    biSample.setCreatedAt(style.getCreateDate());
//                    //创建人
//                    biSample.setCreatedBy(style.getCreateName());
//                    //修改时间
//                    biSample.setModifiedAt(style.getUpdateDate());
//                    //修改人
//                    biSample.setModifiedBy(style.getUpdateName());
//                    //审版设计师
//                    biSample.setC8SaReviewedDesigner(style.getReviewedDesignName());
//                    //审版设计师,用户登录
//                    biSample.setC8SaReviewedDesignerId(style.getReviewedDesignId());
//                    //款式来源
//                    biSample.setC8StyleOrigin(style.getStyleOriginName());
//                    //款式风格
//                    biSample.setC8StyleAttrFlavour(style.getStyleFlavourName());
//
//
//                    //动态字段
//
//                    List<FieldManagementVo> fieldManagementVoList = styleService.queryDimensionLabelsByStyle(new DimensionLabelsSearchDto(style.getId()));
//                    if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
//                        fieldManagementVoList.forEach(m -> {
//                            if ("衣长分类".equals(m.getFieldName())) {
//                                biSample.setC8StyleAttrLengthRange(m.getVal());
//                            }
//                            if ("衣长".equals(m.getFieldName())) {
//                                biSample.setC8StyleAttrCoatLength(m.getVal());
//                            }
//                            if ("腰型".equals(m.getFieldName())) {
//                                //腰型
//                                biSample.setC8StyleAttrYaoXing(m.getVal());
//                            }
//                            if ("袖长".equals(m.getFieldName())) {
//                                //袖长
//                                biSample.setC8StyleAttrXiuChang(m.getVal());
//                            }
//                            if ("袖型".equals(m.getFieldName())) {
//                                //袖型
//                                biSample.setC8StyleAttrXiuXing(m.getVal());
//                            }
//                            if ("胸围".equals(m.getFieldName())) {
//
//                                //胸围
//                                biSample.setC8StyleAttrXiongWei(m.getVal());
//                            }
//                            if ("门襟".equals(m.getFieldName())) {
//
//                                //门襟
//                                biSample.setC8StyleAttrMenJIng(m.getVal());
//                            }
//                            if ("毛纱针型".equals(m.getFieldName())) {
//                                //毛纱针型
//                                biSample.setC8StyleAttrYarnNeedleType(m.getVal());
//                            }
//                            if ("毛纱针法".equals(m.getFieldName())) {
//                                //毛纱针法
//                                biSample.setC8StyleAttrYarnNeedle(m.getVal());
//                            }
//                            if ("廓形".equals(m.getFieldName())) {
//                                //smpGoodsDto.setProfileId(m.getVal());
//                                //smpGoodsDto.setProfileName(m.getValName());
//                            }
//                            if ("花型".equals(m.getFieldName())) {
//                                //花型
//                                biSample.setC8StyleAttrPrintting(m.getVal());
//                            }
//                            if ("领型".equals(m.getFieldName())) {
//                                //领型
//                                biSample.setC8StyleAttrLingXing(m.getVal());
//                            }
//                            if ("材质".equals(m.getFieldName())) {
//                                //材质
//                                biSample.setC8StyleAttrCaiZhi(m.getVal());
//                            }
//                            if ("肩宽".equals(m.getFieldName())) {
//                                //肩宽
//                                biSample.setC8StyleAttrJianKuan(m.getVal());
//                            }
//                        });
//
//                        //Style URL
//                        biSample.setC8StylePLMID(style.getId());
//                        //款式工艺
//                        biSample.setC8StyleAttrProductSpecs(null);
//                        long i = patternMakingService.count(new QueryWrapper<PatternMaking>().eq("style_id", style.getId()));
//                        PreProductionSampleTask preProductionSampleTask = preProductionSampleTaskService.getOne(new QueryWrapper<PreProductionSampleTask>().eq("style_id", style.getId()).orderByDesc("create_date").last("limit 1"));
//                        if (preProductionSampleTask != null) {
//                            i = i + 1;
//                        }
//                        //样品数
//                        biSample.setC8StyleCntSample(String.valueOf(i));
//
//                        //改款设计师
//                        biSample.setC8SaRevisedDesigner(style.getRevisedDesignName());
//                        //改款设计师,用户登录
//                        biSample.setC8SaRevisedDesignerId(style.getRevisedDesignId());
//                        //款式定位
//                        biSample.setC8StyleAttrOrientation(style.getPositioningName());
//
//
//                        //模式
//                        biSample.setDevelopmentMode(null);
//                        //复制自
//                        biSample.setCopiedFrom(null);
//                        //市场调研   废弃
//                        biSample.setCompetitiveStyles(null);
//
//
//                    }
//                }
//                list.add(biSample);
//            }
//
//        }
//
//        this.remove(null);
//        this.saveBatch(list, 100);
//    }
//}
