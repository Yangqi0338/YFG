package com.base.sbc.open.dto;

import com.base.sbc.module.smp.entity.TagPrinting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MoreLanguageTagPrintingList {
    /**
     * 是否赠品
     */
    @JsonProperty(value = "IsGift")
    private Boolean getIsGift(){
        return tagPrintingList.stream().anyMatch(it-> !Boolean.TRUE.equals(it.getIsGift()));
    }

    /**
     * 商品吊牌价确认
     */
    @JsonProperty(value = "MerchApproved")
    private Boolean getMerchApproved(){
        return tagPrintingList.stream().allMatch(MoreLanguageTagPrinting::getMerchApproved);
    }

    /**
     * 品控部确认
     */
    @JsonProperty(value = "Approved")
    private Boolean getApproved(){
        return tagPrintingList.stream().allMatch(MoreLanguageTagPrinting::getApproved);
    }

    /**
     * 后技术确认
     */
    @JsonProperty(value = "TechApproved")
    private Boolean getTechApproved(){
        return tagPrintingList.stream().allMatch(MoreLanguageTagPrinting::getTechApproved);
    }

    /**
     * 翻译确认
     */
    @JsonProperty(value = "TranslateApproved")
    private Boolean getTranslateApproved(){
        return tagPrintingList.stream().allMatch(MoreLanguageTagPrinting::getTranslateApproved);
    }

    public String getC8_Colorway_WareCode() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getC8_Colorway_WareCode).orElse("");
    }

    public String getStyleCode() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getStyleCode).orElse("");
    }

    public String getC8_Colorway_BatchNo() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getC8_Colorway_BatchNo).orElse("");
    }

    public String getSecCode() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getSecCode).orElse("");
    }

    public String getMainCode() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getMainCode).orElse("");
    }

    public Boolean getIsAccessories() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getIsAccessories).orElse(false);
    }

    public String getC8_Season_Brand() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getC8_Season_Brand).orElse("");
    }

    public String getCareSymbols() {
        return tagPrintingList.stream().findFirst().map(MoreLanguageTagPrinting::getCareSymbols).orElse("");
    }


    private List<MoreLanguageTagPrinting> tagPrintingList;

}
