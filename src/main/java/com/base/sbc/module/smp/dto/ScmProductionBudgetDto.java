package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonAnyGetter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScmProductionBudgetDto {

    private String id;
    private BigDecimal amount;
    private BigDecimal tagAmount;
    private String budgetTypeId;
    private String seasonName;
    private String brandName;
    private String yearName;

    private String budgetNo;
    private String budgetTypeName;


}