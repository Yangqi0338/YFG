/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：表单类型 实体类
 * @address com.base.sbc.module.formType.entity.FormType
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:01
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_form_type")
public class FormType extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 分组 */
    @ApiModelProperty(value = "分组"  )
    private String groupId;
    /** 数据库表名 */
    @ApiModelProperty(value = "数据库表名"  )
    private String name;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String coding;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 说明 */
    @ApiModelProperty(value = "说明"  )
    private String tableExplain;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
