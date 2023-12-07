package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageQueryDto extends Page {

    /** 可能为空字符串,表示中国 */
    @NotNull(message = "国家语言不能为空")
    @ApiModelProperty(value = "查询国家语言条件标签Id")
    private String countryLanguageId;


    @ApiModelProperty(value = "标准列码条件标签Code")
    private String standardColumnCode;

}
