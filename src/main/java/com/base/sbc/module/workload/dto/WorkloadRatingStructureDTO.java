/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：工作量评分配置QueryDto 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.dto.WorkloadRatingItemQueryDto
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:16
 */
@Data
@ApiModel("工作量评分配置 WorkloadRatingTitleFieldDTO")
public class WorkloadRatingStructureDTO {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    public String prodCategory1st;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    public String prodCategory;
    /**
     * 中类
     */
    @ApiModelProperty(value = "中类")
    public String prodCategory2nd;
    /**
     * 小类
     */
    @ApiModelProperty(value = "小类")
    public String prodCategory3st;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

}
