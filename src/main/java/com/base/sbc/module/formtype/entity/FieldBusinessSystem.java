/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：字段对应下游系统关系 实体类
 * @address com.base.sbc.module.formtype.entity.FieldBusinessSystem
 * @author your name
 * @email your email
 * @date 创建时间：2024-5-31 10:35:28
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_field_business_system")
@ApiModel("字段对应下游系统关系 FieldBusinessSystem")
public class FieldBusinessSystem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 业务系统类型 */
    @ApiModelProperty(value = "业务系统类型"  )
    private String businessType;
    /** 表单字段id */
    @ApiModelProperty(value = "表单字段id"  )
    private String fieldId;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
