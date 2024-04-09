/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.entity;

import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 类描述：国家地区表 实体类
 * @address com.base.sbc.module.moreLanguage.entity.Country
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_country_language")
@ApiModel("国家地区表 Country")
public class CountryLanguage extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 国家编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 编码索引 */
    @ApiModelProperty(value = "编码索引"  )
    private Integer codeIndex;
    /** 名称 */
    @ApiModelProperty(value = "名称"  )
    private String name;
    /** 国家编码 */
    @ApiModelProperty(value = "语言编码"  )
    private String languageCode;
    /** 国家编码 */
    @ApiModelProperty(value = "币种编码"  )
    private String coinCode;
    /** 国家名称 */
    @ApiModelProperty(value = "币种名称"  )
    private String coinName;
    /** 号型语种 */
    @ApiModelProperty(value = "号型语种"  )
    private String modelLanguageCode;
    /** 国家名称 */
    @ApiModelProperty(value = "启用状态"  )
    private YesOrNoEnum enableFlag;
    /** 标准类型 */
    @ApiModelProperty(value = "标准类型"  )
    private CountryLanguageType type;
    /** 单条状态 */
    @ApiModelProperty(value = "单条状态"  )
    private YesOrNoEnum singleLanguageFlag = YesOrNoEnum.NO;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private Integer sort;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    public String getLanguageCodeByColumnCode(String standardColumnCode) {
        return standardColumnCode.equals(MoreLanguageProperties.modelStandardColumnCode) ? this.getModelLanguageCode() : this.getLanguageCode();
    }

}
