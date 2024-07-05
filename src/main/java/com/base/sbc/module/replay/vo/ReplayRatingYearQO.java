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
public class ReplayRatingYearQO extends QueryFieldDto {

    /** 销售季标签 */
    @ApiModelProperty(value = "销售季标签")
    @NotNull(message = "销售季未选定")
    private YesOrNoEnum seasonFlag;

    /** 复盘维度类型 */
    @ApiModelProperty(value = "复盘维度类型")
    @NotNull(message = "查询类型不能为空")
    private ReplayRatingType type;

    /** 版型库id */
    @ApiModelProperty(value = "版型库id")
//    @ValidCondition(column = "type", columnValue = "PATTERN")
//    @NotBlank(message = "版型库id不能为空")
    private String patternLibraryId;

    /** 物料编码 */
    @ApiModelProperty(value = "物料编码")
//    @ValidCondition(column = "type", columnValue = "FABRIC")
//    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

}
