/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-号型类型 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-9 15:29:29
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_model_type")
@ApiModel("基础资料-号型类型 BasicsdatumModelType")
public class BasicsdatumModelType extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
    /** Dimension 1 Type */
    @ApiModelProperty(value = "Dimension 1 Type"  )
    private String dimensionType;
    /** 尺码 */
    @ApiModelProperty(value = "尺码"  )
    private String size;
    /** 尺码id集合 */
    @ApiModelProperty(value = "尺码id集合"  )
    private String sizeIds;
    /** 尺码编码 */
    @ApiModelProperty(value = "尺码编码"  )
    private String sizeCode;
    /** 尺码真实编码 */
    @ApiModelProperty(value = "尺码真实编码"  )
    private String  sizeRealCode;
    /** 基础 尺码 */
    @ApiModelProperty(value = "基础 尺码"  )
    private String basicsSize;
    /** 基础尺码编码（对应尺码排序） */
    @ApiModelProperty(value = "基础尺码编码（对应尺码排序）"  )
    private String basicsSizeSort;
    /** 默认尺码 */
    @ApiModelProperty(value = "默认尺码"  )
    private String defaultSize;
    /** 默认尺码id集合 */
    @ApiModelProperty(value = "默认尺码id集合"  )
    private String defaultSizeIds;
    /** 默认尺码编码 */
    @ApiModelProperty(value = "默认尺码编码"  )
    private String defaultSizeCode;
    /** 默认尺码真实编码 */
    @ApiModelProperty(value = "默认尺码真实编码"  )
    private String  defaultSizeRealCode;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String categoryId;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;
    /** 款式确认(1确定，0未确认) */
    @ApiModelProperty(value = "款式确认(1确定，0未确认)"  )
    private String styleConfirmation;
    /** 材料确认(1确定，0未确认) */
    @ApiModelProperty(value = "材料确认(1确定，0未确认)"  )
    private String materialsConfirmation;
    /** 尺寸确认(1确定，0未确认) */
    @ApiModelProperty(value = "尺寸确认(1确定，0未确认)"  )
    private String sizeConfirmation;
    /** 发送状态(1发送，0未发送) */
    @ApiModelProperty(value = "发送状态(1发送，0未发送)"  )
    private String sendStatus;
    /** 尺码标签id */
    @ApiModelProperty(value = "尺码标签id"  )
    private String sizeLabelId;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

