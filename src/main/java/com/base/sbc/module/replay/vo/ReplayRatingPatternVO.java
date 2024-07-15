/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import cn.hutool.core.text.StrJoiner;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@ApiModel("基础资料-复盘评分 ReplayRatingVo")
public class ReplayRatingPatternVO extends ReplayRatingVO {

    /** 套版款号id */
    @ApiModelProperty(value = "套版款号id")
    private String registeringId;

    /** 套版款号 */
    @ApiModelProperty(value = "套版款号")
    private String registeringNo;

    /** 版型库id */
    @ApiModelProperty(value = "版型库id")
    private String patternLibraryId;

    /** 版型库code */
    @ApiModelProperty(value = "版型库code")
    private String patternLibraryCode;

    /** 廓形code */
    @ApiModelProperty(value = "廓形code")
    private String silhouetteCode;

    /** 廓形 */
    @ApiModelProperty(value = "廓形")
    @JsonIgnore
    private String silhouetteName;

    /** 模板code */
    @ApiModelProperty(value = "模板code")
    private String templateCode;

    /** 模板name */
    @ApiModelProperty(value = "模板name")
    private String templateName;

    /** 当季销售件数 */
    @ApiModelProperty(value = "当季销售件数")
    private BigDecimal seasonSaleCount = BigDecimal.ZERO;

    /** 当季投产件数(SCM) */
    @ApiModelProperty(value = "当季投产件数(SCM)")
    private BigDecimal seasonProductionCount;

    /** 使用款 */
    @ApiModelProperty(value = "使用款")
    private BigDecimal useCount;

    /** 投产款 */
    @ApiModelProperty(value = "投产款")
    private BigDecimal productionCount;

    /** 是否转入版型库 */
    @ApiModelProperty(value = "是否转入版型库")
    private YesOrNoEnum transferPatternFlag;

    @Override
    protected StrJoiner categoryNameJoiner() {
        return super.categoryNameJoiner().append(silhouetteName);
    }

    /** 当季产销比 */
    public String getSeasonProductionSaleRate() {
        return BigDecimalUtil.dividePercentage(seasonSaleCount, seasonProductionCount) + "%";
    }

    /** 版型成功率 */
    public String getPatternSuccessRate() {
        return BigDecimalUtil.dividePercentage(productionCount, useCount) + "%";
    }

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.PATTERN;
    }


}
