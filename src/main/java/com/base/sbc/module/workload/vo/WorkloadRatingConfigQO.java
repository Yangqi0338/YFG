/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.vo;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.workload.WorkloadRatingItemType;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：工作量评分选项配置QueryDto 实体类
 * @address com.base.sbc.module.workload.dto.WorkloadRatingConfigQueryDto
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkloadRatingConfigQO extends Page {
    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** id */
    @ApiModelProperty(value = "id")
    private String id;
    /** 类型: sample 样衣工 */
    @ApiModelProperty(value = "类型: sample 样衣工")
    @NotNull(message = "工作量类型不能为空")
    private WorkloadRatingType type;
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    @NotBlank(message = "品牌不能空")
    private String brand;
    /** 评分项名 */
    @ApiModelProperty(value = "评分项名")
    private String itemName;
    /** 评分项类型 array 数组 dict 字典 structure 结构 */
    @ApiModelProperty(value = "评分项类型 array 数组 dict 字典 structure 结构")
    @JsonIgnore
    private WorkloadRatingItemType itemType;
    /** 数组值 | 枚举值 | 字典key | 结构id */
    @ApiModelProperty(value = "数组值 | 枚举值 | 字典key | 结构id")
    @JsonIgnore
    private String itemValue;
    /** 是否能配置 */
    @ApiModelProperty(value = "是否能配置")
    private YesOrNoEnum isConfigShow;
    /** 是否构建表头 */
    @ApiModelProperty(value = "是否构建表头")
    private boolean buildTitleField = Boolean.TRUE;
    /** 是否查询依赖数据 */
    @ApiModelProperty(value = "是否查询依赖数据")
    private boolean searchValue = Boolean.TRUE;
    /** 不查询那些类型 */
    @ApiModelProperty(value = "不查询那些类型")
    private List<WorkloadRatingItemType> notSearch;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
