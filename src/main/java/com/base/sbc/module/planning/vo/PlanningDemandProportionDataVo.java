package com.base.sbc.module.planning.vo;

import cn.hutool.core.util.NumberUtil;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanningDemandProportionDataVo extends PlanningDemandProportionData {
    @ApiModelProperty(value = "下单数")
    private Long orderNum;
    @ApiModelProperty(value = "下单占比")
    private BigDecimal orderRatio;
    @ApiModelProperty(value = "缺口")
    private Long orderGap;

    public String getOrderRatioStr() {
        if (orderRatio == null) {
            return "0%";
        }
        return NumberUtil.toStr(orderRatio.multiply(new BigDecimal("100"))) + "%";
    }
}
