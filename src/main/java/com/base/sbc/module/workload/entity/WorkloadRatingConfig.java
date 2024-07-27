/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：工作量评分配置 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.entity.WorkloadRatingConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workload_rating_config")
@ApiModel("工作量评分配置 WorkloadRatingConfig")
public class WorkloadRatingConfig extends BaseDataExtendEntity {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 类型: sample 样衣工 */
    @ApiModelProperty(value = "类型: sample 样衣工")
    private String type;
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 评分项名称 */
    @ApiModelProperty(value = "评分项名称")
    private String itemName;
    /** 评分项值 */
    @ApiModelProperty(value = "评分项值")
    private String itemValue;
    /** 分值 */
    @ApiModelProperty(value = "分值")
    private BigDecimal score;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
