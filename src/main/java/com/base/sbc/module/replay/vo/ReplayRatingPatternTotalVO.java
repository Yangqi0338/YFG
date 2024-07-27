/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

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
@ApiModel("基础资料-复盘评分 ReplayRatingPatternTotalVO")
public class ReplayRatingPatternTotalVO {

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /** 当季销售件数 */
    @ApiModelProperty(value = "当季销售件数")
    private BigDecimal seasonSaleCount;

    /** 当季投产件数(SCM) */
    @ApiModelProperty(value = "当季投产件数(SCM)")
    private BigDecimal seasonProductionCount;

    /** 使用款 */
    @ApiModelProperty(value = "使用款")
    private BigDecimal useCount;

    /** 投产款 */
    @ApiModelProperty(value = "投产款")
    private BigDecimal productionCount;

    /** 当季产销比 */
    private String seasonProductionSaleRate;
    /** 当季产销比 */
    private String patternSuccessRate;

    /** 当季产销比 */
    public String getSeasonProductionSaleRate() {
        if (seasonSaleCount == null || seasonProductionCount == null) return null;
        return BigDecimalUtil.dividePercentage(seasonSaleCount, seasonProductionCount) + "%";
    }

    /** 版型成功率 */
    public String getPatternSuccessRate() {
        return BigDecimalUtil.dividePercentage(productionCount, useCount) + "%";
    }

}
