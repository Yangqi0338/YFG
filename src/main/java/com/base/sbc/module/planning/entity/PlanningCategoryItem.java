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

import java.math.BigDecimal;

/**
 * 类描述：企划-坑位信息 实体类
 * @address com.base.sbc.module.planning.entity.PlanningCategoryItem
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-19 16:52:34
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_category_item")
@ApiModel("企划-坑位信息 PlanningCategoryItem")
public class PlanningCategoryItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 波段企划id */
    @ApiModelProperty(value = "波段企划id"  )
    private String planningBandId;
    /** 品类信息id */
    @ApiModelProperty(value = "品类信息id"  )
    private String planningCategoryId;
    /** 款式图 */
    @ApiModelProperty(value = "款式图"  )
    private String stylePic;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 品类id(小类) */
    @ApiModelProperty(value = "品类id(小类)"  )
    private String categoryId;
    /** 品类名称路径:(中类/小类) */
    @ApiModelProperty(value = "品类名称路径:(中类/小类)"  )
    private String categoryName;
    /** 品类id路径:(中类/小类) */
    @ApiModelProperty(value = "品类id路径:(中类/小类)"  )
    private String categoryIds;
    /** 价格带 */
    @ApiModelProperty(value = "价格带"  )
    private String price;
    /** 关联的素材库数量 */
    @ApiModelProperty(value = "关联的素材库数量"  )
    private BigDecimal materialCount;
    /** 关联历史款 */
    @ApiModelProperty(value = "关联历史款"  )
    private String hisDesignNo;
    /** 状态:0未下发,1已下发,2已完成 */
    @ApiModelProperty(value = "状态:0未下发,1已下发,2已完成"  )
    private String status;
    /** 设计师名称 */
    @ApiModelProperty(value = "设计师名称"  )
    private String designer;
    /** 设计师id */
    @ApiModelProperty(value = "设计师id"  )
    private String designerId;
    /** 任务等级:普通,紧急,非常紧急 */
    @ApiModelProperty(value = "任务等级:普通,紧急,非常紧急"  )
    private String taskLevel;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

