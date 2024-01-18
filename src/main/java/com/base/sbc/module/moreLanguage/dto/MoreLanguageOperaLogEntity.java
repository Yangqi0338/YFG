package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.operalog.dto.OperaLogDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.StringJoiner;

/**
 * 类描述：资料包-公共筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackCommonSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("公共分页筛选条件 PackCommonSearchDto")
public class MoreLanguageOperaLogEntity extends OperaLogEntity {

    public String getCode() {
        if (StrUtil.isBlank(this.getContent())) return "";
        return this.getContent().split("-")[0];
    }

    public String getCountryLanguageType() {
        if (StrUtil.isBlank(this.getContent()) || this.getContent().split("-").length <= 1) return "";
        return this.getContent().split("-")[1];
    }

    public String getStandardColumnCode() {
        return this.getDocumentCode();
    }

    public String getStandardColumnName() {
        return this.getDocumentName();
    }

    @JsonIgnore
    private String content;

    @JsonIgnore
    private String documentCode;

    @JsonIgnore
    private String documentName;

}
