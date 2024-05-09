package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.vo.EditPermissionReturnVo;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusDto extends EditPermissionReturnVo {

    @NotEmpty(message = "款号列表")
    @ApiModelProperty(value = "款号")
    private String bulkStyleNo;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "接收部门"  )
    private String receiveDeptId;

    @ApiModelProperty(value = "国家编码列表")
    private List<MoreLanguageStatusCountryDto> countryList;

}
