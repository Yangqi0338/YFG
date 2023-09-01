package com.base.sbc.open.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.style.dto.StylePageDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.entity.BiStyle;
import com.base.sbc.open.mapper.BiStyleMapper;
import com.base.sbc.open.service.BiStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/24 16:31:00
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class BiStyleServiceImpl extends ServiceImpl<BiStyleMapper, BiStyle> implements BiStyleService {
    private final StyleService styleService;
    private final PackInfoService packInfoService;
    private final StylePricingService stylePricingService;
    private final StyleColorService styleColorService;
    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;

    /**
     * 款式样衣表
     */
    @Override
    public void style() {
        List<BiStyle> list = new ArrayList<>();

        for (Style style : styleService.list()) {
            PackInfo packInfo = packInfoService.getOne(new QueryWrapper<PackInfo>().eq("style_no", style.getStyleNo()));
            if (packInfo != null) {
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), null);
                if (stylePricingVO != null) {
                    PackBomVersion packBomVersion = packBomVersionService.getEnableVersion(packInfo.getId(), "packBigGoods");
                    List<PackBom> packBomList = packBomService.list(new BaseQueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("pack_type", "packBigGoods").eq("bom_version_id", packBomVersion.getId()));
                    for (PackBom packBom : packBomList) {

                        BiStyle biStyle = new BiStyle();
                        //是否上会
                        biStyle.setC8SyncIfSalesconfernce(stylePricingVO.getMeetFlag());
                        //克重
                        biStyle.setC8StyleAttrKezhong(packBom.getGramWeight());

                        StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_id", style.getId()));

                        if (styleColor != null) {
                            //是否大货
                            biStyle.setC8SyncIfProduction(StringUtils.isEmpty(styleColor.getStyleNo()) ? "否" : "是");
                            //下稿设计师
                            biStyle.setC8SADataDesigner(styleColor.getSenderDesignerName());
                            //下稿设计师,用户登录
                            biStyle.setC8SADataDesignerID(styleColor.getSenderDesignerId());
                        }
                        //紧急状态
                        biStyle.setC8StyleStatus(style.getTaskLevelName());

                        //季节,品牌
                        biStyle.setC8SeasonBrand(style.getBrandName());
                        //季节,年度
                        biStyle.setC8SeasonYear(style.getYearName());
                        //季节,季节
                        biStyle.setC8SeasonQuarter(style.getSeasonName());
                        //设计款号
                        biStyle.setStyleCode(style.getDesignNo());
                        //启用
                        biStyle.setStyleActive(true);
                        //大类
                        biStyle.setStyle1stCategory(style.getProdCategory1stName());
                        //品类
                        biStyle.setC8StyleProdCategory(style.getProdCategoryName());
                        //中类
                        biStyle.setC8Style2ndCategory(style.getProdCategory2ndName());
                        //小类
                        biStyle.setC8Style3rdCategory(style.getProdCategory3rdName());
                        //款式类型
                        biStyle.setProductType(style.getStyleTypeName());
                        //生产类型
                        biStyle.setDevelopmentType(style.getDevtTypeName());
                        //款式名称
                        biStyle.setStyleName(style.getStyleName());
                        //廓形
                        biStyle.setC8StylerKuoXing(style.getSilhouetteName());
                        //设计师
                        biStyle.setC8StyleAttrDesigner(style.getDesigner());
                        //设计师,用户登录
                        biStyle.setC8StyleAttrDesignerID(style.getDesignerId());
                        //跟款设计师
                        biStyle.setC8StyleAttrMerchDesigner(style.getMerchDesignName());
                        //跟款设计师,用户登录
                        biStyle.setC8StyleAttrMerchDesignerID(style.getMerchDesignId());
                        //工艺员
                        biStyle.setC8StyleAttrTechnician(style.getTechnicianName());
                        //工艺员,用户登录
                        biStyle.setC8StyleAttrTechnicianID(style.getTechnicianId());
                        //版师
                        biStyle.setC8StyleAttrPatternMaker(style.getPatternDesignName());
                        //版师,用户登录
                        biStyle.setC8StyleAttrPatternMakerID(style.getPatternDesignId());
                        //材料专员
                        biStyle.setC8StyleAttrFabDevelope(style.getFabDevelopeName());
                        //材料专员,用户登录
                        biStyle.setC8StyleAttrFabDevelopeID(style.getFabDevelopeId());
                        //实际出稿时间
                        biStyle.setC8StyleAttrActualDesignTime(style.getActualPublicationDate());
                        //单位
                        biStyle.setC8StyleAttrUOM(style.getStyleUnit());
                        //开发分类
                        biStyle.setC8StyleAttrDevClass(style.getDevClassName());
                        //打版难度
                        biStyle.setC8StylePatDiff(style.getPatDiffName());
                        //创建时间
                        biStyle.setCreatedAt(style.getCreateDate());
                        //创建人
                        biStyle.setCreatedBy(style.getCreateName());
                        //修改时间
                        biStyle.setModifiedAt(style.getUpdateDate());
                        //修改人
                        biStyle.setModifiedBy(style.getUpdateName());
                        //审版设计师
                        biStyle.setC8SAReviewedDesigner(style.getReviewedDesignName());
                        //审版设计师,用户登录
                        biStyle.setC8SAReviewedDesignerID(style.getReviewedDesignId());
                        //款式来源
                        biStyle.setC8StyleOrigin(style.getStyleOriginName());
                        //款式风格
                        biStyle.setC8StyleAttrFlavour(style.getStyleFlavourName());


                        //动态字段

                        List<FieldManagementVo> fieldManagementVoList = styleService.queryDimensionLabelsBySdId(style.getId());
                        if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
                            fieldManagementVoList.forEach(m -> {
                                if ("衣长分类".equals(m.getFieldName())) {
                                    biStyle.setC8StyleAttrLengthRange(m.getVal());
                                }
                                if ("衣长".equals(m.getFieldName())) {
                                    biStyle.setC8StyleAttrCoatLength(m.getVal());
                                }
                                if ("腰型".equals(m.getFieldName())) {
                                    //腰型
                                    biStyle.setC8StyleAttrYaoXing(m.getVal());
                                }
                                if ("袖长".equals(m.getFieldName())) {
                                    //袖长
                                    biStyle.setC8StyleAttrXiuChang(m.getVal());
                                }
                                if ("袖型".equals(m.getFieldName())) {
                                    //袖型
                                    biStyle.setC8StyleAttrXiuXing(m.getVal());
                                }
                                if ("胸围".equals(m.getFieldName())) {

                                    //胸围
                                    biStyle.setC8StyleAttrXiongWei(m.getVal());
                                }
                                if ("门襟".equals(m.getFieldName())) {

                                    //门襟
                                    biStyle.setC8StyleAttrMenJIng(m.getVal());
                                }
                                if ("毛纱针型".equals(m.getFieldName())) {
                                    //毛纱针型
                                    biStyle.setC8StyleAttrYarnNeedleType(m.getVal());
                                }
                                if ("毛纱针法".equals(m.getFieldName())) {
                                    //毛纱针法
                                    biStyle.setC8StyleAttrYarnNeedle(m.getVal());
                                }
                                if ("廓形".equals(m.getFieldName())) {
                                    //smpGoodsDto.setProfileId(m.getVal());
                                    //smpGoodsDto.setProfileName(m.getValName());
                                }
                                if ("花型".equals(m.getFieldName())) {
                                    //花型
                                    biStyle.setC8StyleAttrPrintting(m.getVal());
                                }
                                if ("领型".equals(m.getFieldName())) {
                                    //领型
                                    biStyle.setC8StyleAttrLingXing(m.getVal());
                                }
                                if ("材质".equals(m.getFieldName())) {
                                    //材质
                                    biStyle.setC8StyleAttrCaiZhi(m.getVal());
                                }
                                if ("肩宽".equals(m.getFieldName())) {
                                    //肩宽
                                    biStyle.setC8StyleAttrJianKuan(m.getVal());
                                }
                            });



                            //市场调研
                            biStyle.setCompetitiveStyles(null);
                            //Style URL
                            biStyle.setC8StylePLMID(null);
                            //款式工艺
                            biStyle.setC8StyleAttrProductSpecs(null);
                            //样品数
                            biStyle.setC8StyleCntSample(null);
                            //模式
                            biStyle.setDevelopmentMode(null);
                            //复制自
                            biStyle.setCopiedFrom(null);
                            //改款设计师
                            biStyle.setC8SARevisedDesigner(null);
                            //改款设计师,用户登录
                            biStyle.setC8SARevisedDesignerID(null);
                            //款式定位
                            biStyle.setC8StyleAttrOrientation(null);
                            list.add(biStyle);

                        }
                    }
                }
            }
        }

        this.remove(null);
        this.saveBatch(list, 100);
    }
}
