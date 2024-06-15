package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusQueryDto extends QueryFieldDto {

    @ApiModelProperty(value = "款号列表")
    private String bulkStyleNo;

    @ApiModelProperty(value = "编码列表")
    private String countryCode;

    @ApiModelProperty(value = "品控确认时间")
    private String[] confirmTime;

    @ApiModelProperty(value = "状态")
    private String status;

}
