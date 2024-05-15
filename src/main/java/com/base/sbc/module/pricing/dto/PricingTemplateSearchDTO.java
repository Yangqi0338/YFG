package com.base.sbc.module.pricing.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 核价模板列表查询
 * @Author xhj
 * @Date 2023/6/16 15:16
 */
@Data
@ApiModel("核价模板列表查询")
public class PricingTemplateSearchDTO extends QueryFieldDto {
    @ApiModelProperty(value = "品牌", example = "4")
    private String brand;
    @ApiModelProperty(value = "生产类型", example = "CMT")
    private String devtType;
    @ApiModelProperty(value = "模板编码", example = "123")
    private String templateCode;
    @ApiModelProperty(value = "模板名称", example = "123")
    private String templateName;
    @ApiModelProperty(value = "启用停用状态", example = "123")
    private String status;

}
