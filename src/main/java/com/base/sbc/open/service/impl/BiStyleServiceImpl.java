package com.base.sbc.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.open.entity.BiSample;
import com.base.sbc.open.entity.BiStyle;
import com.base.sbc.open.mapper.BiStyleMapper;
import com.base.sbc.open.service.BiStyleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/24 16:31:00
 * @mail 247967116@qq.com
 */
@Service
public class BiStyleServiceImpl extends ServiceImpl<BiStyleMapper,BiStyle> implements BiStyleService {
    /**
     * 款式样衣表
     */
    @Override
    public void style() {
        List<BiStyle> list = new ArrayList<>();
        BiStyle biStyle = new BiStyle();
        //季节,品牌
        biStyle.setC8SeasonBrand(null);
        //季节,年度
        biStyle.setC8SeasonYear(null);
        //季节,季节
        biStyle.setC8SeasonQuarter(null);
        //设计款号
        biStyle.setStyleCode(null);
        //启用
        biStyle.setStyleActive(null);
        //大类
        biStyle.setStyle1stCategory(null);
        //品类
        biStyle.setC8StyleProdCategory(null);
        //中类
        biStyle.setC8Style2ndCategory(null);
        //小类
        biStyle.setC8Style3rdCategory(null);
        //样品数
        biStyle.setC8StyleCntSample(null);
        //模式
        biStyle.setDevelopmentMode(null);
        //款式类型
        biStyle.setProductType(null);
        //生产类型
        biStyle.setDevelopmentType(null);
        //紧急状态
        biStyle.setC8StyleStatus(null);
        //款式名称
        biStyle.setStyleName(null);
        //廓形
        biStyle.setC8StylerKuoXing(null);
        //设计师
        biStyle.setC8StyleAttrDesigner(null);
        //设计师,用户登录
        biStyle.setC8StyleAttrDesignerID(null);
        //跟款设计师
        biStyle.setC8StyleAttrMerchDesigner(null);
        //跟款设计师,用户登录
        biStyle.setC8StyleAttrMerchDesignerID(null);
        //工艺员
        biStyle.setC8StyleAttrTechnician(null);
        //工艺员,用户登录
        biStyle.setC8StyleAttrTechnicianID(null);
        //版师
        biStyle.setC8StyleAttrPatternMaker(null);
        //版师,用户登录
        biStyle.setC8StyleAttrPatternMakerID(null);
        //材料专员
        biStyle.setC8StyleAttrFabDevelope(null);
        //材料专员,用户登录
        biStyle.setC8StyleAttrFabDevelopeID(null);
        //实际出稿时间
        biStyle.setC8StyleAttrActualDesignTime(null);
        //单位
        biStyle.setC8StyleAttrUOM(null);
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
        //开发分类
        biStyle.setC8StyleAttrDevClass(null);
        //款式工艺
        biStyle.setC8StyleAttrProductSpecs(null);
        //是否大货
        biStyle.setC8SyncIfProduction(null);
        //是否上会
        biStyle.setC8SyncIfSalesconfernce(null);
        //打版难度
        biStyle.setC8StylePatDiff(null);
        //市场调研
        biStyle.setCompetitiveStyles(null);
        //Style URL
        biStyle.setC8StylePLMID(null);
        //创建时间
        biStyle.setCreatedAt(null);
        //创建人
        biStyle.setCreatedBy(null);
        //修改时间
        biStyle.setModifiedAt(null);
        //修改人
        biStyle.setModifiedBy(null);
        //复制自
        biStyle.setCopiedFrom(null);
        //改款设计师
        biStyle.setC8SARevisedDesigner(null);
        //改款设计师,用户登录
        biStyle.setC8SARevisedDesignerID(null);
        //审版设计师
        biStyle.setC8SAReviewedDesigner(null);
        //审版设计师,用户登录
        biStyle.setC8SAReviewedDesignerID(null);
        //下稿设计师
        biStyle.setC8SADataDesigner(null);
        //下稿设计师,用户登录
        biStyle.setC8SADataDesignerID(null);
        //款式来源
        biStyle.setC8StyleOrigin(null);
        //款式定位
        biStyle.setC8StyleAttrOrientation(null);
        //款式风格
        biStyle.setC8StyleAttrFlavour(null);





        list.add(biStyle);




        this.remove(null);
        this.saveBatch(list,100);
    }
}
