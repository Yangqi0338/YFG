package com.base.sbc.open.dto;

import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.entity.StyleInfoSku;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/24 10:05
 */
@Data
public class OpenStyleDto {

    private String id;

    public void init(Style s){
        this.id = s.getId();
        this.Operator = s.getUpdateName();
        this.code = s.getDesignNo();
        this.name = s.getStyleName();
        this.ctg = s.getProdCategory3rd();
        this.year = s.getYear();
        this.season = s.getSeason();
        this.band = this.year+this.season+s.getBandCode();
//        this.band = s.getBandCode();
        this.sizeGroup = s.getSizeRange();
        this.designer = s.getDesigner();
        this.brand = s.getSeriesId();
        this.ProfileInfo = s.getSilhouette();
        this.sex = s.getSexName();
        this.Source = s.getDesignChannelId();
        this.unit = s.getStyleUnitCode();
        this.imgUrl = s.getStylePic();
//        this.standardRule;//执行标准
//        this.safeLevel;//安全等级
        this.StyleInfo = s.getStyleFlavourName();
        this.isDisable = false;
        this.isPush = true;
//        this.smCode = s.getDesignNo();
        this.BigCtg = s.getProdCategory();
        this.SaleInfo = s.getSellingPoint();
//        private String Composition;//成分
        this.checkExtinfoStatuz = true;
        this.remark = s.getRemarks();

        //扩展字段
        this.pdtEx = new PdteEx();
        this.pdtEx.initPdteEx(s);
        this.Att01 = this.pdtEx.getAtt01();
        this.Att02 = this.pdtEx.getAtt02();
        this.Att03 = this.pdtEx.getAtt03();
        this.Att04 = this.pdtEx.getAtt04();
        this.Att05 = this.pdtEx.getAtt05();
        this.Att06 = this.pdtEx.getAtt06();
        this.Att07 = this.pdtEx.getAtt07();
        this.Att08 = this.pdtEx.getAtt08();
        this.Att09 = this.pdtEx.getAtt09();
    }


    private String Operator;//操作人
    private String code;//款式编码
    private String name;//款式名称
    private String ctg;//末级类目
    private String year;//年份
    private String season;//季节
    private String band;//波段
    private String sizeGroup;//尺码组
    private String designer;//设计师
    private String listDate;//上市日期
    private String brand;//品牌
    private String ProfileInfo;//廓型(编码)
    private String sex;//性别
    private String Source;//来源
    private String spCode;//供应商
    private String unit;//单位（编码）
    private String imgUrl;//款式主图地址
    private String standardRule;//执行标准
    private String safeLevel;//安全等级
    private String StyleInfo;//风格
    private boolean isDisable;//是否冻结
    private boolean isPush;//是否推送
//    private String OtherImgs;//款式其他图片(不含主图)---暂不传
    private String srcUrl;//链接地址
    private String smCode;//样衣编号
    private String BigCtg;//一级类目
    private BigDecimal price;//吊牌价
    private BigDecimal costPrice;//成本价
    private BigDecimal retailPrice;//零售价
    private BigDecimal financePrice;//财务成本价
    private String Tags;//标签
    private String SaleInfo;//卖点
    private String Composition;//成分
    private String remark;//备注
    private boolean checkExtinfoStatuz;//是否验证吊牌


    private String Att01;//是否授权款
    private String Att02;//日销价
    private String Att03;//针梭织
    private String Att04;//产品线
    private String Att05;//二级类目
    private String Att06;//三级类目
    private String Att07;//四级类目
    private String Att08;//款式类型
    private String Att09;//渠道类型

    private PdteEx pdtEx;//款式主表扩展表
    private List<Sku> skus;
    private List<Skc> skcs;

    private Map<String,String> sizeMap;//临时使用的尺码数据

    /**
     * 吊牌列表
     * @param hangTag
     */
    public void setHangTag(HangTag hangTag) {
        this.standardRule = hangTag.getExecuteStandard();//执行标准
        this.safeLevel = hangTag.getSaftyType();//安全等级
//        this.Composition = hangTag.getComposition;//成分
    }

    /**
     * 款式主表扩展表
     */
    @Data
    class PdteEx{
        private void initPdteEx(Style s){
            this.Att03 = s.getNeedleWeaving();
            //设计品类
            if (StringUtils.isNotBlank(s.getDesignCategoryName())){
                String[] split = s.getDesignCategoryName().split(" / ");
                if (split.length > 0){
                    this.Att04 = split[0];
                    if (split.length > 1){
                        this.Att05 = split[1];
                    }
                    if (split.length > 2){
                        this.Att06 = split[2];
                    }
                    if (split.length > 3){
                        this.Att07 = split[3];
                    }
                }
            }
            this.Att08 = s.getStyleTypeName();
            this.Att09 = s.getChannelName();
        }

        private String Att01;//是否授权款
        private String Att02;//日销价
        private String Att03;//针梭织
        private String Att04;//产品线
        private String Att05;//二级类目
        private String Att06;//三级类目
        private String Att07;//四级类目
        private String Att08;//款式类型
        private String Att09;//渠道类型
        private String Att10;//扩展表字段10
        private String Att11;//扩展表字段11
        private String Att12;//扩展表字段12
        private String Att13;//扩展表字段13
        private String Att14;//扩展表字段14
        private String Att15;//扩展表字段15
        private BigDecimal Num01;//扩展表字段1
        private BigDecimal Num02;//扩展表字段2
        private BigDecimal Num03;//扩展表字段3
        private BigDecimal Num04;//扩展表字段4
        private BigDecimal Num05;//扩展表字段5
        private BigDecimal Num06;//扩展表字段6
        private BigDecimal Num07;//扩展表字段7
        private BigDecimal Num08;//扩展表字段8
        private BigDecimal Num09;//扩展表字段9
        private BigDecimal Num10;//扩展表字段10
        private String extra;
    }

