/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：工作量评分数据计算结果QueryDto 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.dto.WorkloadRatingDetailQueryDto
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkloadRatingDetailQO extends Page {
    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 类型: sample 样衣工 */
    @ApiModelProperty(value = "类型: sample 样衣工")
    private String type;
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 评分项值集合, 逗号拼接 */
    @ApiModelProperty(value = "评分项值集合, 逗号拼接")
    private String itemValue;
    /** 结果 */
    @ApiModelProperty(value = "结果")
    private BigDecimal result;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
