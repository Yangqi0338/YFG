package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ScmProductionCheckDto {

    /** 订货本Id */
    @NotBlank(message = "订货本Id不能为空")
    private String orderBookDetailId;

    /** 投产编码 */
    @NotBlank(message = "投产编码不能为空")
    private String orderNo;

}
