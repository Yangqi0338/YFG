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
 * 类描述：基础资料-物料档案-物料颜色 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:22
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_material_color")
@ApiModel("基础资料-物料档案-物料颜色 BasicsdatumMaterialColor")
public class BasicsdatumMaterialColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 颜色代码 */
    @ApiModelProperty(value = "颜色代码"  )
    private String colorCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colorName;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColorCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

