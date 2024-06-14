/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.annotation.ExtendField;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailDimensionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingDetailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-复盘评分-详情 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayRatingDetail
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_replay_rating_detail", autoResultMap = true)
@ApiModel("基础资料-复盘评分-详情 ReplayRatingDetail")
public class ReplayRatingDetail extends BaseDataExtendEntity {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /** 文件Url */
    @ApiModelProperty(value = "文件Url")
    @ExtendField
    private String fileUrl;

    /** 跟进人 */
    @ApiModelProperty(value = "跟进人")
    @ExtendField
    private String followUserName;

    /** 责任人 */
    @ApiModelProperty(value = "责任人")
    @ExtendField
    private String userName;


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 复盘评分id */
    @ApiModelProperty(value = "复盘评分id")
    private String replayRatingId;
    /** 评分类型 0评分 1后续改善 */
    @ApiModelProperty(value = "评分类型 0评分 1后续改善")
    private ReplayRatingDetailType type;
    /** 维度类型 0默认 1版型 2面料 3颜色 */
    @ApiModelProperty(value = "维度类型 0默认 1版型 2面料 3颜色")
    private ReplayRatingDetailDimensionType dimensionType;
    /** 描述 */
    @ApiModelProperty(value = "描述")
    private String description;
    /** 评级 */
    @ApiModelProperty(value = "评级")
    private Integer level;
    /** 文件id */
    @ApiModelProperty(value = "文件id")
    private String fileId;
    /** 文件格式 */
    @ApiModelProperty(value = "文件格式")
    private String fileType;
    /** 跟进人id */
    @ApiModelProperty(value = "跟进人id")
    private String followUserId;
    /** 责任人id */
    @ApiModelProperty(value = "责任人id")
    private String userId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
