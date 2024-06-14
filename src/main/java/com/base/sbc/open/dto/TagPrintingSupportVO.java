package com.base.sbc.open.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagPrintingSupportVO extends TagPrinting {
    @Override
    @JsonIgnore
    public Boolean getIsGift() {
        return BooleanUtil.isTrue(super.getIsGift());
    }

    @Override
    @JsonIgnore
    public Boolean getMerchApproved() {
        return BooleanUtil.isTrue(super.getMerchApproved());
    }

    @Override
    @JsonIgnore
    public Boolean getApproved() {
        return BooleanUtil.isTrue(super.getApproved());
    }

    @Override
    @JsonIgnore
    public Boolean getTechApproved() {
        return BooleanUtil.isTrue(super.getTechApproved());
    }

    @Override
    @JsonIgnore
    public String getC8_Colorway_WareCode() {
        return super.getC8_Colorway_WareCode();
    }

    @Override
    @JsonIgnore
    public String getStyleCode() {
        return super.getStyleCode();
    }

    @Override
    @JsonIgnore
    public String getC8_Colorway_BatchNo() {
        return super.getC8_Colorway_BatchNo();
    }

    @Override
    @JsonIgnore
    public String getSecCode() {
        return super.getSecCode();
    }

    @Override
    @JsonIgnore
    public String getMainCode() {
        return super.getMainCode();
    }

    @Override
    @JsonIgnore
    public Boolean getIsAccessories() {
        return BooleanUtil.isTrue(super.getIsAccessories());
    }

    @Override
    @JsonIgnore
    public String getC8_Season_Brand() {
        return super.getC8_Season_Brand();
    }

    @Override
    @JsonIgnore
    public String getCareSymbols() {
        return super.getCareSymbols();
    }

    @JsonIgnore
    private final Map<String, CodeMapping<?>> codeMap = MapUtil.ofEntries(
            MapUtil.entry("DP02", new CodeMapping<>("SaftyTitle-安全技术类别")),
            MapUtil.entry("DP03", new CodeMapping<>("OPStandardTitle-执行标准")),
            MapUtil.entry("DP04", new CodeMapping<>("StyleTitle-款号")),
            MapUtil.entry("DP05", new CodeMapping<>("ProductTitle-品名", MoreLanguageTagPrinting::getProductName, MoreLanguageTagPrinting::setProductName)),
            MapUtil.entry("DP06", new CodeMapping<>("SizeTitle-尺码", Size::getSystemSizeName, Size::setSIZENAME).setListFunc(MoreLanguageTagPrinting::getSize)),
            MapUtil.entry("DP07", new CodeMapping<>("colorTitle-颜色", MoreLanguageTagPrinting::getColorDescription, MoreLanguageTagPrinting::setColorDescription)),
            MapUtil.entry("DP09", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("DP10", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("DP11", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("DP12", new CodeMapping<>("FillingTitle-充绒量")),
            MapUtil.entry("DP13", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("DP16", new CodeMapping<>("CompositionTitle-成分信息")),

            MapUtil.entry("XM01", new CodeMapping<>("AttentionTitle-温馨提示", MoreLanguageTagPrinting::getAttention, MoreLanguageTagPrinting::setAttention)),
            MapUtil.entry("XM02", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM03", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM04", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM11", new CodeMapping<>("CompositionTitle-成分信息", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM10", new CodeMapping<>("CompositionTitle-成分信息")),
            MapUtil.entry("XM06", new CodeMapping<>("CareSymbolTitle-洗标")),
            MapUtil.entry("XM07", new CodeMapping<>("C8_APPBOM_StorageTitle-贮藏要求", MoreLanguageTagPrinting::getC8_APPBOM_StorageReq, MoreLanguageTagPrinting::setC8_APPBOM_StorageReq)),
            MapUtil.entry("XM08", new CodeMapping<>("SizeTitle-尺码", Size::getSystemSizeName, Size::setSIZENAME).setListFunc(MoreLanguageTagPrinting::getSize)),
            MapUtil.entry("XM09", new CodeMapping<>("colorTitle-颜色", MoreLanguageTagPrinting::getColorDescription, MoreLanguageTagPrinting::setColorDescription))
    );

    @JsonIgnore
    public List<? extends TagPrintingSupportVO> getMySelfList(){
        return CollUtil.toList(this);
    }

    @JsonIgnoreType
    @Data
    public static class CodeMapping<K> {

        private Function<MoreLanguageTagPrinting, List<K>> listFunc;

        private String titleCode;
        private String titleName;

        private Pair<Function<K, String>, BiConsumer<K, String>> mapping;

        public CodeMapping<K> setListFunc(Function<MoreLanguageTagPrinting, List<K>> listFunc) {
            this.listFunc = listFunc;
            return this;
        }

        public CodeMapping(String titleCode) {
            String[] split = titleCode.split("-");
            this.titleCode = split[0];
            this.titleName = split.length > 1 ? split[1] : "";
        }

        public CodeMapping(String titleCode, Function<K, String> key, BiConsumer<K, String> value) {
            this.mapping = Pair.of(key,value);
            String[] split = titleCode.split("-");
            this.titleCode = split[0];
            this.titleName = split.length > 1 ? split[1] : "";
        }
    }

}
