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
@ApiModel("样衣分页查询 SampleDto")
public class SamplePageDto extends Page {

    @ApiModelProperty(value = "样衣ID", example = "680014765321355265")
    private String sampleId;

    @ApiModelProperty(value = "样衣明细ID", example = "680014765321355265")
    private String sampleItemId;

    @ApiModelProperty(value = "位置ID", example = "680014765321355265")
    private String positionId;

    @ApiModelProperty(value = "剩余天数：0-全部，1-1天，2-2天，3-3天，4-一周，5-大于1周", example = "0")
    private Integer daysRemaining;

    @ApiModelProperty(value = "是否逾期：0-否，1是", example = "0")
    private Integer isOverdue;

    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供", example = "0")
    private Integer type;

    @ApiModelProperty(value = "来源：1-新增，2-导入，3-外部", example = "0")
    private Integer fromType;

    @ApiModelProperty(value = "在库状态：0-未入库，1-在库，2-借出，3-删除", example = "0")
    private String status;

    @ApiModelProperty(value = "企业编号", example = "680014765321355265")
    private String companyCode;
}
