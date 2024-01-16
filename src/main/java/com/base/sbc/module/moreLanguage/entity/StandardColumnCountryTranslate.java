/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：品名多语言属性值配置表 实体类
 * @address com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:41
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_standard_column_country_translate")
@ApiModel("品名多语言属性值配置表 StandardColumnCountryTranslate")
public class StandardColumnCountryTranslate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 国家编码 */
    @ApiModelProperty(value = "国家语言Id"  )
    private String countryLanguageId;
    /** 表头编码 */
    @ApiModelProperty(value = "表头编码"  )
    private String titleCode;
    /** 表头名称 */
    @ApiModelProperty(value = "表头名称"  )
    private String titleName;
    /** 属性编码 */
    @ApiModelProperty(value = "属性编码"  )
    private String propertiesCode;
    /** 属性名称 */
    @ApiModelProperty(value = "属性名称"  )
    private String propertiesName;
    /** 翻译 */
    @ApiModelProperty(value = "翻译"  )
    private String content;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
