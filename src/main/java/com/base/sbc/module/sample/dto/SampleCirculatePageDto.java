package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类描述： 样衣分页查询
 * @address com.base.sbc.module.sample.dto.SamplePageDto
 */
@Data
@ApiModel("样衣借还分页查询 SampleDto")
public class SampleCirculatePageDto extends Page {

    @ApiModelProperty(value = "样衣借还ID", example = "680014765321355265")
    private String sampleCirculateId;

    @ApiModelProperty(value = "在库状态：0-未入库，1-在库，2-借出，3-删除", example = "0")
    private String status;

    @ApiModelProperty(value = "企业编号", example = "680014765321355265")
    private String companyCode;
}
