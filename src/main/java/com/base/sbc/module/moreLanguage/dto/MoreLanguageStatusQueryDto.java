package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusQueryDto extends Page {

    @ApiModelProperty(value = "款号列表")
    private String bulkStyleNo;

    @ApiModelProperty(value = "编码列表")
    private String countryCode;

    @ApiModelProperty(value = "品控确认时间")
    private String[] confirmTime;

}
