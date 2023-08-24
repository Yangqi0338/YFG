/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料企划明细 实体类
 * @address com.base.sbc.module.fabric.entity.FabricPlanningItem
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-23 11:02:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_planning_item")
@ApiModel("面料企划明细 FabricPlanningItem")
public class FabricPlanningItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 面料企划id */
    @ApiModelProperty(value = "面料企划id"  )
    private String fabricPlanningId;
    /** 来源：1.新增，2.基础面料库、4.其他 */
    @ApiModelProperty(value = "来源：1.新增，2.基础面料库、4.其他"  )
    private String source;
    /** 来源id */
    @ApiModelProperty(value = "来源id"  )
    private String sourceId;
    /** 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料； */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；"  )
    private String fabricLabel;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
