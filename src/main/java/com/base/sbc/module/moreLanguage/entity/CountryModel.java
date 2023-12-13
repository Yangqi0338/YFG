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
 * 类描述：多国家号型尺寸翻译表 实体类
 * @address com.base.sbc.module.moreLanguage.entity.CountryModel
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:45
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_country_model")
@ApiModel("多国家号型尺寸翻译表 CountryModel")
public class CountryModel extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 国家编码 */
    @ApiModelProperty(value = "国家语言Id"  )
    private String countryLanguageId;
    /** 号型编码 */
    @ApiModelProperty(value = "号型编码"  )
    private String modelCode;
    /** 号型名称 */
    @ApiModelProperty(value = "号型名称"  )
    private String modelName;
    /** 尺码编码 */
    @ApiModelProperty(value = "尺码编码"  )
    private String basicSizeCode;
    /** 尺码名称 */
    @ApiModelProperty(value = "尺码名称"  )
    private String basicSizeName;
    /** 翻译 */
    @ApiModelProperty(value = "翻译"  )
    private String content;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
