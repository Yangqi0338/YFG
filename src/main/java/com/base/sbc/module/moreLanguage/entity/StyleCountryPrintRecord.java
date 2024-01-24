/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.business.StandardColumnType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：款式国家打印表 实体类
 * @address com.base.sbc.module.moreLanguage.entity.StyleCountryPrintRecord
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_country_print_record")
@ApiModel("款式国家打印表 Country")
public class StyleCountryPrintRecord extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 款式编码 */
    @ApiModelProperty(value = "款式编码"  )
    private String bulkStyleNo;
    /** 国家语言id */
    @ApiModelProperty(value = "国家语言id"  )
    private String countryLanguageId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

}
