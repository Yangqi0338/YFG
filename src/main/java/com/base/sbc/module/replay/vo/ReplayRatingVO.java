/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("基础资料-复盘评分 ReplayRatingVo")
public class ReplayRatingVO extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 复盘维度类型 0销售复盘 1版型评分 2面料评分 3颜色评分 */
    @ApiModelProperty(value = "复盘维度类型 0销售复盘 1版型评分 2面料评分 3颜色评分")
    private String type;
    /** 外键id */
    @ApiModelProperty(value = "外键id")
    private String foreignId;
    /** 复盘状态：0未复盘 1已复盘 */
    @ApiModelProperty(value = "复盘状态：0未复盘 1已复盘")
    private String replayStatus;
    /** 评分状态：0未评分 1已评分 */
    @ApiModelProperty(value = "评分状态：0未评分 1已评分")
    private String ratingStatus;
    /** 扩展字段 (typeName|关联的主表的非实时数据) */
    @ApiModelProperty(value = "扩展字段 (typeName|关联的主表的非实时数据)")
    private String extend;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
