package com.base.sbc.open.dto;

import com.base.sbc.module.smp.entity.TagPrinting;
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


    private List<MoreLanguageTagPrinting> tagPrintingList;

}
