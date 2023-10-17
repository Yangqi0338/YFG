package com.base.sbc.module.pricing.dto;

import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.pricing.entity.StylePricing;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/10/16 19:31:33
 * @mail 247967116@qq.com
 */
@Data
public class StylePricingStatusDTO extends StylePricing {
    private String ids;
    /**
     * 是否计控确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控确认 0.否、1.是")
    private String controlConfirm;

    /**
     * 是否商品吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否商品吊牌确认 0.否、1.是")
    private String productHangtagConfirm;
    /**
     * 是否计控吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控吊牌确认 0.否、1.是")
    private String controlHangtagConfirm;
}
