/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：工作量评分选项配置 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.entity.WorkloadRatingItemConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workload_rating_item_config")
@ApiModel("工作量评分选项配置 WorkloadRatingItemConfig")
public class WorkloadRatingItemConfig extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 评分项名 */
    @ApiModelProperty(value = "评分项名")
    private String itemName;
    /** 评分项类型 array 数组 dict 字典 structure 结构 */
    @ApiModelProperty(value = "评分项类型 array 数组 dict 字典 structure 结构")
    private String itemType;
    /** 数组值 | 字典key | 结构id */
    @ApiModelProperty(value = "数组值 | 字典key | 结构id")
    private String itemValue;
    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /** 是否是其他 */
    @ApiModelProperty(value = "是否是其他")
    private String isOther;
    /** 是否能配置 */
    @ApiModelProperty(value = "是否能配置")
    private String isConfigShow;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
