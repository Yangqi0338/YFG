/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式BOM指定面料表 实体类
 * @address com.base.sbc.module.style.entity.StyleSpecFabric
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-25 16:04:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_spec_fabric")
@ApiModel("款式BOM指定面料表 StyleSpecFabric")
public class StyleSpecFabric extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 配色表t_style_color 主键id */
    @ApiModelProperty(value = "配色表t_style_color 主键id"  )
    private String styleColorId;
    /** 指定面料厂家 */
    @ApiModelProperty(value = "指定面料厂家"  )
    private String manufacturerFabric;
    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号"  )
    private String manufacturerNumber;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String ingredient;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

