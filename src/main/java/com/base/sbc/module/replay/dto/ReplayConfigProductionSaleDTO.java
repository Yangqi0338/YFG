/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.config.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：基础资料-复盘管理 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Data
public class ReplayConfigProductionSaleDTO {

    /** 投产量 */
    @ApiModelProperty(value = "投产量")
    private BigDecimal production;

    /** 销售量 */
    @ApiModelProperty(value = "销售量")
    private BigDecimal sale;

    /** 产销比 */
    @ApiModelProperty(value = "产销比")
    public String getProductionSaleRate() {
        return BigDecimalUtil.dividePercentage(sale, production) + "%";
    }


    public ReplayConfigProductionSaleDTO plus(ReplayConfigProductionSaleDTO mathContext) {
        return this;
    }
}
