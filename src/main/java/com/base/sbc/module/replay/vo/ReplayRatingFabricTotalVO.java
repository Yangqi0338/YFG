/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：基础资料-复盘评分Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@ApiModel("基础资料-复盘评分 ReplayRatingFabricTotalVO")
public class ReplayRatingFabricTotalVO {

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /** 单位用量 */
    @ApiModelProperty(value = "单位用量")
    private BigDecimal bulkUnitUse;

    /** 剩余备料 */
    @ApiModelProperty(value = "剩余备料")
    private BigDecimal remainingMaterial;

    /** 投产量 */
    @ApiModelProperty(value = "投产量")
    private BigDecimal production;

    /** 使用米数计算器 */
    @ApiModelProperty(value = "使用米数计算器")
    private UnitConverterEnum unitConverter = UnitConverterEnum.METER;

    /** 投产量 */
    @ApiModelProperty(value = "投产量")
    private BigDecimal realProduction;

    /** 使用米数 */
    @ApiModelProperty(value = "使用米数")
    public BigDecimal getRealProduction() {
        if (production == null) return null;
        return unitConverter.calculate(BigDecimalUtil.mul(production, bulkUnitUse));
    }


}
