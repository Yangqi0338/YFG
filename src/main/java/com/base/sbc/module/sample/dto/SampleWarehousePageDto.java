package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣仓库 dto
 * @address com.base.sbc.module.sample.dto.SampleWarehouseDto
 */
@Data
@ApiModel("样衣仓库 SampleWarehouseDto")
public class SampleWarehousePageDto extends Page {

    @ApiModelProperty(value = "ID", example = "680014765321355265")
    private String id;

    @ApiModelProperty(value = "企业编码", example = "680014765321355265")
    private String companyCode;

}
