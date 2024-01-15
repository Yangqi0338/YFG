package com.base.sbc.open.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.access.method.P;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageTagPrinting extends TagPrinting {
    /**
     * 大货款号标题
     */
    private String languageName;

    /**
     * 大货款号标题
     */
    private String StyleTitle;

    /**
     * 成分标题
     */
    private String CompositionTitle;

    /**
     * 洗标标题
     */
    private String CareSymbolTitle;

    /**
     * 品名标题
     */
    private String ProductTitle;

    /**
     * 执行标准
     */
    private String OPStandardTitle;

    /**
     * 温馨提示标题
     */
    private String AttentionTitle;

    /**
     * 安全标题
     */
    private String SaftyTitle;

    /**
     * 贮藏要求
     */
    private String C8_APPBOM_StorageTitle;

    /**
     * 是否赠品
     */
    @JsonIgnore
    private Boolean IsGift;

    /**
     * 商品吊牌价确认
     */
    @JsonIgnore
    private Boolean MerchApproved;

    /**
     * 品控部确认
     */
    @JsonIgnore
    private Boolean Approved;

    /**
     * 后技术确认
     */
    @JsonIgnore
    private Boolean TechApproved;

    /**
     * 吊牌价
     */
    @JsonIgnore
    private BigDecimal C8_Colorway_SalesPrice;

    @JsonIgnore
    private final Map<String, CodeMapping<?, String>> codeMap = MapUtil.ofEntries(
            MapUtil.entry("DP02", new CodeMapping<>(MoreLanguageTagPrinting::setSaftyTitle, MoreLanguageTagPrinting::setSaftyType)),
            MapUtil.entry("DP03", new CodeMapping<>(MoreLanguageTagPrinting::setOPStandardTitle, MoreLanguageTagPrinting::setOPStandard)),
            MapUtil.entry("DP04", new CodeMapping<>(MoreLanguageTagPrinting::setStyleTitle, MoreLanguageTagPrinting::setStyleCode)),
            MapUtil.entry("DP05", new CodeMapping<>(MoreLanguageTagPrinting::setProductTitle, MoreLanguageTagPrinting::setProductName)),
//            MapUtil.entry("DP06", new CodeMapping<>(Size::setSIZENAME, Size::setEuropeCode)),
            MapUtil.entry("DP07", new CodeMapping<>(MoreLanguageTagPrinting::setColorCode, MoreLanguageTagPrinting::setColorDescription)),
            MapUtil.entry("DP09,DP11,DP10,DP12", new CodeMapping<>(MoreLanguageTagPrinting::setCompositionTitle, MoreLanguageTagPrinting::setComposition)),
            MapUtil.entry("XM01", new CodeMapping<>(MoreLanguageTagPrinting::setAttentionTitle, MoreLanguageTagPrinting::setAttention)),
            MapUtil.entry("XM06", new CodeMapping<>(MoreLanguageTagPrinting::setCareSymbolTitle, MoreLanguageTagPrinting::setCareSymbols)),
            MapUtil.entry("XM07", new CodeMapping<>(MoreLanguageTagPrinting::setC8_APPBOM_StorageTitle, MoreLanguageTagPrinting::setC8_APPBOM_StorageReq))
    );

    @JsonIgnore
    private final Map<String, Function<MoreLanguageTagPrinting, List<?>>> codeListMap = MapUtil.ofEntries(
				MapUtil.entry("DP06", MoreLanguageTagPrinting::getSize)

    );

    @JsonIgnore
    public List<MoreLanguageTagPrinting> getMySelfList(){
        return CollUtil.toList(this);
    }

    @JsonIgnoreType
    public class CodeMapping<K,V> extends Pair<BiConsumer<K, V>, BiConsumer<K, V>> {
        public CodeMapping(BiConsumer<K, V> key, BiConsumer<K, V> value) {
            super(key, value);
        }
    }


}
