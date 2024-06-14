/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-复盘评分 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayRating
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_replay_rating", autoResultMap = true)
@ApiModel("基础资料-复盘评分 ReplayRating")
public class ReplayRating extends BaseDataExtendEntity {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 复盘维度类型 0销售复盘 1版型评分 2面料评分 3颜色评分 */
    @ApiModelProperty(value = "复盘维度类型 0销售复盘 1版型复盘 2面料复盘 3颜色复盘")
    private ReplayRatingType type;
    /** 外键id */
    @ApiModelProperty(value = "外键id")
    private String foreignId;
    /** 复盘状态：0未复盘 1已复盘 */
    @ApiModelProperty(value = "复盘状态：0未复盘 1已复盘")
    private YesOrNoEnum replayFlag;
    /** 评分状态：0未评分 1已评分 */
    @ApiModelProperty(value = "评分状态：0未评分 1已评分")
    private YesOrNoEnum ratingFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
