/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：资料包-尺寸表-明细 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackSizeDetail
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-18 10:56:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_size_detail")
@ApiModel("资料包-尺寸表-明细 PackSizeDetail")
public class PackSizeDetail extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private String packType;
    /**
     * 尺码表id
     */
    @ApiModelProperty(value = "尺码表id")
    private String packSizeId;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String size;
    /**
     * 样衣尺寸
     */
    @ApiModelProperty(value = "样衣尺寸")
    private String template;
    /**
     * 成衣尺寸
     */
    @ApiModelProperty(value = "成衣尺寸")
    private String garment;
    /**
     * 洗后尺寸
     */
    @ApiModelProperty(value = "洗后尺寸")
    private String washing;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
