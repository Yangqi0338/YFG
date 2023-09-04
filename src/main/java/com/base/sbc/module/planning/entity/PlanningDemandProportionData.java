/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：企划-需求占比数据表 实体类
 * @address com.base.sbc.module.planning.entity.PlanningDemandProportionData
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-31 15:17:47
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_demand_proportion_data")
@ApiModel("企划-需求占比数据表 PlanningDemandProportionData")
public class PlanningDemandProportionData extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 需求id */
    @ApiModelProperty(value = "需求id"  )
    private String demandId;
    /** 分类 */
    @ApiModelProperty(value = "分类"  )
    private String classify;
    /** 分类名称 */
    @ApiModelProperty(value = "分类名称"  )
    private String classifyName;
    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    private Integer num;
    /** 占比 */
    @ApiModelProperty(value = "占比"  )
    private String proportion;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

