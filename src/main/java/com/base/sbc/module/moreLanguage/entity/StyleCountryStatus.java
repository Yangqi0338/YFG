/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("t_style_country_status")
@ApiModel("国家款式状态表 StyleCountryStatus")
public class StyleCountryStatus extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 款式编码 */
    @ApiModelProperty(value = "款式编码"  )
    @NotBlank(message = "款式编码不能为空")
    private String bulkStyleNo;
    /** 状态 */
    @ApiModelProperty(value = "状态"  )
    @NotNull(message = "状态不能为空")
    private StyleCountryStatusEnum status;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 名字 */
    @ApiModelProperty(value = "名字"  )
    private String name;
    /** 标准类型 */
    @ApiModelProperty(value = "标准类型"  )
    private CountryLanguageType type;
    /** 标准列编码 */
    @ApiModelProperty(value = "标准列编码"  )
    private String standardColumnCode;
    /** 检查详情Json */
    @ApiModelProperty(value = "检查详情Json"  )
    private String checkDetailJson;
    /** 打印时间 */
    @ApiModelProperty(value = "打印时间"  )
    private Date printTime;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

}
