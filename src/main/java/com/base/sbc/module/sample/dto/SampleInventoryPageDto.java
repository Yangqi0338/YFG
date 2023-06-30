package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 类描述： 样衣盘点分页查询
 * @address com.base.sbc.module.sample.dto.SampleInventoryPageDto
 */
@Data
@ApiModel("样衣盘点分页查询 SampleInventoryPageDto")
public class SampleInventoryPageDto extends Page {

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    /** 企业编号 */
    @ApiModelProperty(value = "企业编号", example = "680014765321355265")
    private String companyCode;

    /** 样衣盘点ID */
    @ApiModelProperty(value = "样衣盘点ID", example = "680014765321355265")
    private String sampleInventoryId;

    /** 状态：1-正常，2-作废，3-删除 */
    @ApiModelProperty(value = "状态：1-正常，2-作废，3-删除", example = "1")
    private String status;
}
