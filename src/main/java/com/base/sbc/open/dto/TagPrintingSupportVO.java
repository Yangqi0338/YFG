package com.base.sbc.open.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
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
        return super.getIsGift();
    }

    @Override
    @JsonIgnore
    public Boolean getMerchApproved() {
        return super.getMerchApproved();
    }

    @Override
    @JsonIgnore
    public Boolean getApproved() {
        return super.getApproved();
    }

    @Override
    @JsonIgnore
    public Boolean getTechApproved() {
        return super.getTechApproved();
    }

    @Override
    @JsonIgnore
    public BigDecimal getC8_Colorway_SalesPrice() {
        return super.getC8_Colorway_SalesPrice();
    }

    @JsonIgnore
    private final Map<String, CodeMapping<?>> codeMap = MapUtil.ofEntries(
            MapUtil.entry("DP02", new CodeMapping<>("SaftyTitle")),
            MapUtil.entry("DP03", new CodeMapping<>("OPStandardTitle")),
            MapUtil.entry("DP04", new CodeMapping<>("StyleTitle")),
            MapUtil.entry("DP05", new CodeMapping<>("ProductTitle", MoreLanguageTagPrinting::getProductName, MoreLanguageTagPrinting::setProductName)),

            MapUtil.entry("DP06", new CodeMapping<>("SizeTitle", Size::getSIZECODE, Size::setEuropeCode).setListFunc(MoreLanguageTagPrinting::getSize)),

            MapUtil.entry("DP07", new CodeMapping<>("colorTitle", MoreLanguageTagPrinting::getColorCode, MoreLanguageTagPrinting::setColorDescription)),
            MapUtil.entry("DP09,DP11,DP10,DP13", new CodeMapping<>("CompositionTitle", MoreLanguageTagPrinting::getComposition, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM01", new CodeMapping<>("AttentionTitle", MoreLanguageTagPrinting::getAttention, MoreLanguageTagPrinting::setAttention)),
            MapUtil.entry("XM06", new CodeMapping<>("CareSymbolTitle")),
            MapUtil.entry("XM07", new CodeMapping<>("C8_APPBOM_StorageTitle", MoreLanguageTagPrinting::getC8_APPBOM_StorageReq, MoreLanguageTagPrinting::setC8_APPBOM_StorageReq))
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

        private Pair<Function<K, String>, BiConsumer<K, String>> mapping;

        public CodeMapping<K> setListFunc(Function<MoreLanguageTagPrinting, List<K>> listFunc) {
            this.listFunc = listFunc;
            return this;
        }

        public CodeMapping(String titleCode) {
            this.titleCode = titleCode;
        }

        public CodeMapping(String titleCode, Function<K, String> key, BiConsumer<K, String> value) {
            this.mapping = Pair.of(key,value);
            this.titleCode = titleCode;
        }
    }

}
