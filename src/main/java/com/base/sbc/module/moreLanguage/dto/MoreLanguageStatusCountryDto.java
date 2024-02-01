package com.base.sbc.module.moreLanguage.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCountryDto {

    @ApiModelProperty(value = "国家语言编码")
    private String code;

    @ApiModelProperty(value = "状态")
    public String getStatus(){
        return statusCode.getText();
    }

    private StyleCountryStatusEnum statusCode;

    @DateTimeFormat("yyyy/MM/dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss", timezone = "GMT+8")
    private Date time;

    private String person;

}
