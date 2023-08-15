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
 * 类描述：颜色企划明细 实体类
 * @address com.base.sbc.module.planning.entity.ColorPlanningItem
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-15 13:58:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_color_planning_item")
@ApiModel("颜色企划明细 ColorPlanningItem")
public class ColorPlanningItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 颜色企划id */
    @ApiModelProperty(value = "颜色企划id"  )
    private String colorPlanningId;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colorName;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 潘通色号 */
    @ApiModelProperty(value = "潘通色号"  )
    private String pantoneColor;
    /** 颜色图 */
    @ApiModelProperty(value = "颜色图"  )
    private String colorPic;
    /** 标签（多个逗号分割） */
    @ApiModelProperty(value = "标签（多个逗号分割）"  )
    private String tag;
    /** 标签编码（多个逗号分割） */
    @ApiModelProperty(value = "标签编码（多个逗号分割）"  )
    private String tagCode;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpec;
    /** 色系 */
    @ApiModelProperty(value = "色系"  )
    private String colorSystem;
    /** 16进制 */
    @ApiModelProperty(value = "16进制"  )
    private String colorHex;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

