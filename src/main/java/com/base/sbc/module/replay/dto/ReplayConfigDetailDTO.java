/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：基础资料-复盘管理 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Data
public class ReplayConfigDetailDTO {

    /** 今年 */
    @ApiModelProperty(value = "今年")
    private List<ReplayConfigTimeDTO> thisYear;

    /** 去年 */
    @ApiModelProperty(value = "去年")
    private List<ReplayConfigTimeDTO> lastYear;

    /** 前年 */
    @ApiModelProperty(value = "前年")
    private List<ReplayConfigTimeDTO> beforeYear;

}