    /**
     * 初始化sku
     * @param skuList
     */
    public void initSku(List<StyleInfoSku> skuList,String code){
        List<Sku> skus = new ArrayList<>();
        for (StyleInfoSku sku : skuList) {
            Sku skuDto = new Sku();
            skuDto.init(sku,code,this.sizeMap);
            skus.add(skuDto);
        }
        this.skus = skus;
    }
    /**
     * sku
     */
    @Data
    class Sku{
        private String skuCode;//Sku编码
        private String sizeCode;//尺码编码
        private String sizeName;//尺码名称
        private String colorCode;//颜色编码
        private String colorName;//颜色名称
        private BigDecimal CostPrice;//成本价
        private BigDecimal TagPrice;//吊牌价
        private String GramWeight;//克重
        private String gbCode;//国标码
        private BigDecimal ZERO = new BigDecimal(0);

        public void init(StyleInfoSku sku, String code, Map<String, String> sizeMap) {
//            this.skuCode = sku.getSkuCode();
            this.skuCode = code;
            if (StringUtils.isNotBlank(sku.getColorCode())){
                this.colorCode = sku.getColorCode();
                this.skuCode += sku.getColorCode();
            }
            if (sizeMap != null && StringUtils.isNotBlank(sizeMap.get(sku.getSizeName()))){
                this.sizeCode = sizeMap.get(sku.getSizeName());
                this.skuCode += sizeMap.get(sku.getSizeName());
//                this.sizeCode = sku.getSizeName();
//                this.skuCode += sku.getSizeName();
            }
//            if (StringUtils.isNotBlank(sku.getSizeCode())){
//                this.sizeCode = sku.getSizeCode();
//                this.skuCode += sku.getSizeCode();
//            }
            this.colorName = sku.getColorName();
            this.sizeName = sku.getSizeName();
            if (sku.getCostPrice() == null){
                this.CostPrice = ZERO;
            }else{
                this.CostPrice = sku.getCostPrice();
            }
            if (sku.getTagPrice() == null){
                this.TagPrice = ZERO;
            }else{
                this.TagPrice = sku.getTagPrice();
            }
        }
    }

    /**
     * 初始化skc
     * @param skcList
     */
    public void initSkc(List<StyleInfoColor> skcList) {
        List<Skc> skcs = new ArrayList<>();
//        BigDecimal zero = new BigDecimal(0);
//        Skc skc1 = new Skc();
//        Skc skc2 = new Skc();
//        Skc skc3 = new Skc();
//        Skc skc4 = new Skc();
//        skc1.setColorCode("JH");
//        skc1.setImgUrl(" ");
//        skc1.setPrice(zero);
//        skc1.setCostprice(zero);
//        skc1.setReatilprice(zero);
//        skc1.setIsDisable(false);
//
//        skc2.setColorCode("00");
//        skc2.setImgUrl(" ");
//        skc2.setPrice(zero);
//        skc2.setCostprice(zero);
//        skc2.setReatilprice(zero);
//        skc2.setIsDisable(false);
//
//        skc3.setColorCode("JJ");
//        skc3.setImgUrl(" ");
//        skc3.setPrice(zero);
//        skc3.setCostprice(zero);
//        skc3.setReatilprice(zero);
//        skc3.setIsDisable(false);
//
//        skc4.setColorCode("01");
//        skc4.setImgUrl(" ");
//        skc4.setPrice(zero);
//        skc4.setCostprice(zero);
//        skc4.setReatilprice(zero);
//        skc4.setIsDisable(false);
//
//        skcs.add(skc1);
//        skcs.add(skc2);
//        skcs.add(skc3);
//        skcs.add(skc4);
        for (StyleInfoColor color : skcList) {
            Skc skc = new Skc();
            skc.init(color);
            skcs.add(skc);
        }
        this.skcs = skcs;
    }
    /**
     * skc
     */
    @Data
    class Skc{

        public void init(StyleInfoColor color){
            this.colorCode = color.getColorCode();
            this.ImgUrl = color.getImages();
            if (color.getTagPrice() != null){
                this.price = color.getTagPrice();

            }else{
                this.price = ZERO;
            }
            if (color.getSkcCostPrice() != null){
                this.costprice = color.getSkcCostPrice();
            }else{
                this.costprice = ZERO;
            }
            this.reatilprice = ZERO;
            this.IsDisable = false;
        }

        private BigDecimal ZERO = new BigDecimal(0);

        private String colorCode;//颜色编码
        private String ImgUrl;//图片地址
        private BigDecimal price;//吊牌价
        private BigDecimal costprice;//成本价
        private BigDecimal reatilprice;//零售价
        private boolean IsDisable;//是否冻结，默认为false
        private String Remark;//备注
        private PdtSkcEx pdtSkcEx;//

        /**
         * skc扩展表
         */
        @Data
        class PdtSkcEx{
            private String Att01;//扩展表字段01
            private String Att02;//扩展表字段02
            private String Att03;//扩展表字段03
            private String Att04;//扩展表字段04
            private String Att05;//扩展表字段05
            private String Att06;//扩展表字段06
            private String Att07;//扩展表字段07
            private String Att08;//扩展表字段08
            private String Att09;//扩展表字段09
            private String Att10;//扩展表字段10
            private BigDecimal Num01;//扩展表字段1
            private BigDecimal Num02;//扩展表字段2
            private BigDecimal Num03;//扩展表字段3
            private BigDecimal Num04;//扩展表字段4
            private BigDecimal Num05;//扩展表字段5
        }
    }
}
