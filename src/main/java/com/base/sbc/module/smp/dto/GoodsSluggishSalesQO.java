/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.smp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
@ApiModel("基础资料-复盘评分 ReplayRatingBulkWarnVO")
public class GoodsSluggishSalesQO {

    /**
     * 大货款号[范围]
     */
    @ApiModelProperty(value = "大货款号[范围]")
    private String bulkStyleNo;

    /**
     * 年份[范围]
     */
    @ApiModelProperty(value = "年份[范围]")
    private List<Integer> year;

    /**
     * 周数[范围]
     */
    @ApiModelProperty(value = "周数[范围]")
    private List<String> weekends;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

}
