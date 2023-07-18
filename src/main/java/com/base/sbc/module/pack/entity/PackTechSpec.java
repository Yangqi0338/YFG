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

import java.math.BigDecimal;
/**
 * 类描述：资料包-工艺说明 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackTechSpec
 * @email your email
 * @date 创建时间：2023-7-17 15:54:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_tech_spec")
@ApiModel("资料包-工艺说明 PackTechSpec")
public class PackTechSpec extends BaseDataEntity<String> {

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
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装
     */
    @ApiModelProperty(value = "工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装")
    private String specType;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private BigDecimal sort;
    /**
     * 工艺项目
     */
    @ApiModelProperty(value = "工艺项目")
    private String item;
    /**
     * 工艺项目编码
     */
    @ApiModelProperty(value = "工艺项目编码")
    private String itemCode;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String content;
    /**
     * 描述图片:富文本处理生成的图片
     */
    @ApiModelProperty(value = "描述图片:富文本处理生成的图片")
    private String contentImgUrl;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

