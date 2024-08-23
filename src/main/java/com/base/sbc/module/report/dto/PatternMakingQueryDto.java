package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PatternMakingQueryDto extends QueryFieldDto {

    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /**
     * 设计师搜索
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    @ApiModelProperty(value = "生产类型名称")
    private String devtTypeName;
}
