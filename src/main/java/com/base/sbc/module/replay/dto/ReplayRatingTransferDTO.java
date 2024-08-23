/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
@ApiModel("基础资料-复盘评分转入版型库 ReplayRatingTransferDTO")
public class ReplayRatingTransferDTO {

    /** 模板code */
    @ApiModelProperty(value = "模板code")
    @NotBlank(message = "版型库模板未传入")
    private String templateCode;

    /** 模板名称 */
    @ApiModelProperty(value = "模板名称")
    @NotBlank(message = "版型库名称未传入")
    private String templateName;

    /** 配色id */
    @ApiModelProperty(value = "配色id")
    @NotBlank(message = "款式配色id未传入")
    private String styleColorId;

    /** 款式id */
    @ApiModelProperty(value = "款式id")
    @NotBlank(message = "款式id未传入")
    private String styleId;

    /** 是否转入父编码 */
    @ApiModelProperty(value = "是否转入父编码")
    private YesOrNoEnum transferParentFlag;

}
