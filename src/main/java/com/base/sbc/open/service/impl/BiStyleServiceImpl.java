package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.style.dto.StylePageDto;
import com.base.sbc.module.style.entity.Style;
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

    /**
     * 款式样衣表
     */
    @Override
    public void style() {
        List<BiStyle> list = new ArrayList<>();

        for (Style style : styleService.list()) {
            BiStyle biStyle = new BiStyle();
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

            //市场调研
            biStyle.setCompetitiveStyles(null);
            //Style URL
            biStyle.setC8StylePLMID(null);
            //紧急状态
            biStyle.setC8StyleStatus(null);
            //款式工艺
            biStyle.setC8StyleAttrProductSpecs(null);
            //是否大货
            biStyle.setC8SyncIfProduction(null);
            //是否上会
            biStyle.setC8SyncIfSalesconfernce(null);
            //材质
            biStyle.setC8StyleAttrCaiZhi(null);
            //毛纱针法
            biStyle.setC8StyleAttrYarnNeedle(null);
            //毛纱针型
            biStyle.setC8StyleAttrYarnNeedleType(null);
            //肩宽
            biStyle.setC8StyleAttrJianKuan(null);
            //胸围
            biStyle.setC8StyleAttrXiongWei(null);
            //腰型
            biStyle.setC8StyleAttrYaoXing(null);
            //花型
            biStyle.setC8StyleAttrPrintting(null);
            //衣长(CM)
            biStyle.setC8StyleAttrCoatLength(null);
            //衣长分类
            biStyle.setC8StyleAttrLengthRange(null);
            //袖型
            biStyle.setC8StyleAttrXiuXing(null);
            //袖长
            biStyle.setC8StyleAttrXiuChang(null);
            //门襟
            biStyle.setC8StyleAttrMenJIng(null);
            //领型
            biStyle.setC8StyleAttrLingXing(null);
            //克重
            biStyle.setC8StyleAttrKezhong(null);
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
            //下稿设计师
            biStyle.setC8SADataDesigner(null);
            //下稿设计师,用户登录
            biStyle.setC8SADataDesignerID(null);
            //款式定位
            biStyle.setC8StyleAttrOrientation(null);
            list.add(biStyle);
        }


        this.remove(null);
        this.saveBatch(list, 100);
    }
}
