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

/**
 * 类描述：字段显示隐藏配置 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.FieldDisplayConfig
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-28 16:31:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_field_display_config")
@ApiModel("字段显示隐藏配置 FieldDisplayConfig")
public class FieldDisplayConfig extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 类型(款式看板/企划看板)
     */
    @ApiModelProperty(value = "类型(款式看板/企划看板)")
    private String type;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 配置
     */
    @ApiModelProperty(value = "配置")
    private String config;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

