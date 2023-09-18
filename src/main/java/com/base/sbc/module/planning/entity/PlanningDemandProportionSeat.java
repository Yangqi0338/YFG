/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：企划-需求占比坑位表 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.PlanningDemandProportionSeat
 * @email your email
 * @date 创建时间：2023-9-14 11:16:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_demand_proportion_seat")
@ApiModel("企划-需求占比坑位表 PlanningDemandProportionSeat")
public class PlanningDemandProportionSeat extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 需求id
     */
    @ApiModelProperty(value = "需求id")
    private String demandId;
    /**
     * 需求占比id
     */
    @ApiModelProperty(value = "需求占比id")
    private String planningDemandProportionDataId;
    /**
     * 匹配的配色id
     */
    @ApiModelProperty(value = "匹配的配色id")
    private String styleColorId;
    /**
     * 匹配标志:z0坑位(未匹配),a1完美匹配(坑位),b2手动匹配(坑位)
     */
    @ApiModelProperty(value = "匹配标志:z0坑位(未匹配),a1完美匹配(坑位),b2手动匹配(坑位)")
    private String matchFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

