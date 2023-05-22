/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.entity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：字段值 实体类
 * @address com.base.sbc.module.fieldManagement.entity.FieldVal
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-22 20:35:53
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_field_val")
@ApiModel("字段值 FieldVal")
public class FieldVal extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 关联id */
    @ApiModelProperty(value = "关联id"  )
    private String fId;
    /** 数据分组 */
    @ApiModelProperty(value = "数据分组"  )
    private String dataGroup;
    /** 字段名称 */
    @ApiModelProperty(value = "字段名称"  )
    private String fieldName;
    /** 字段值 */
    @ApiModelProperty(value = "字段值"  )
    private String val;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

