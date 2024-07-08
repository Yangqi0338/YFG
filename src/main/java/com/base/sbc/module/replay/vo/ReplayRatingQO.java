/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 类描述：基础资料-复盘评分QueryDto 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.dto.ReplayRatingQueryDto
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReplayRatingQO extends QueryFieldDto {

    /** 复盘维度类型 */
    @ApiModelProperty(value = "复盘维度类型")
    @NotNull(message = "查询类型不能为空")
    private ReplayRatingType type;

    /** 产品季id */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /** 波段 */
    @ApiModelProperty(value = "波段")
    private String bandName;

    /** 大类code */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;

    /** 品类code */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    /** 中类code */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;

    /** 企划等级 */
    @ApiModelProperty(value = "企划等级")
    private ReplayRatingLevelEnum planningLevel;

    /** 销售等级 */
    @ApiModelProperty(value = "销售等级")
    private ReplayRatingLevelEnum saleLevel;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /** 评分状态：0未评分 1已评分 */
    @ApiModelProperty(value = "评分状态：0未评分 1已评分")
    private YesOrNoEnum ratingFlag;

    /** 版型库id */
    @ApiModelProperty(value = "版型库id")
    private String registeringId;

    /** 套版款号 */
    @ApiModelProperty(value = "套版款号")
    private String registeringNo;

    /** 廓形 */
    @ApiModelProperty(value = "廓形")
    private String silhouette;

    /** 申请版型库状态 */
    @ApiModelProperty(value = "申请版型库状态")
    private YesOrNoEnum transferPatternFlag;

    /** 物料编号 */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    /** 面料供应商 */
    @ApiModelProperty(value = "面料供应商")
    private String supplierId;

    /** 面料颜色 */
    @ApiModelProperty(value = "面料颜色")
    private String colorCode;

    /** 面料自主研发 */
    @ApiModelProperty(value = "面料自主研发")
    private YesOrNoEnum materialOwnResearchFlag;

    /** 查询总数标志 */
    @ApiModelProperty(value = "查询总数标志")
    private YesOrNoEnum findTotalFlag;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "字段说明")
    private String fieldExplain;

}
