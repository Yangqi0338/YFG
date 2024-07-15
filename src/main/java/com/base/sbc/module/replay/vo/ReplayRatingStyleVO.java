/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.module.replay.dto.ProductionSaleDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ReplayRatingStyleVO extends ReplayRatingVO {

//    /** 企划等级 */
//    @ApiModelProperty(value = "企划等级")
//    @ExtendField
//    private SluggishSaleLevelEnum planningLevel;
//    /** 季节等级 */
//    @ApiModelProperty(value = "季节等级")
//    @ExtendField
//    private SluggishSaleLevelEnum seasonLevel;
    /**
     * 跳转版型id
     */
    @ApiModelProperty(value = "跳转版型id")
    private String gotoPatternId;

    /** 生产销售 */
    @ApiModelProperty(value = "生产销售")
    @JsonUnwrapped
    private ProductionSaleDTO productionSaleDTO;

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.STYLE;
    }

}
