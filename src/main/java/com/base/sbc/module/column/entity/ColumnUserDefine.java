/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.column.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：用户级列自定义头 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.column.entity.ColumnUserDefine
 * @email your email
 * @date 创建时间：2023-12-6 17:33:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_column_user_define")
@ApiModel("用户级列自定义头 ColumnUserDefine")
public class ColumnUserDefine extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 表格编码
     */
    @ApiModelProperty(value = "表格编码")
    private String tableCode;
    /**
     * 默认启用版本
     */
    @ApiModelProperty(value = "默认启用版本")
    private String isDefault;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 表格名称
     */
    @ApiModelProperty(value = "表格名称")
    private String tableName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
