/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.dto;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Data
@ApiModel(value = "大货详情多语言请求")
public class HangTagMoreLanguageCheckDTO {

    private static final long serialVersionUID = 1L;

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    @NotBlank(message = "大货款号不能为空")
    private String bulkStyleNo;
    /**
     * 国家语言Id
     */
    @ApiModelProperty(value = "国家语言编码Id")
    @NotBlank(message = "国家语言编码不能为空")
    private String code;
    /**
     * 语言编码
     */
    @ApiModelProperty(value = "语言编码")
    private String languageCode;
    /**
     * 吊牌类型
     */
    @ApiModelProperty(value = "吊牌类型")
    @NotNull(message = "吊牌类型不能为空")
    private CountryLanguageType type;
    /**
     * 来源：PDM,BCS,PRINT(打印)
     */
    @ApiModelProperty(value = "来源：PDM,BCS,PRINT(打印)")
    @NotNull(message = "来源不能为空")
    private SystemSource source;

    public boolean match(CountryLanguageDto countryLanguageDto) {
        return countryLanguageDto.getType() == type && StrUtil.equals(countryLanguageDto.getCode(), code) &&
                (StrUtil.isBlank(languageCode) || StrUtil.equals(countryLanguageDto.getLanguageCode(), languageCode));
    }

}

