/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.annotation.ExtendField;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import com.base.sbc.config.enums.business.workload.WorkloadRatingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：工作量评分数据计算结果 实体类
 * @address com.base.sbc.module.workload.entity.WorkloadRatingDetail
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_workload_rating_detail", autoResultMap = true)
@ApiModel("工作量评分数据计算结果 WorkloadRatingDetail")
public class WorkloadRatingDetail extends BaseDataExtendEntity {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    @ApiModelProperty(value = "面料名称")
    @ExtendField
    private String fabricName;

    @ApiModelProperty(value = "追加合计")
    @ExtendField
    private String otherName;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 类型: sample 样衣工 */
    @ApiModelProperty(value = "类型: sample 样衣工")
    private WorkloadRatingType type;
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 评分项id集合, 逗号拼接 */
    @ApiModelProperty(value = "评分项id集合, 逗号拼接")
    private String itemId;
    /** 评分项值集合, 逗号拼接 */
    @ApiModelProperty(value = "评分项值集合, 逗号拼接")
    private String itemValue;
    /** 代理分数的外键id,现改为必定代理,可理解为必有值 */
    @ApiModelProperty(value = "代理分数的关联表外键id")
    private String proxyKey;
    /** 结果 */
    @ApiModelProperty(value = "结果")
    private BigDecimal result;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    @JsonIgnore
    public String getOriginItemValue() {
        return itemValue;
    }

    @JsonIgnore
    public String getOriginItemId() {
        return itemId;
    }

}
