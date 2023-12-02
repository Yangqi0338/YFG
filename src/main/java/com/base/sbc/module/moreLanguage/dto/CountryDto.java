package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.moreLanguage.entity.Country;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountryDto extends Country {



}
